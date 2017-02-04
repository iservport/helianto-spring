package org.helianto.ingress.controller

import java.util.Locale
import javax.validation.Valid

import org.helianto.ingress.domain.Registration
import org.helianto.ingress.service.{ResponseService, SignupService}
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation._
import org.springframework.web.context.request.WebRequest

/**
  * Base classe to SignUpController.
  *
  * @author mauriciofernandesdecastro
  */
@Controller
@RequestMapping(value = Array("/signup"))
class SignupController(signUpService:SignupService, responseService:ResponseService) {

  /**
    * Signup request page.
    *
    * @param model
    * @param contextId
    * @param principal
    * @param request
    */
  @GetMapping
  def getSignupPage(model: Model, @RequestParam(defaultValue = "1") contextId: Integer, @RequestParam(required = false) principal: String, request: WebRequest):String =
    { val x = signUpService.prompt(request, model);println("XXXXXXX");println(model.asMap().get("registration"));x}


  /**
    * After user submission.
    *
    * <p>
    *   If the principal type, defined by helianto.register.principal is e-mail, send confirmation by e-mail, otherwise
    *   use an alternative method.
    * </p>
    *
    * @param model
    * @param command
    * @param error
    * @param request
    * @param locale
    */
  @PostMapping
  def submitSignupPage(model: Model, @Valid command: Registration, error: BindingResult, request: WebRequest, locale: Locale): String =
    signUpService.confirm(command, request, model, locale)

  /**
    * Once required by the configuration (@see MfaProperties), the user must submit a valid confirmation code.
    *
    * @param model
    * @param registrationId
    * @param confirmationCode
    * @param request
    * @param locale
    */
  @PostMapping(path = Array("/confirm"))
  def submitSignupConfirmPage(model: Model, @RequestParam registrationId: String, @RequestParam confirmationCode: String, request: WebRequest, locale: Locale): String =
    signUpService.submit(registrationId, confirmationCode, request, model, locale)

}