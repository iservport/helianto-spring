package org.helianto.ingress.service

import java.util.Locale

import org.helianto.UnitSpec
import org.helianto.ingress.domain.UserToken
import org.helianto.message.service.SecurityNotificationService
import org.helianto.security.service.SecurityService
import org.springframework.ui.{ExtendedModelMap, Model}

/**
  * Idempotent identity command side.
  */
class PasswordServiceTests extends UnitSpec {

  import org.mockito.Mockito._

  val locale = Locale.getDefault

  val responseService = new ResponseService(null, null) {
    override def response(model: Model, locale: Locale) = "frame-security"
    }

  def newService = new  PasswordService(mock[UserTokenService], mock[SecurityService], mock[SecurityNotificationService]
    , responseService)

  "A password recovery page" should "be presented to the user" in {
    val service = newService
    val model = new ExtendedModelMap()
    val error = "1"
    service.getRecovery(error, model, locale) shouldBe "frame-security"
    model.asMap.get("main") shouldBe "security/password-recovery.html"
  }

  "Recovery page submit" should "generate a token and notification to the user" in {
    val service = newService
    val model = new ExtendedModelMap()
    val userToken = new UserToken("PASSWORD_RECOVERY", "principal").appendFirstName("displayName")
    when(service.userTokenService.createOrRefreshToken("principal", "PASSWORD_RECOVERY"))
      .thenReturn(userToken)
    service.doRecover("principal", model, locale) shouldBe "frame-security"
    model.asMap.get("main") shouldBe "security/read-your-email.html"
  }

}
