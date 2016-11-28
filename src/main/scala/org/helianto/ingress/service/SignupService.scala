package org.helianto.ingress.service

import javax.servlet.http.HttpServletRequest

import org.helianto.ingress.domain.{Registration, UserToken}
import org.helianto.message.service.SecurityNotificationService
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.social.connect.web.ProviderSignInUtils
import org.springframework.stereotype.Service
import org.springframework.ui.Model
import org.springframework.web.context.request.WebRequest

/**
  * Provider sign-up service.
  */
@Service
class SignupService
(notificationService:SecurityNotificationService
 , registrationService: RegistrationService
 , responseService: ResponseService) {

  @Autowired(required = false)
  val providerSignInUtils: ProviderSignInUtils = null

  @Value("${helianto.contextName}")
  val contextName: String = ""

  /**
    * The page may have been requested either after a plain "create new account" link or
    * after a failed provider sign in.
    *
    * @param request
    * @param model
    */
  def signUpOrRegister(request: WebRequest, model: Model): String =
    Option(providerSignInUtils).map(_.getConnectionFromSession(request)) match {
      case Some(connection) => registrationService.register(connection, request, model)
      case None => responseService.signupResponse(model, request.getLocale, new Registration(contextName, true))
    }

  def submitSignupPage(command: Registration, request: WebRequest, model: Model) = {
    val registration = Option(request.getHeader("X-FORWARDED-FOR")) match {
      case Some(ip) => registrationService.saveOrUpdate(command, ip)
        // TODO Find another way to retrieve IP
      case None => registrationService.saveOrUpdate(command, "")
    }
    if (registration.isAdmin) { notificationService.sendSignUpAdmin(registration) }
    else { notificationService.sendSignUpUser(registration) }
//    model.addAttribute("emailSent", emailSent)
    responseService.confirmationResponse(model, request.getLocale)
  }

}
