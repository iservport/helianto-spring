package org.helianto.core.service

import org.helianto.core.domain.Entity
import org.helianto.core.repository.EntityRepository

/**
  * Entity command side
  */
class EntityInstallServiceTests extends UnitSpec {

  import org.mockito.Mockito._

  "An entity command" should "should be saved as an entity" in {
    val service = new  EntityInstallService(mock[EntityRepository])
    val c = new Entity
    val t = new Entity
    when(service.targetRepository.findOne(c.getId))
      .thenReturn(t)
    service.saveOrUpdate(c)
  }

}
