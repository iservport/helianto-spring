package org.helianto.ingress.service

import javax.servlet.http.HttpServletRequest

import org.helianto.core.domain.Identity
import org.helianto.ingress.domain.{Registration, UserToken}
import org.helianto.message.service.SecurityNotificationService
import org.helianto.security.domain.UserDetailsAdapter
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService}
import org.springframework.social.connect.{Connection, ConnectionData, UserProfile}
import org.springframework.social.connect.web.ProviderSignInUtils
import org.springframework.stereotype.Service
import org.springframework.ui.Model
import org.springframework.web.context.request.WebRequest

import scala.util.{Success, Try}

/**
  * Provider sign-up service.
  */
@Service
class SignupService
(userDetailsService: UserDetailsService
 , notificationService:SecurityNotificationService
 , userTokenService: UserTokenService
 , responseService: ResponseService) {

  private val logger: Logger = LoggerFactory.getLogger(classOf[SignupService])

  @Autowired(required = false)
  val providerSignInUtils: ProviderSignInUtils = null

  def signUpOrRegister(request: WebRequest, model: Model): String =
    Option(providerSignInUtils) match {
      case Some(provider) =>
        Option(provider.getConnectionFromSession(request)) match {
          case Some(connection) =>
            val registration = fromProviderUser(connection)
            model.addAttribute("registration", registration)
            signUpOrRegister(registration.getPrincipal, request, model)
          case None => responseService.signupResponse(model, request.getLocale, new Registration)
        }
      case None => responseService.signupResponse(model, request.getLocale, new Registration)
    }

  /**
    * Sign up or register.
    *
    * @param principal
    * @param model
    */
  private[service] def signUpOrRegister(principal: String, request: WebRequest, model: Model): String = {
    // social provider (likely Facebook) authorized
    Option(principal) match {
      case Some(principal) if (principal.indexOf('@') > 0) =>{
        // an existing user is trying to sign-in via provider
        Try(userDetailsService.loadUserByUsername(principal)) match {
          case Success(userDetails) => {
            signin(userDetails)
            Option(providerSignInUtils) match {
              case Some(provider) =>
                provider.doPostSignUp(userDetails.asInstanceOf[UserDetailsAdapter].getUserId + "", request)
                responseService.homeResponse(model, request.getLocale)
              case None => responseService.registerResponse(model, request.getLocale, new Identity(principal))
            }
          }
          case _ => {
            // and we have e-mail, but no user, go to register
            responseService.registerResponse(model, request.getLocale, new Identity(principal))
          }
        }
      }
      case _ => {
        logger.info("None email in SignupForm")
        // we still need to ask for e-mail
        model.addAttribute("hasPrincipal", false)
        responseService.registerResponse(model, request.getLocale, new Identity())
      }
    }
  }

  private[service] def signin(userDetails: UserDetails) = {
    val authentication = new UsernamePasswordAuthenticationToken(userDetails, null.asInstanceOf[Any], userDetails.getAuthorities)
    SecurityContextHolder.getContext.setAuthentication(authentication)
  }

  private[service] def fromProviderUser(connection: Connection[_]): Registration = {
    val userProfile: UserProfile = connection.fetchUserProfile
    val data: ConnectionData = connection.createData
    val registration: Registration = new Registration(
      Option(userProfile.getEmail).getOrElse("")
      , Option(userProfile.getFirstName).getOrElse("")
      , Option(userProfile.getLastName).getOrElse("")
      , Option(data.getImageUrl).getOrElse("")
    )
    registration.setProviderUserId(data.getProviderUserId)
    registration
  }

  def submitSignupPage(command: UserToken, request: HttpServletRequest, model: Model) = {
    val userToken = Option(request.getHeader("X-FORWARDED-FOR")) match {
      case Some(ip) => userTokenService.saveOrUpdate(command, ip)
        // TODO Find another way to retrieve IP
      case None => userTokenService.saveOrUpdate(command, request.getRequestURI)
    }
    notificationService.sendSignUp(userTokenService.createOrRefreshToken(userToken.getPrincipal, UserToken.TokenSources.SIGNUP.name()))
//    model.addAttribute("emailSent", emailSent)
    responseService.confirmationResponse(model, request.getLocale)
  }


}
