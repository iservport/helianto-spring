package org.helianto.ingress.controller

import java.util.Locale

import org.helianto.ingress.service.ResponseService
import org.helianto.user.domain.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RequestMethod, RequestParam}

/**
  * Login controller.
  *
  * @author mauriciofernandesdecastro
  */
@Controller
@RequestMapping(value = Array("/login"))
class LoginController(responseService:ResponseService) {

  /**
    * Login page.
    *
    * @param model model injetado automaticamente pelo container para receber o modelo
    * @param locale locale injetado automaticamente pelo container para identificar a localização
    */
  @GetMapping
  def getSignInPage(model: Model, locale: Locale) = {
    responseService.loginResponse(model, locale)
  }

  /**
    * Login errors.
    *
    * @param model model
    * @param error error parameter
    * @param locale locale
    */
  @GetMapping(params = Array("error"))
  def showLoginErrorParam(model: Model, @RequestParam error: String, locale: Locale) = {
    responseService.loginErrorResponse(model, locale, error)
  }

  /**
    * Login errors.
    *
    * @param model  model
    * @param type   error type
    * @param locale locale
    */
  @GetMapping(value = Array("/error"))
  def showLoginErrorPath(model: Model, @RequestParam `type`: String, locale: Locale) = {
    responseService.loginErrorResponse(model, locale, `type`)
  }

}