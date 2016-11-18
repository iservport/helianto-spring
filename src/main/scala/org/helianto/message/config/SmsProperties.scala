package org.helianto.message.config

import java.net.URI

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.web.util.UriComponentsBuilder

import scala.beans.BeanProperty

@ConfigurationProperties(prefix="helianto.sms")
class SmsProperties {

  @BeanProperty var url: String = ""

  @BeanProperty var entityName: String = ""

  @BeanProperty var service: ServiceProperties = null

  @BeanProperty var accessToken: String = ""

  @BeanProperty var senderFullName: String = ""

  def serviceUri: URI = UriComponentsBuilder.fromHttpUrl(url).path(service.serviceName).build(true).toUri

}
