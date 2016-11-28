package org.helianto.ingress.util

import java.security.Principal
import java.util.Locale

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.ui.Model

trait FreemarkerMixIn {

  val logger: Logger = LoggerFactory.getLogger(classOf[FreemarkerMixIn])

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
    logger.info(s"Rendering frame-bootstrap with baseName $baseName.")
    "frame-bootstrap"
  }

}
