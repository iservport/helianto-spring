package org.helianto.message.service

import org.helianto.message.config.{MailerProperties, SmsProperties}
import org.helianto.message.domain.{Message, MessageLog, TotalVoiceSmsMessage}
import org.helianto.message.repository.MessageLogRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.http.{HttpEntity, HttpHeaders, HttpMethod, MediaType}
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import scala.util.{Failure, Success, Try}

@Service
class TotalVoiceService
(val restTemplate: RestTemplate
 , val smsProperties: SmsProperties
 , val messageLogRepository: MessageLogRepository) {

  private[service] val logger: Logger = LoggerFactory.getLogger(classOf[TotalVoiceService])

  private[service] def createEntity(message: TotalVoiceSmsMessage):HttpEntity[TotalVoiceSmsMessage] = {
    val headers: HttpHeaders = new HttpHeaders
    headers.setContentType(MediaType.APPLICATION_JSON)
    headers.set("Access-Token", smsProperties.accessToken)
    new HttpEntity[TotalVoiceSmsMessage](message, headers)
  }


  // TODO message log
  def send(message: TotalVoiceSmsMessage) = {
    logger.debug("Sending {}", message)
    logger.info("Props {}", smsProperties)
    val uri = smsProperties.serviceUri
    logger.debug("To {}", uri)
    Try(restTemplate.exchange(uri, HttpMethod.POST, createEntity(message), classOf[String])) match {
      case Success(responseEntity) =>
        val body = new String(responseEntity.getBody.getBytes("ISO-8859-1"),"UTF-8") //responseEntity.getBody
        logger.debug("Response body {}", body)
//        messageLogRepository.saveAndFlush(new MessageLog(message, body, responseEntity.getStatusCode.value))
        message
      case Failure(e) =>
        logger.error("Unable to send message.", e)
        throw new IllegalArgumentException
    }
  }
}
