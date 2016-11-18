package org.helianto.message.config

import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.client.RestTemplate

@Configuration
class MessageConfig {

  @Bean
  def restTamplate = new RestTemplate

  @Bean
  def mailerProperties  = new MailerProperties

  @Bean
  def smsProperties  = new SmsProperties

  @Bean
  def messageSource = {
    val messageSource = new ResourceBundleMessageSource()
    messageSource.setBasenames("_i18n/default")
    messageSource.setUseCodeAsDefaultMessage(true)
    messageSource.setDefaultEncoding("UTF-8")
    messageSource
  }

}
