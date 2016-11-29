package org.helianto.user.service

import org.helianto.core.domain.{Entity, Identity}
import org.helianto.core.service.EntityPostInstallService
import org.helianto.user.domain.{User, UserAssociation}
import org.helianto.user.domain.enums.{UserState, UserType}
import org.helianto.user.repository.UserRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * Called after entity installation.
  *
  * <p>Performs 4 post-installation tasks:</p>
  * <ol>
  *   <li>Installs a system group with name ADMIN, delegating any user post-installation action to the appropriate bean,
  *   if available.</li>
  *   <li>Associates a manager to the ADMIN group.</li>
  *   <li>Installs a system group with name USER, delegating any user post-installation action to the appropriate bean,
  *   if available.</li>
  *   <li>Associates the manager also to the USER group.</li>
  * </ol>
  */
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

  /**
    * Installs system groups.
    *
    * @param entityId
    * @param userKey
    * @param manager
    * @return the intalled system group.
    */
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

  /**
    * Associates a manager to a group.
    *
    * @param userGroup the group
    * @param manager the manager identity
    * @return the user association
    */
  def assignManager(userGroup: User, manager: Identity): UserAssociation = {
    Option(userInstallService.installUser(userGroup.getEntityId, manager, UserState.ACTIVE)) match {
      case Some(u) => userInstallService.associate(userGroup, u)
      case None =>
        throw new IllegalArgumentException(s"Unable to associate manager: user not valid for $manager")
    }
  }

}
