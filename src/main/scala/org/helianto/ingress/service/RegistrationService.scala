package org.helianto.ingress.service

import java.util.Locale

import org.helianto.core.domain.{Entity, Identity}
import org.helianto.core.service.{EntityInstallService, IdentityService}
import org.helianto.ingress.domain.{Registration, UserToken}
import org.helianto.ingress.repository.RegistrationRepository
import org.helianto.security.domain.UserDetailsAdapter
import org.helianto.user.service.UserInstallService
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService}
import org.springframework.social.connect.Connection
import org.springframework.social.connect.web.ProviderSignInUtils
import org.springframework.stereotype.Service
import org.springframework.ui.Model
import org.springframework.web.context.request.WebRequest

import scala.util.{Success, Try}

/**
  * Registration service.
  */
@Service
class RegistrationService
(userDetailsService: UserDetailsService
 , identityInstallService: IdentityService
 , entityInstallService: EntityInstallService
 , userInstallService: UserInstallService
 , registrationRepository: RegistrationRepository
 , responseService: ResponseService
 , userTokenService: UserTokenService){

  private val logger: Logger = LoggerFactory.getLogger(classOf[RegistrationService])

  @Autowired(required = false)
  val providerSignInUtils: ProviderSignInUtils = null

  @Value("${helianto.contextName}")
  val contextName: String = ""

  /**
    * Get the registration page.
    *
    * @param confirmationToken
    * @param model
    * @param locale
    * @param userType
    */
  // TODO validate if registration.isAdmin matches userType
  def get(confirmationToken: String, model: Model, locale: Locale, userType: String) =
    findRegistrationOption(confirmationToken) match {
      case Some(registration) =>
        responseService.registerResponse(model, locale, registration, userType)
      case None => responseService.signupResponse(model, locale, new Registration(contextName, true))
    }

  private[service] def findRegistrationOption(id: String) = Option(registrationRepository.findOne(id))

  /**
    * Continuous registration event sourcing.
    *
    * Registration is meant to be updated at every submission form command change. This is
    * similar to event sourcing.
    *
    * @param command
    * @param ipAddress
    */
  // TODO implement registration ES
  def saveOrUpdate(command: Registration, ipAddress: String = ""): Registration = {
    val registration = findRegistrationOption((command.getId)) match {
      case Some(r) => r.merge(command)
      case None => command
    }
    registrationRepository.saveAndFlush(registration.merge(ipAddress))
  }

  /**
    * The user is requiring to become admin of a new  entity.
    *
    * @param command
    * @param model
    * @param locale
    */
  // TODO notify
  def createAdmin(command: Registration, model: Model, locale: Locale) = {
    val registration = saveOrUpdate(command)
    entityInstallService.findOption(registration.getEntityAlias) match {
      case Some(entity) => responseService.registerDenyResponse(model, locale, registration, "entityExists")
      case _ =>
//        val userToken = userTokenService.install(registration.getPrincipal, "CREATE_ADMIN")
        entityInstallService.install(registration)
//        notificationService.sendWelcome(userToken)
        responseService.loginResponse(model, locale)
    }
  }

  /**
    * The user is requiring association to an existing entity.
    *
    * @param registration
    * @param model
    * @param locale
    */
  def subscribe(registration: Registration, model: Model, locale: Locale) = {
    model.addAttribute("form", registration)
    entityInstallService.findOption(registration.getEntityAlias) match {
      case Some(entity) => registerPendingUser(registration, entity)
      case None => responseService.registerDenyResponse(model, locale, registration, "entityNotFound")
    }
  }

  /**
    * Install user from registration data.
    *
    * @param registration
    * @param model
    * @param locale
    */
  def install(registration: Registration, model: Model, locale: Locale) = {

  }

  /**
    * Register.
    *
    * @param connection
    * @param request
    * @param model
    * @return
    */
  private[service] def register(connection: Connection[_], request: WebRequest, model: Model): String = {
    val registration = fromProviderUser(connection)
    model.addAttribute("registration", registration)
    // social provider (likely Facebook) authorized
    Option(registration).map(_.getPrincipal) match {
      case Some(principal) if (principal.indexOf('@') > 0) =>{
        // an existing user is trying to sign-in via provider
        Try(userDetailsService.loadUserByUsername(principal)) match {
          case Success(userDetails) => {
            signin(userDetails)
            Option(providerSignInUtils) match {
              case Some(provider) =>
                provider.doPostSignUp(userDetails.asInstanceOf[UserDetailsAdapter].getUserId + "", request)
                responseService.homeResponse(model, request.getLocale)
              case None => responseService.registerResponse(model, request.getLocale, new Registration(principal), "")
            }
          }
          case _ => {
            // and we have e-mail, but no user, go to register
            responseService.registerResponse(model, request.getLocale, new Registration(principal), "")
          }
        }
      }
      case _ => {
        logger.info("None email in SignupForm")
        // we still need to ask for e-mail
        model.addAttribute("hasPrincipal", false)
        responseService.registerResponse(model, request.getLocale, new Registration(), "")
      }
    }
  }

  private[service] def signin(userDetails: UserDetails) = {
    val authentication = new UsernamePasswordAuthenticationToken(userDetails, null.asInstanceOf[Any], userDetails.getAuthorities)
    SecurityContextHolder.getContext.setAuthentication(authentication)
  }



  /**
    * Submit a request to the current admin where user is requiring association to an existing entity.
    *
    * @param registration
    * @param entity
    */
  // TODO install user
  // TODO notify
  private def registerPendingUser(registration: Registration, entity: Entity) = {
    val identity = identityInstallService.install(registration)
    val userToken = userTokenService.createOrRefreshToken(registration.getPrincipal, registration.getEntityAlias)
//    var user: User = userInstallService.installUser(entity, registration.getEmail, UserState.PENDING.getValue)
//    try {
//      subscriptionService.sendSubscriptionRequest(entity, identity)
//      subscriptionService.sendSubscriptionRequestNotification(userToken)
//    } catch {
//      case e:Exception  => e.printStackTrace()
//    }
  }

//  /**
//    * Remove a lead.
//    *
//    * @param leadPrincipal
//    */
//  def removeLead(leadPrincipal: String): String = {
//    val leads: java.util.List[Lead] = leadRepository.findByPrincipal(leadPrincipal)
//    import scala.collection.JavaConversions._
//    for (lead <- leads) {
//      leadRepository.delete(lead)
//    }
//    leadPrincipal
//  }

  private[service] def fromProviderUser(connection: Connection[_]): Registration = {
    Option(connection) match {
      case Some(c) =>
        Option(c.fetchUserProfile) match {
          case Some(userProfile) =>
            Option(c.createData) match {
              case Some(data) =>
                new Registration(
                  Option(userProfile.getEmail).getOrElse("")
                  , Option(userProfile.getFirstName).getOrElse("")
                  , Option(userProfile.getLastName).getOrElse("")
                  , Option(data.getImageUrl).getOrElse("")
                  , Option(data.getProviderUserId).getOrElse("")
                )
              case None => null
            }
          case None => null
        }
      case None => null
    }
  }

}
