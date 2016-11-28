package org.helianto.ingress.config

import org.springframework.boot.context.properties.ConfigurationProperties

import scala.beans.BeanProperty

@ConfigurationProperties(prefix="helianto.register")
class RegisterProperties {

  @BeanProperty var pun: String = ""

  @BeanProperty var inLineCss: String = ""

  def enablePun = Array("REQUIRED").contains(Option(pun).getOrElse("NONE").toUpperCase)

}
