package org.helianto.ingress.service

import java.util.Locale

import org.helianto.core.domain.Identity
import org.helianto.ingress.domain.UserToken
import org.helianto.message.service.SecurityNotificationService
import org.helianto.security.service.SecurityService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.ui.Model

/**
  * Password service.
  *
  * @author mauriciofernandesdecastro
  */
@Service
class PasswordService
( val userTokenService: UserTokenService
  , val securityService: SecurityService
  , val notificationService: SecurityNotificationService
  , val responseService: ResponseService
){

  private val logger = LoggerFactory.getLogger(classOf[PasswordService])

  /**
    * Get password change page.
    *
    * @param confirmationToken
    * @param model
    * @param locale
    */
  def get(confirmationToken: String, model: Model, locale: Locale) =
    userTokenService.getIdentityOption(confirmationToken) match {
      case Some(identity) =>
        model.addAttribute("confirmationToken", confirmationToken)
        responseService.changeResponse(model, locale)
      case None =>
        model.addAttribute("recoverFailMsg", "label.user.password.recover.fail.message.1")
        model.addAttribute("recoveryFail", "true")
        responseService.changeResponse(model, locale)
    }

  /**
    * Get recovery page.
    *
    * @param error
    * @param model
    * @param locale
    */
  def getRecovery(error: String, model: Model, locale: Locale) = responseService.recoveryResponse(model, locale)

  /**
    * True encrypted password matches the changing one.
    *
    * @param pass
    * @param identity
    */
  def ckeckPassword(pass: String, identity: Identity) = securityService.ckeckPassword(pass, identity)

  /**
    * Do change password.
    *
    * @param principal
    * @param password
    */
  def changePassword(principal: String, password: String) = securityService.changePassword(principal, password)

  /**
    *
    * @param confirmationToken
    * @param password
    * @param model
    * @param locale
    */
  def doChangePassword(confirmationToken: String, password: String, model: Model, locale: Locale) =
    userTokenService.getIdentityOption(confirmationToken) match {
      case Some(identity) =>
        model.addAttribute("email", identity.getPrincipal)
        securityService.changePassword(identity.getPrincipal,password)
        responseService.loginResponse(model, locale)
      case None =>
        model.addAttribute("recoverFail", "false")
        model.addAttribute("recoverFailMsg", "label.user.password.recover.fail.message.0")
        responseService.changeResponse(model, locale)
    }

  /**
    * Password recovery submission.
    *
    * @param principal
    * @param model
    * @param locale
    */
  def doRecover(principal: String, model: Model, locale: Locale) = {
    userTokenService.createOrRefreshToken(principal, "PASSWORD_RECOVERY") match {
      case token: UserToken if token.getId>0 =>
        val responseText = notificationService.sendRecovery(token)
        logger.debug(s"Received $responseText")
        model.addAttribute("emailSent", true)
      case _ =>
        model.addAttribute("emailSent", false)
    }
    responseService.signUpEmailResponse(model, locale)
  }

}
