package org.helianto.message.service

import org.helianto.ingress.domain.{Registration, UserToken}
import org.helianto.message.domain._
import org.helianto.message.repository.MessageLogRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.http.{HttpEntity, HttpHeaders, HttpMethod, MediaType}
import org.springframework.web.client.RestTemplate

import scala.util.{Failure, Success, Try}

/**
  * Notification base class.
  */
abstract class AbstractNotificationService {

  private[service] val logger: Logger = LoggerFactory.getLogger(classOf[AbstractNotificationService])

  val messageService: MessageService

  val messageLogRepository: MessageLogRepository

  val restTemplate: RestTemplate

  private[service] def createEntity(message: Message):HttpEntity[Message] = {
    val headers: HttpHeaders = new HttpHeaders
    headers.setContentType(MediaType.APPLICATION_JSON)
    new HttpEntity[Message](message, headers)
  }

  private[service] def createMessage(registration: Registration, service: String, prefix: String): Message =
    messageService.createMessage(
      ContactData(registration.getPrincipal, registration.getFirstName, registration.getEntityAlias), service, prefix, registration.getId)

  private[service] def createMessage(userToken: UserToken, entityName: Option[String], service: String, prefix: String): Message =
    messageService.createMessage(
      ContactData(userToken.getPrincipal, userToken.getFirstName, entityName.getOrElse("&nbsp;")), service, prefix, userToken.getToken)

  def send(registration: Registration, service: String, prefix: String): Message =
    send(createMessage(registration, service, prefix))

  def send(userToken: UserToken, service: String, prefix: String): Message =
    send(userToken, None, service, prefix)

  def send(userToken: UserToken, entityName: Option[String], service: String, prefix: String): Message =
    send(createMessage(userToken, entityName, service, prefix))

  def send(message: Message): Message = {
    logger.debug(s"Sending $message")
    Try(restTemplate.exchange(messageService.getMessageUri(), HttpMethod.POST, createEntity(message), classOf[String])) match {
      case Success(responseEntity) =>
        val body = new String(responseEntity.getBody.getBytes("ISO-8859-1"),"UTF-8")
        logger.debug("Response body {}", body)
        messageLogRepository.saveAndFlush(new MessageLog(message, body, responseEntity.getStatusCode.value))
        message
      case Failure(e) =>
        logger.error("Unable to send message.", e)
        throw new IllegalArgumentException
    }
  }

}