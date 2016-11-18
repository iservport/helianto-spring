package org.helianto.message.config

import scala.beans.BeanProperty

/**
  * Service Properties
  */
class ServiceProperties {

  @BeanProperty var serviceType: String = ""

  @BeanProperty var serviceName: String = ""

  @BeanProperty var user: String = ""

  @BeanProperty var password: String = ""

  override def toString = s"MailerService($serviceType, $serviceName, $user, $password)"

}
