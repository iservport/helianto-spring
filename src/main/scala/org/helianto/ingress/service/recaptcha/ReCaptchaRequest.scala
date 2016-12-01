package org.helianto.ingress.service.recaptcha

import scala.beans.BeanProperty

/**
  * Model reCaptcha request.
  */
class ReCaptchaRequest
( @BeanProperty val secret: String
, @BeanProperty val response: String
, @BeanProperty val remoteip: String = ""
)
