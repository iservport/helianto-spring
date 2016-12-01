package org.helianto.ingress.config

import org.springframework.boot.context.properties.ConfigurationProperties

import scala.beans.BeanProperty

@ConfigurationProperties(prefix="helianto.mfa")
class MfaProperties {

  @BeanProperty var cellPhone: String = ""

  @BeanProperty var service: String = "0"

  @BeanProperty var label: String = ""

  def enableCellPhone = Array("REQUIRED", "OPTIONAL").contains(Option(cellPhone).getOrElse("NONE").toUpperCase)

  def requireCellPhone = Array("REQUIRED").contains(Option(cellPhone).getOrElse("NONE").toUpperCase)

  def requireMfa = enableCellPhone && !Array("NONE").contains(Option(service).getOrElse("NONE").toUpperCase)

}
