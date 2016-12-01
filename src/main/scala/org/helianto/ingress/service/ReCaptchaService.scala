package org.helianto.ingress.service

import org.helianto.ingress.domain.ReCaptcha
import org.helianto.ingress.service.recaptcha.ReCaptchaRequest
import org.helianto.message.domain.{Message, MessageLog}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.http.{HttpEntity, HttpHeaders, HttpMethod, MediaType}
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import scala.util.{Failure, Success, Try}

@Service
class ReCaptchaService(val restTemplate: RestTemplate) {

  private[service] val logger: Logger = LoggerFactory.getLogger(classOf[ReCaptchaService])

  val api = "https://www.google.com/recaptcha/api/siteverify"

  val reCaptchaSecret = ""

  def verify(response: ReCaptcha) = {
    logger.debug(s"Verifying reCaptcha $response")
    Try(restTemplate.exchange(api, HttpMethod.POST, createEntity(response), classOf[String])) match {
      case Success(responseEntity) =>
        val body = new String(responseEntity.getBody.getBytes("ISO-8859-1"),"UTF-8")
        logger.debug("Response body {}", body)
        // TODO verify
      case Failure(e) =>
        logger.error("Unable to verify captcha.", e)
        throw new IllegalArgumentException
    }
  }

  private[service] def createEntity(response: ReCaptcha) = {
    val headers: HttpHeaders = new HttpHeaders
    headers.setContentType(MediaType.APPLICATION_JSON)
    new HttpEntity[ReCaptchaRequest](new ReCaptchaRequest(reCaptchaSecret, response.getgRecaptchaResponse), headers)
  }

}
