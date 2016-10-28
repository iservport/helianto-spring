package org.helianto.core.service

import org.helianto.core.domain.{Entity, Identity}
import org.helianto.core.repository.IdentityRepository

/**
  * Idempotent identity command side.
  */
class IdentityInstallServiceTests extends UnitSpec {

  import org.mockito.Mockito._

  "An identity command" should "should be saved as an identity" in {
    val service = new  IdentityInstallService(mock[IdentityRepository])
    val c = new Identity
    val t = new Identity
    when(service.targetRepository.findByPrincipal("principal"))
      .thenReturn(t)
    service.saveOrUpdate(c)
  }

}
