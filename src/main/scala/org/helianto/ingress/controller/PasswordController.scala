package org.helianto.ingress.controller

import java.util.Locale

import org.helianto.ingress.service.{PasswordService, ResponseService}
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation._

/**
  * Password change controller.
  */
@Controller
@RequestMapping(value = Array("/password"))
class PasswordController(passwordService: PasswordService) {

  /**
    * Password change request page after e-mail submission.
    *
    * @param model
    * @param confirmationToken
    * @param locale
    */
  @GetMapping(path = Array("/change"))
  def getChangePasswordPage(model: Model, @RequestParam confirmationToken: String, locale: Locale): String =
    passwordService.get(confirmationToken, model, locale)

  /**
    * Get e-mail recovery page.
    *
    * @param error
    * @param model
    * @param locale
    */
  @GetMapping(path = Array("/recovery"))
  def getRecoveryPage(error: String, model: Model, locale: Locale): String =
    passwordService.getRecovery(error, model, locale)


  /**
    * Do change password.
    *
    * @param model
    * @param confirmationToken
    * @param password
    * @param locale
    */
  @PostMapping(path = Array("/change"), params = Array("confirmationToken", "password"))
  def postChangePassword(model: Model, @RequestParam confirmationToken: String, @RequestParam password: String, locale: Locale): String =
    passwordService.doChangePassword(confirmationToken, password, model, locale)

  /**
    * Post e-mail recovery page.
    *
    * @param model
    * @param principal
    * @param locale
    */
  @PostMapping(path = Array("/recovery"), params = Array("principal"))
  def postRecoveryPage(model: Model, @RequestParam principal: String, locale: Locale): String =
    passwordService.doRecover(principal, model, locale)

}