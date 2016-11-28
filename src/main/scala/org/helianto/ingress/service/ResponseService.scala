package org.helianto.ingress.service

import java.util.Locale

import org.helianto.ingress.config.{MfaProperties, RegisterProperties, WelcomeProperties}
import org.helianto.ingress.domain.Registration
import org.helianto.security.config.GoogleProperties
import org.helianto.user.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.social.{FacebookProperties, LinkedInProperties}
import org.springframework.stereotype.Service
import org.springframework.ui.Model

/**
  * Response service
  */
@Service
class ResponseService
(val welcomeProperties: WelcomeProperties
 , val mfaProperties: MfaProperties
 , val registerProperties: RegisterProperties
)  {

  @Autowired(required=false)
  val facebookProperties: FacebookProperties = null

  @Autowired(required=false)
  val googleProperties: GoogleProperties = null

  @Autowired(required=false)
  val linkedInProperties: LinkedInProperties = null

  def loginResponse(model: Model, locale: Locale) = {
    model.addAttribute("titlePage", "Login")
    model.addAttribute("inLineCss", welcomeProperties.inLineCss)
    model.addAttribute("main", "security/login.html")
    response(model, locale)
  }

  def loginErrorResponse(model: Model, locale: Locale, error: String) = {
    model.addAttribute("error", error)
    model.addAttribute("inLineCss", welcomeProperties.inLineCss)
    model.addAttribute("main", "security/login.html")
    val user: User = new User
    user.setAccountNonExpired(false)
    model.addAttribute("user", user)
    loginResponse(model, locale)
  }

  def confirmationResponse(model: Model, locale: Locale) = {
    model.addAttribute("main", "security/read-your-email.html")
    model.addAttribute("inLineCss", welcomeProperties.inLineCss)
    response(model, locale)
  }

  def changeResponse(model: Model, locale: Locale) = {
    model.addAttribute("main", "security/password-change.html")
    model.addAttribute("inLineCss", welcomeProperties.inLineCss)
    response(model, locale)
  }

  def recoveryResponse(model: Model, locale: Locale) = {
    model.addAttribute("main", "security/password-recovery.html")
    model.addAttribute("inLineCss", welcomeProperties.inLineCss)
    response(model, locale)
  }

  def registerResponse(model: Model, locale: Locale, registration: Registration, userType: String) = {
    if (!Array("admin", "user").contains(userType)) throw new IllegalArgumentException(s"Invalid registration page $userType")
    if (Option(registerProperties).exists(_.enablePun)) { model.addAttribute("enablePun", "TRUE") }
    model.addAttribute("registration", registration)
    model.addAttribute("inLineCss", registerProperties.inLineCss)
    model.addAttribute("main", s"security/register-$userType.html")
    response(model, locale)
  }

  // TODO template
  def registerDenyResponse(model: Model, locale: Locale, registration: Registration, message: String) = {
    model.addAttribute(message, true)
    model.addAttribute("form", registration)
    model.addAttribute("inLineCss", registerProperties.inLineCss)
    model.addAttribute("main", "security/register.html")
    response(model, locale)
  }

  def signupResponse(model: Model, locale: Locale, registration: Registration) = {
    if (mfaProperties.enableCellPhone) { model.addAttribute("enableCellPhone", "TRUE") }
    if (mfaProperties.requireCellPhone) { model.addAttribute("requireCellPhone", "TRUE") }
    model.addAttribute("registration", registration)
    model.addAttribute("inLineCss", registerProperties.inLineCss)
    model.addAttribute("main", "security/signup.html")
    response(model, locale)
  }

  def homeResponse(model: Model, locale: Locale) = "redirect:/"

  /**
    * Internal response.
    *
    * @param model    modelo
    * @param locale   locale
    */
  private[service] def response(model: Model, locale: Locale): String = {
    model.addAttribute("baseName", "security")
    if (Option(facebookProperties).nonEmpty) { model.addAttribute("enableFacebook", "TRUE") }
    if (Option(linkedInProperties).nonEmpty) { model.addAttribute("enableLinkedin", "TRUE") }
    if (Option(googleProperties).nonEmpty) { model.addAttribute("enableGoogle", "TRUE") }
    model.addAttribute("baseLogo", welcomeProperties.logo)
    model.addAttribute("brandName", welcomeProperties.brandName)
    model.addAttribute("copyright", welcomeProperties.copyright)
    model.addAttribute("buildNumber", welcomeProperties.buildNumber)
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
