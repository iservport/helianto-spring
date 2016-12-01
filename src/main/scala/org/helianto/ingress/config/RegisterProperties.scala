package org.helianto.ingress.config

import org.springframework.boot.context.properties.ConfigurationProperties

import scala.beans.BeanProperty
import scala.util.{Failure, Success, Try}

@ConfigurationProperties(prefix="helianto.register")
class RegisterProperties {

  @BeanProperty var admin: String = "" // ALLOW | DENY | ${defaultDomain}

  @BeanProperty var principal: String = "" // EMAIL | PIN | PHONE

  @BeanProperty var pinMask: String = ""

  @BeanProperty var pun: String = "" // REQUIRED | NONE

  @BeanProperty var punMask: String = ""

  @BeanProperty var contextDomain: String = ""

  @BeanProperty var inLineCss: String = ""

  def principalType: String = Option(principal).getOrElse("EMAIL").toUpperCase

  def enableAdmin = Array("ALLOW").contains(Option(admin).getOrElse("DENY").toUpperCase)

  def requireDomain = !Array("ALLOW","DENY").contains(Option(admin).getOrElse("DENY").toUpperCase)

  def defaultDomain = if (requireDomain) Try(Option(admin).get) match {
    case Success(domain) => domain
    case Failure(e) =>
      throw new IllegalArgumentException("You must either supply a [defaultDomain] property or set it as 'DENY' | 'ALLOW'")
  }

  def enablePun = Array("REQUIRED").contains(Option(pun).getOrElse("NONE").toUpperCase)

  def enableContextDomain = Option(pun).exists(_.nonEmpty)

}
