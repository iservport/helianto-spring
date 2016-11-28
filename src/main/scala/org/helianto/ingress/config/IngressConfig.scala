package org.helianto.ingress.config

import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class IngressConfig {

  @Bean
  def welcomeProperties  = new WelcomeProperties

  @Bean
  def mfaProperties  = new MfaProperties

  @Bean
  def entityInstallProperties  = new RegisterProperties

}
