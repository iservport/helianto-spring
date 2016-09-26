package org.helianto.user.service

import org.helianto.core.domain.{Entity, Identity}
import org.helianto.core.service.EntityPostInstallService
import org.helianto.user.domain.{User, UserAssociation}
import org.helianto.user.domain.enums.{UserState, UserType}
import org.helianto.user.repository.UserRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DefaultEntityPostInstallService extends EntityPostInstallService {

  val logger: Logger = LoggerFactory.getLogger(classOf[DefaultEntityPostInstallService])

  @Autowired
  val userRepository: UserRepository = null

  @Autowired
  val userInstallService: UserInstallService = null

  @Autowired(required = false)
  val postInstaller: UserPostInstallService = null

  override def entityPostInstall(entity: Entity, identity: Identity) = {
    Option(identity) match {
      case Some(i) =>
        val adminGroup = installSystemGroup(entity.getId, "ADMIN", i)
        logger.info(s"Installed $adminGroup.")
        val manager = assignManager(adminGroup, i).getChild
        logger.info(s"Installed $manager.")
        val userGroup = installSystemGroup(entity.getId, "USER", i)
        logger.info(s"Installed $userGroup.")
        userInstallService.associate(userGroup, manager)
      case None =>
        throw new IllegalArgumentException(s"Unable to create system groups: manager not found with $identity.")
    }
    entity
  }

  def installSystemGroup(entityId: String, userKey: String, manager: Identity) = {
    val normalizedKey = Option(userKey) match {
      case Some(k) => k.toUpperCase
      case None =>
        throw new IllegalArgumentException("Unable to create system group: null key.")
    }
    val systemGroup = Option(userRepository.findByEntityIdAndUserKey(entityId, userKey)) match {
      case Some(u) => u
      case None => userRepository.saveAndFlush(new User(entityId, UserType.SYSTEM, normalizedKey, s"SYSTEM_$normalizedKey"))
    }
    Option(postInstaller) match {
      case Some(p) => p.systemPostInstall(systemGroup)
      case _ => systemGroup
    }
  }

  def assignManager(userGroup: User, manager: Identity): UserAssociation = {
    Option(userInstallService.installUser(userGroup.getEntityId, manager, UserState.ACTIVE)) match {
      case Some(u) => userInstallService.associate(userGroup, u)
      case None =>
        throw new IllegalArgumentException(s"Unable to associate manager: user not valid for $manager")
    }
  }

}
