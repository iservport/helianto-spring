package org.helianto.message.service

import java.util.Locale

import org.helianto.message.config.MailerProperties
import org.helianto.message.domain.{ContactData, Message, MessageData, MessageDefaults}
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service

import scala.io.Codec

@Service
class MessageService(messageSource: MessageSource, mailerProperties: MailerProperties) {

  @Value("${helianto.api.url}") val apiUrl = ""

  private[service] var templatePrefix = "helianto.sendgrid.template."

  def createMessage(recipientData: ContactData, service: String, prefix: String, token: String): Message =
    new Message(mailerProperties.sender, recipientData, service, messageData(prefix), messageDefaults, token)

  def getMessageUri() = mailerProperties.mailerUri

  private[service] def c(key: String)(implicit locale:Locale, codec: Codec) =
    m(key)("common", locale, codec)

  private[service] def m(key: String)(implicit prefix:String, locale:Locale, codec: Codec) =
    new String(messageSource.getMessage(s"$prefix.$key", null, locale).getBytes(),"UTF-8")

  private[service] def messageDefaults(implicit locale:Locale = Locale.getDefault) =
    MessageDefaults(c("greeting"), apiUrl, c("seeOnline"), c("sentByText"), c("disclaimer"), c("unsubscribeText"),
      c("unsubscribeCaption"), "unsubscribe", c("ensure"), c("copyright"))

  private[service] def messageData(implicit prefix:String, locale:Locale = Locale.getDefault) =
    MessageData(m("subject"), m("title"), m("procedure"), m("callToAction"), m("fallBack"), m("trailingInfo"))

}
