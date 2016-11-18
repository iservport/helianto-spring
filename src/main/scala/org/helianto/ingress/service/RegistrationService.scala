package org.helianto.ingress.service

import java.util.Locale

import org.helianto.core.domain.Entity
import org.helianto.core.repository.{CityRepository, EntityRepository}
import org.helianto.core.service.{EntityInstallService, IdentityService}
import org.helianto.ingress.domain.Registration
import org.helianto.ingress.repository.RegistrationRepository
import org.helianto.user.service.UserInstallService
import org.springframework.stereotype.Service
import org.springframework.ui.Model

/**
  * Registration service.
  */
@Service
class RegistrationService
(cityRepository: CityRepository
 , identityInstallService: IdentityService
 , entityInstallService: EntityInstallService
 , userInstallService: UserInstallService
 , registrationRepository: RegistrationRepository
 , responseService: ResponseService
 , userTokenService: UserTokenService){

  /**
    * Get the page.
    *
    * @param confirmationToken
    * @param model
    * @param locale
    */
  def get(confirmationToken: String, model: Model, locale: Locale) =
  Option(userTokenService.findPreviousSignupAttempts(confirmationToken, 5)) match {
    case Some(identity) => responseService.registerResponse(model, locale, identity)
    case None => responseService.signupResponse(model, locale, new Registration)
  }

  /**
    * Continuous registration event sourcing.
    *
    * Registration is meant to be updated at every submission form state change. This is
    * similar to event sourcing.
    *
    * @param registration
    */
  // TODO implement registration ES
  def updateRegistration(registration: Registration): Unit =
    registrationRepository.save(registration)

  /**
    * The user is requiring to become admin of a new  entity.
    *
    * @param registration
    * @param model
    * @param locale
    */
  // TODO notify
  def createAdmin(registration: Registration, model: Model, locale: Locale) = {
    model.addAttribute("form", registration)
    entityInstallService.findOption(registration.getEntityAlias) match {
      case Some(entity) => responseService.registerDenyResponse(model, locale, registration, "entityExists")
      case _ =>
//        val userToken = userTokenService.install(registration.getPrincipal, "CREATE_ADMIN")
        entityInstallService.install(registration.getCityId, registration.getEntityAlias, registration.getPrincipal)
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

}
