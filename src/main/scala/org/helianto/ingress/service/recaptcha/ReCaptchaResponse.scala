package org.helianto.ingress.service.recaptcha

import java.util.Date

import scala.beans.BeanProperty

/**
  * Model reCaptcha response.
  */
class ReCaptchaResponse
( @BeanProperty val success: Boolean
  , @BeanProperty val challengeTs: Date
  , @BeanProperty val hostname: String
  , @BeanProperty val errorCodes: Array[String] = Array("")
)
