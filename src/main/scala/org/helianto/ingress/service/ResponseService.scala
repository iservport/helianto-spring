package org.helianto.ingress.service

import java.util.Locale

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.ui.Model

/**
  * Response service
  */
@Service
class ResponseService {

  @Autowired
  val env:Environment = null

  def loginResponse(model: Model, locale: Locale) = {
    model.addAttribute("main", "security/login.html")
    response(model, locale)
  }

  def confirmationResponse(model: Model, locale: Locale) = {
    model.addAttribute("main", "security/read-your-email.html")
    response(model, locale)
  }

  def changeResponse(model: Model, locale: Locale) = {
    model.addAttribute("main", "security/password-change.html")
    response(model, locale)
  }

  def recoveryResponse(model: Model, locale: Locale) = {
    model.addAttribute("main", "security/password-recovery.html")
    response(model, locale)
  }

  /**
    * Internal response.
    *
    * @param model    modelo
    * @param locale   locale
    */
  private def response(model: Model, locale: Locale) = {
    model.addAttribute("sender", env.getProperty("iservport.sender.mail"))
    model.addAttribute("baseName", "security")
    model.addAttribute("copyright", env.getProperty("helianto.copyright", ""))
    model.addAttribute("buildNumber", env.getProperty("buildNumber", "-1"))
    // TODO tratar locale como é recebido do browser
    // no momento forçamos para pt_BR (stdLocale ao invés de locale)
    val stdlocale = new Locale("pt", "BR")
    if (stdlocale != null) {
      model.addAttribute("locale", stdlocale.toString.toLowerCase)
      model.addAttribute("locale_", stdlocale.toString.replace("_", "-").toLowerCase)
    }
    "frame-security"
  }
}
