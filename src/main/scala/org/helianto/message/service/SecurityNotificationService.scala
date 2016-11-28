package org.helianto.message.service

import org.helianto.ingress.domain.{Registration, UserToken}
import org.helianto.message.repository.MessageLogRepository
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
 , val messageService: MessageService
 , val restTemplate: RestTemplate
) extends AbstractNotificationService {

  def sendSignUpUser(registration: Registration) = send(registration, "/register/user", "register")

  def sendSignUpAdmin(registration: Registration) = send(registration, "/register/admin", "register")

  def sendSubscription(userToken: UserToken) = send(userToken, "/subscribe", "subscribe")

  def sendConfirmation(userToken: UserToken) = send(userToken, "/confirm", "confirm")

  def sendWelcome(userToken: UserToken) = send(userToken, "/login", "welcome")

  def sendRecovery(userToken: UserToken) = send(userToken, "/password/recovery", "recovery")

  def sendChange(userToken: UserToken) = send(userToken, "/password/change", "change")

}
