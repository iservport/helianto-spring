package org.helianto.core.service

import org.helianto.core.domain.{Entity, Identity}
import org.helianto.core.repository.EntityRepository
import org.helianto.core.utils.CommandMixin
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class EntityInstallService(val targetRepository: EntityRepository) extends CommandMixin[Entity]{

  val logger = LoggerFactory.getLogger(classOf[EntityInstallService])

  @Value("${helianto.contextName}")
  val contextName: String = ""

  @Autowired(required = false)
  val postInstaller: EntityPostInstallService = null

  def install(cityCode: String, alias: String, identity: Identity): Entity = {
    val entity = Option(targetRepository.findByContextNameAndAliasIgnoreCase(contextName, alias)) match {
      case Some(e) => e
      case None => {
        logger.info(s"Installing entity: $contextName/$alias")
        targetRepository.saveAndFlush(new Entity(contextName, alias, cityCode))
      }
    }
    Option(postInstaller) match {
      case Some(p) =>
        Option(identity) match {
          case Some(i) => p.entityPostInstall(entity, i)
          case None => throw new IllegalArgumentException
      }
      case None => entity
    }
  }

  override def validateTarget(target: Entity, command: Entity) = (target.getContextName equals contextName)

  def getNewTarget(command: Entity) = new Entity(contextName, command.getAlias, command.getCityCode)

}
