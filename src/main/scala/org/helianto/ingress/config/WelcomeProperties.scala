package org.helianto.ingress.config

import org.springframework.boot.context.properties.ConfigurationProperties

import scala.beans.BeanProperty

@ConfigurationProperties(prefix="helianto.welcome")
class WelcomeProperties {

  @BeanProperty var brandName: String = ""

  @BeanProperty var logo: String = ""

  @BeanProperty var salutation: String = ""

  @BeanProperty var inLineCss: String = ""

  @BeanProperty var copyright: String = ""

  @BeanProperty var buildNumber: String = "0"

}
