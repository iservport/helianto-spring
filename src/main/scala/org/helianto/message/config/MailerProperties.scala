package org.helianto.message.config

import java.net.URI

import org.helianto.message.domain.ContactData
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.web.util.UriComponentsBuilder

import scala.beans.BeanProperty

@ConfigurationProperties(prefix="helianto.mailer")
class MailerProperties {

  @BeanProperty var url: String = ""

  @BeanProperty var entityName: String = ""

  @BeanProperty var service: ServiceProperties = null

  @BeanProperty var senderEmail: String = ""

  @BeanProperty var senderFullName: String = ""

  def sender = ContactData(senderEmail, senderFullName, entityName)

  def mailerUri: URI = UriComponentsBuilder.fromHttpUrl(url).path(service.serviceName).build(true).toUri

  override def toString = s"MailerProperties($url, $entityName, $service, $senderEmail, $senderFullName)"

}
