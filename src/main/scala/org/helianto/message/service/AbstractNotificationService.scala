package org.helianto.message.service

import java.util.Locale

import org.helianto.ingress.domain.UserToken
import org.helianto.message.config.MailerProperties
import org.helianto.message.domain._
import org.helianto.message.repository.MessageLogRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.http.{HttpEntity, HttpHeaders, HttpMethod, MediaType}
import org.springframework.web.client.RestTemplate

import scala.io.Codec
import scala.util.{Failure, Success, Try}

/**
  * Notification base class.
  */
abstract class AbstractNotificationService {

  private[service] val logger: Logger = LoggerFactory.getLogger(classOf[AbstractNotificationService])

  val messageLogRepository: MessageLogRepository

  val restTemplate: RestTemplate

  val messageSource: MessageSource

  val mailerProperties: MailerProperties

  @Value("${helianto.api.url}") val apiUrl = ""

//  @Value("${helianto.mailer.url}") val rootUrl = ""
//
//  @Value("${helianto.mailer.sender.noReplyEmail}") val senderEmail: String = ""
//
//  @Value("${helianto.mailer.sender.rootFullName}") val senderName: String = ""
//
//  @Value("${helianto.mailer.sender.entityName}") val entityName: String = ""

  private[service] var templatePrefix = "helianto.sendgrid.template."

  private[service] def c(key: String)(implicit locale:Locale, codec: Codec) =
    m(key)("common", locale, codec)

  private[service] def m(key: String)(implicit prefix:String, locale:Locale, codec: Codec) =
    new String(messageSource.getMessage(s"$prefix.$key", null, locale).getBytes(),"UTF-8")

  private[service] def messageDefaults(implicit locale:Locale = Locale.getDefault) =
    MessageDefaults(c("greeting"), apiUrl, c("seeOnline"), c("sentByText"), c("disclaimer"), c("unsubscribeText"),
      c("unsubscribeCaption"), "unsubscribe", c("ensure"), c("copyright"))

  private[service] def messageData(implicit prefix:String, locale:Locale = Locale.getDefault) =
    MessageData(m("subject"), m("title"), m("procedure"), m("callToAction"), m("fallBack"), m("trailingInfo"))

  private[service] def createEntity(message: Message):HttpEntity[Message] = {
    val headers: HttpHeaders = new HttpHeaders
    headers.setContentType(MediaType.APPLICATION_JSON)
    new HttpEntity[Message](message, headers)
  }

  private[service] def createMessage(userToken: UserToken, entityName: Option[String], service: String, prefix: String): Message =
    createMessage(ContactData(userToken.getPrincipal, userToken.getFirstName, entityName.getOrElse("&nbsp;")), service, prefix)

  private[service] def createMessage(recipientData: ContactData, service: String, prefix: String): Message =
    new Message(mailerProperties.sender, recipientData, service, messageData(prefix), messageDefaults)

  def send(userToken: UserToken, service: String, prefix: String): Message =
    send(userToken, None, service, prefix)

  def send(userToken: UserToken, entityName: Option[String], service: String, prefix: String): Message =
    send(createMessage(userToken, entityName, service, prefix))

  def send(message: Message): Message = {
    logger.debug("Sending {}", message)
    logger.info("Props {}", mailerProperties)
    val uri = mailerProperties.mailerUri
    logger.debug("To {}", uri)
    Try(restTemplate.exchange(uri, HttpMethod.POST, createEntity(message), classOf[String])) match {
      case Success(responseEntity) =>
        val body = new String(responseEntity.getBody.getBytes("ISO-8859-1"),"UTF-8") //responseEntity.getBody
        logger.debug("Response body {}", body)
        messageLogRepository.saveAndFlush(new MessageLog(message, body, responseEntity.getStatusCode.value))
        message
      case Failure(e) =>
        logger.error("Unable to send message.", e)
        throw new IllegalArgumentException
    }
  }

}