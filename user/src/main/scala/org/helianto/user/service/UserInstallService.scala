package org.helianto.user.service

import org.helianto.core.domain.Identity
import org.helianto.core.repository._
import org.helianto.core.service.EntityPostInstallService
import org.helianto.user.domain.{User, UserAssociation}
import org.helianto.user.domain.enums.{UserState, UserType}
import org.helianto.user.repository.{UserAssociationRepository, UserRepository}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserInstallService {

  val logger: Logger = LoggerFactory.getLogger(classOf[UserInstallService])

  @Autowired
  val identityRepository: IdentityRepository = null

  @Autowired
  val entityRepository: EntityRepository = null

  @Autowired
  val userRepository: UserRepository = null

  @Autowired
  val associationRepository: UserAssociationRepository = null

  @Autowired(required = false)
  val postInstaller: UserPostInstallService = null

  def installUser(entityId: String, identityId: String, userState: UserState): User = {
    Option(identityRepository.findOne(identityId)) match {
      case Some(i) => installUser(entityId, i, userState)
      case None => throw new IllegalArgumentException("Unable to install user: identity not found.")
    }
  }

  def installUser(entityId: String, identity: Identity, userState: UserState): User = {
    val user = Option(userRepository.findByEntityIdAndUserKey(entityId, identity.getPrincipal)) match {
      case Some(u) => u
      case None =>
        Option(entityRepository.findOne(entityId)) match {
          case Some(e) =>
            logger.info(s"Install user for entity $e and identity $identity.")
            userRepository.saveAndFlush(new User(entityId, UserType.INTERNAL, identity, userState))
          case None => throw new IllegalArgumentException("Unable to install user: entity not found.")
        }
    }
    Option(postInstaller) match {
      case Some(p) => p.userPostInstall(user)
      case None => user
    }
  }

  def associate(parent: User, child: User) = {
    Option(associationRepository.findByParentAndChild(parent, child)) match {
      case Some(a) => a
      case None => associationRepository.saveAndFlush(new UserAssociation(parent, child))
    }
  }

}