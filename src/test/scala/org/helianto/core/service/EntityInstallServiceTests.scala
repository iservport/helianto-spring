package org.helianto.core.service

import org.helianto.UnitSpec
import org.helianto.core.domain.Entity
import org.helianto.core.repository.{EntityRepository, IdentityRepository}

/**
  * Each entity must refer to a city and have a manager identity
  */
class EntitySaveOrUpdateTests extends UnitSpec {

  import org.mockito.Mockito._

  val service = new EntityInstallService(mock[EntityRepository], mock[IdentityRepository]) {
    override val contextName: String = "CONTEXT"
  }

  "An entity command" should "should be saved as an entity" in {
    val c = new Entity("CONTEXT", "A", "B")
    val t = new Entity("CONTEXT", "A", "B")
    when(service.targetRepository.findOne(c.getId))
      .thenReturn(t)
    when(service.targetRepository.saveAndFlush(t))
      .thenReturn(t)
    val result = service.saveOrUpdate(c)
    assert(t.equals(result))
  }

  it should "have a context name" in {
    val c = new Entity("CONTEXT", "", "")
    when(service.targetRepository.findOne(c.getId))
      .thenReturn(new Entity)
    service.saveOrUpdate(new Entity)
    intercept[IllegalArgumentException] { service.saveOrUpdate(c) }
  }

}

/**
  * Each entity must refer to a city and have a manager identity
  */
class EntityInstallTests extends UnitSpec {

}