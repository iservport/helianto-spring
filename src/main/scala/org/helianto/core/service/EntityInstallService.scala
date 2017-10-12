package org.helianto.core.service

import org.helianto.core.domain.{Entity, EntityData, Identity, IdentityData}
import org.helianto.core.repository.EntityRepository
import org.helianto.core.utils.CommandMixin
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class EntityInstallService(val targetRepository: EntityRepository, val identityService: IdentityService) extends CommandMixin[Entity]{

  val logger = LoggerFactory.getLogger(classOf[EntityInstallService])

  @Value("${helianto.contextName}")
  val contextName: String = ""

  @Autowired(required = false)
  val postInstaller: EntityPostInstallService = null

  def findByContext() = targetRepository.findByContextName(contextName, new Sort(Sort.Direction.ASC, "alias"))

  def findOption(alias: String) = Option(targetRepository.findByContextNameAndAliasIgnoreCase(contextName, alias))

  def findById(id: String) = Option(targetRepository.findOne(id)).getOrElse(throw new IllegalArgumentException)

  def install(installData: EntityData): Entity =
    install(installData.getCityId
      , installData.getEntityAlias
      , installData.getEntityName
      , identityService.install(installData.asInstanceOf[IdentityData])
      , installData.getStateCode
      , installData.getEntityType
      , installData.getPun)

  def install(cityId: String, alias: String, entityName: String, identity: Identity, stateCode: String, entityType: Char, pun: String): Entity = {
    val entity = findOption(alias) match {
      case Some(e) => e.verify(contextName)
      case None => {
        logger.info(s"Installing entity: $contextName/$alias")
        targetRepository.saveAndFlush(new Entity(contextName, alias, entityName, cityId, stateCode, entityType, pun))
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

  def getNewTarget(command: Entity) =
    new Entity(contextName, command.getAlias, command.getEntityName, command.getCityId, command.getStateCode, command.getEntityType, command.getPun)

}
