package org.helianto.ingress.controller

import java.security.Principal
import java.util.Locale

import org.springframework.ui.Model

trait FreemarkerMixIn {

  /**
    * Default response.
    *
    * @param userAuthentication
    * @param model
    * @param baseName
    * @param locale
    */
  protected def response(userAuthentication: Principal, model: Model, baseName: String, locale: Locale = new Locale("pt", "BR")) = {
    model.addAttribute("baseName", baseName)
    model.addAttribute("locale", locale.toString.toLowerCase)
    model.addAttribute("locale_", locale.toString.replace("_", "-").toLowerCase)
    getTemplateName(baseName, model)
  }

  /**
    * Default template name.
    */
  protected def getTemplateName(baseName: String, model: Model) = {
    "frame-bootstrap"
  }

}
