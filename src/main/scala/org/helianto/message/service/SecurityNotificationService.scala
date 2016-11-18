package org.helianto.message.service

import org.helianto.ingress.domain.UserToken
import org.helianto.message.config.MailerProperties
import org.helianto.message.repository.MessageLogRepository
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
  * Security notification service.
  *
  * @author mauriciofernandesdecastro
  */
@Service
class SecurityNotificationService
(val messageLogRepository: MessageLogRepository
 , val restTemplate: RestTemplate
 , val mailerProperties: MailerProperties
 , val messageSource: MessageSource
) extends AbstractNotificationService {

  def sendSignUp(userToken: UserToken) = send(userToken, "/register", "register")

  def sendSubscription(userToken: UserToken) = send(userToken, "/subscribe", "subscribe")

  def sendConfirmation(userToken: UserToken) = send(userToken, "/confirm", "confirm")

  def sendWelcome(userToken: UserToken) = send(userToken, "/login", "welcome")

  def sendRecovery(userToken: UserToken) = send(userToken, "/password/recovery", "recovery")

  def sendChange(userToken: UserToken) = send(userToken, "/password/change", "change")

}
