package org.helianto.ingress.controller

import java.util.Locale

import org.helianto.core.domain.Identity
import org.helianto.ingress.domain.Registration
import org.helianto.ingress.service.ResponseService
import org.helianto.message.domain.TotalVoiceSmsMessage
import org.helianto.message.service.TotalVoiceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, ResponseBody}

/**
  * This controller is intended for testing only.
  *
  * @param service
  */
@Controller
@RequestMapping(value = Array("/security"))
class SecurityTestController(service: ResponseService) {

  @Autowired
  val smsService: TotalVoiceService = null

  /**
    * The same page is presented jat login.
    *
    * It prompts for username and password.
    *
    * @param model
    * @param locale
    */
  @GetMapping(Array("/login"))
  def getLogin(model: Model, locale: Locale): String = {
    service.loginResponse(model, locale)
  }

  /**
    * The same page is presented just after the "Try now!" call to action.
    *
    * It prompts for basic user data. After submission, the system
    * should handle user confirmation, assigning him a code.
    *
    * @param model
    * @param locale
    */
  @GetMapping(Array("/signup"))
  def getSignup(model: Model, locale: Locale): String = {
    service.signupResponse(model, locale, new Registration)
  }

  /**
    * The same page is presented after the user follows a link in the confirmation e-mail,
    * or receives a code via cellphone.
    *
    * @param model
    * @param locale
    */
  @GetMapping(Array("/register/admin"))
  def getRegisterAdmin(model: Model, locale: Locale): String = {
    val identity = new Identity
    service.registerResponse(model, locale, new Registration(identity), "admin")
  }

  /**
    * The same page (here with a sample) is presented after the user follows a link in the confirmation e-mail,
    * or receives a code via cellphone.
    *
    * @param model
    * @param locale
    */
  @GetMapping(Array("/register/admin/sample"))
  def getRegisterAdminSample(model: Model, locale: Locale): String = {
    val registration = new Registration("someone@someOrg.com", "John", "Doe")
    registration.setEntityAlias("SomeOrg.com")
    registration.setPun("12345678000123")
    registration.setStateCode("41")
    service.registerResponse(model, locale, registration, "admin")
  }

  /**
    * The same page is presented after the user follows a link in the confirmation e-mail,
    * or receives a code via cellphone.
    *
    * @param model
    * @param locale
    */
  @GetMapping(Array("/register/user"))
  def getRegisterUser(model: Model, locale: Locale): String = {
    val identity = new Identity
    service.registerResponse(model, locale, new Registration(identity), "user")
  }

  @GetMapping(Array("/register/phone"))
  @ResponseBody
  def getRegisterPhone(model: Model, locale: Locale): TotalVoiceSmsMessage = {
    smsService.send(new TotalVoiceSmsMessage("41991835430", "Seu código de verificação xxx é ZZZ"))
  }

}
