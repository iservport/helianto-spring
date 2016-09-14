package org.helianto.core.service

import org.helianto.core.domain.Entity
import org.helianto.core.repository.EntityRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.stereotype.Service

@Service
class EntityInstallService {

  val logger = LoggerFactory.getLogger(classOf[EntityInstallService])

  @Autowired
  val entityRepository: EntityRepository = null

  @Autowired
  val postInstaller: EntityPostInstallService = null

  @Value("#{helianto.contextName")
  val contextName: String = ""

  def installEntity(cityCode: String, alias: String, principal: String): Entity = {
    val entity = Option(entityRepository.findByContextNameAndAliasIgnoreCase(contextName, alias)) match {
      case Some(e) => e
      case None => {
        logger.info("New entity for  {}.", s"$contextName, $alias")
        entityRepository.saveAndFlush(new Entity(contextName, alias, cityCode))
      }
    }
    Option(postInstaller) match {
      case Some(p) => p.entityPostInstall(entity, principal)
      case None => entity
    }
  }

}
