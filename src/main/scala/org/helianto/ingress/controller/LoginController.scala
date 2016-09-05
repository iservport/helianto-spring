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
    * @param model modelo
    * @param locale locale
    */
  @GetMapping
  def getSignInPage(model: Model, locale: Locale) = {
    model.addAttribute("titlePage", "Login")
    responseService.loginResponse(model, locale)
  }

  /**
    * Login errors.
    *
    * @param model modelo
    * @param error parâmetro de erro
    * @param locale locale
    */
  @RequestMapping(params = Array("error"), method = Array(RequestMethod.GET))
  def showLoginErrorParam(model: Model, @RequestParam error: String, locale: Locale) = {
    model.addAttribute("error", error)
    val user: User = new User
    user.setAccountNonExpired(false)
    model.addAttribute("user", user)
    responseService.loginResponse(model, locale)
  }

  /**
    * Login errors.
    *
    * @param model  modelo
    * @param type   tipo de erro
    * @param locale locale
    */
  @RequestMapping(value = Array("/error"), method = Array(RequestMethod.GET))
  def showLoginErrorPath(model: Model, @RequestParam `type`: String, locale: Locale) = {
    model.addAttribute("error", true)
    val user: User = new User
    user.setAccountNonExpired(false)
    model.addAttribute("user", user)
    responseService.loginResponse(model, locale)
  }

}