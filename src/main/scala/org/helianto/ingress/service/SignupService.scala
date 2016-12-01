package org.helianto.ingress.service

import java.util.Locale

import org.helianto.ingress.config.MfaProperties
import org.helianto.ingress.domain.Registration
import org.helianto.message.domain.TotalVoiceSmsMessage
import org.helianto.message.service.{SecurityNotificationService, TotalVoiceService}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.context.MessageSource
import org.springframework.social.connect.web.ProviderSignInUtils
import org.springframework.stereotype.Service
import org.springframework.ui.Model
import org.springframework.web.context.request.WebRequest

import scala.util.{Failure, Success, Try}

/**
  * Provider sign-up service.
  */
@Service
class SignupService
(notificationService:SecurityNotificationService
 , registrationService: RegistrationService
 , messageSource: MessageSource
 , responseService: ResponseService) {

  private val logger = LoggerFactory.getLogger(classOf[SignupService])

  @Autowired(required = false)
  val providerSignInUtils: ProviderSignInUtils = null

  @Autowired(required = false)
  val totalVoiceService: TotalVoiceService = null

  @Autowired
  val mfaProperties: MfaProperties = null

  @Value("${helianto.contextName}")
  val contextName: String = ""

  /**
    * The page may have been requested either after a plain "create new account" link or
    * after a failed provider sign in.
    *
    * @param request
    * @param model
    */
  def prompt(request: WebRequest, model: Model): String =
    Option(providerSignInUtils).map(_.getConnectionFromSession(request)) match {
      case Some(connection) => registrationService.register(connection, request, model)
      case None => responseService.signUpPromptResponse(model, request.getLocale, new Registration(contextName, true))
    }

  /**
    * SignUp page submission triggers confirmation via e-mail or sms, if possible, or resume signUp.
    *
    * @param command the registration command
    * @param request the request
    * @param model the model
    * @param locale the locale
    *
    * @return the response page
    */
  def confirm(command: Registration, request: WebRequest, model: Model, locale: Locale) = {
    val registration = registrationService.saveOrUpdate(command, getIp(request))
    if      (confirmViaSmsAfterSignUp(registration, locale)) responseService.signUpMfaResponse(model, request.getLocale, registration)
    else if (confirmViaEmailAfterSignUp(registration)) responseService.signUpEmailResponse(model, request.getLocale)
    else     resumeSignUp(registration, model, request.getLocale)
  }

  /**
    * After the user submits a confirmation code received by SMS, he/she is either prompted with the
    * registration page or invited to submit the code again.
    *
    * @param registrationId the registration id
    * @param confirmationCode the confirmation code
    * @param request the web request
    * @param model the model
    * @param locale user's locale
    */
  def submit(registrationId: String, confirmationCode: String, request: WebRequest, model: Model, locale: Locale): String =
    registrationService.findRegistrationOption(registrationId) match {
      case Some(registration) =>
        Try(confirmationCode.toInt) match {
          case Success(code) =>
            if (registration.verifyConfirmationCode(code).isConfirmationCodeVerified) {
              logger.debug(s"Registration $registrationId verified.")
              confirmViaEmailAfterSignUp(registrationService.saveOrUpdate(registration, getIp(request)))
              resumeSignUp(registration, model, request.getLocale)
            }
            else {
              logger.warn(s"Unable to verify registration $registrationId.")
              model.addAttribute("invalidCode", "TRUE")
              // TODO activate captcha
              responseService.signUpMfaResponse(model, request.getLocale, registration)
            }
          case Failure(e) => throw new IllegalArgumentException("Invalid confirmation code")
        }
      case None => throw new IllegalArgumentException("Invalid registration id")
    }

  private[service] def resumeSignUp(registration: Registration, model: Model, locale: Locale): String =
    Option(registration.isAdmin) match {
      case Some(true) => responseService.registerResponse(model, locale, registration, "admin")
      case _ => responseService.registerResponse(model, locale, registration, "user")
    }

  // TODO Find another way to retrieve IP
  private[service] def getIp(request: WebRequest) = Option(request.getHeader("X-FORWARDED-FOR")) match {
    case Some(ip) => ip
    case None => ""
  }

  private[service] def confirmViaEmailAfterSignUp(registration: Registration): Boolean =
    if (registration.getPrincipalType=="EMAIL") {
      logger.debug("Required confirmation by e-mail.")
      Option(registration.isAdmin) match {
        case Some(true) => notificationService.sendSignUpAdmin(registration)
        case _ => notificationService.sendSignUpUser(registration)
      }
      true
    }
    else false

  private[service] def confirmViaSmsAfterSignUp(registration: Registration, locale: Locale): Boolean =
    if (mfaProperties.requireMfa && !registration.isConfirmationCodeVerified) {
      logger.debug(s"Code verification is ${!registration.isConfirmationCodeVerified}")
      Option(totalVoiceService) match {
        case Some(smsService) =>
          val text = messageSource.getMessage("signup.sms.MESSAGE"
            , Array(contextName, registration.getConfirmationCode.toString), locale)
          val x = totalVoiceService.send(new TotalVoiceSmsMessage(registration.getCellPhone, text))
          logger.info(s"SMS notification $x")
          true
        case None => throw new IllegalArgumentException("Mfa required, but service is unavailable.")
    }
  } else false

}
