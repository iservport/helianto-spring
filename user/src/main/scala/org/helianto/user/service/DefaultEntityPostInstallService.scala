package org.helianto.user.service

import org.helianto.core.domain.{Entity, Identity}
import org.helianto.core.service.EntityPostInstallService
import org.helianto.user.domain.User
import org.helianto.user.domain.enums.{UserState, UserType}
import org.helianto.user.repository.UserRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DefaultEntityPostInstallService extends EntityPostInstallService {

  val logger: Logger = LoggerFactory.getLogger(classOf[DefaultEntityPostInstallService])

  @Autowired val userRepository: UserRepository = null

  @Autowired val userInstallService: UserInstallService = null

  override def entityPostInstall(entity: Entity, identity: Identity) = {
    Option(identity) match {
      case Some(manager) =>
        val adminGroup = installSystemGroup(entity.getId, "ADMIN", manager)
        logger.info(s"Installed $adminGroup.")
        val userGroup = installSystemGroup(entity.getId, "USER", manager)
        logger.info(s"Installed $userGroup.")
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
    Option(userRepository.findByEntityIdAndUserKey(entityId, userKey)) match {
      case Some(u) => u
      case None => userRepository.saveAndFlush(new User(entityId, UserType.SYSTEM, normalizedKey, s"SYSTEM_$normalizedKey"))
    }
  }

  def assignManager(userGroup: User, manager: Identity): User = {
    Option(userInstallService.installUser(userGroup.getEntityId, manager, UserState.ACTIVE)) match {
      case Some(u) => userInstallService.associate(userGroup, u).getParent
      case None =>
        throw new IllegalArgumentException(s"Unable to associate manager: user not valid for $manager")
    }
  }

}
