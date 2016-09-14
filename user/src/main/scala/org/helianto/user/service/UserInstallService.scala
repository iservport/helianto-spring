package org.helianto.user.service

import org.helianto.core.domain.Identity
import org.helianto.core.repository._
import org.helianto.user.domain.{User, UserAssociation}
import org.helianto.user.domain.enums.{UserState, UserType}
import org.helianto.user.repository.{UserAssociationRepository, UserRepository}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserInstallService {

  val logger: Logger = LoggerFactory.getLogger(classOf[UserInstallService])

  @Autowired private val identityRepository: IdentityRepository = null
  @Autowired private val entityRepository: EntityRepository = null
  @Autowired private val userRepository: UserRepository = null
  @Autowired private val associationRepository: UserAssociationRepository = null

//  def installUser(entity: Entity, principal: String): User = {
//    return installUser(entity, principal, 'A')
//  }
//
//  def installUser(entity: Entity, principal: String, userState: Character): User = {
//    val identity: Identity = identityInstallService.installIdentity(principal)
//    val userGroups: util.List[User] = installUserGroups(entity)
//    var user: User = null
//    import scala.collection.JavaConversions._
//    for (userGroup <- userGroups) {
//      if (user == null) {
//        user = installUser(userGroup.getEntity, identity, userState)
//      }
//      UserInstallService.logger.info("will find userAssociation to {} and {}.", user, userGroup)
//      var association: UserAssociation = userAssociationRepository.findByParentAndChild(userGroup, user)
//      UserInstallService.logger.info("userAssociation found  {} ", association)
//      if (association == null) {
//        UserInstallService.logger.info("userGroup.getUserType() {}  ", userGroup.getUserType)
//        if (userGroup.getUserType != null && userGroup.isSystemGroup) {
//          val adminUsers: util.List[UserAssociation] = userAssociationRepository.findByParent(userGroup)
//          if (adminUsers != null && adminUsers.size > 0) {
//          }
//          else {
//            UserInstallService.logger.info("ATENTION: a new user association was created between a SystemGroup {} and user {} ", userGroup, user)
//            association = userAssociationRepository.saveAndFlush(new UserAssociation(userGroup, user))
//          }
//        }
//        else {
//          UserInstallService.logger.info("Will associate user {} with entity {}.", user, userGroup.getEntity)
//          association = userAssociationRepository.saveAndFlush(new UserAssociation(userGroup, user))
//        }
//      }
//    }
//    UserInstallService.logger.info("finished associations to user {}.", user.getId)
//    return user
//  }

  def installUser(entityId: String, identityId: String, userState: UserState): User = {
    Option(identityRepository.findOne(identityId)) match {
      case Some(i) => installUser(entityId, i, userState)
      case None => throw new IllegalArgumentException("Unable to install user: identity not found.")
    }
  }

  def installUser(entityId: String, identity: Identity, userState: UserState): User = {
    Option(userRepository.findByEntityIdAndUserKey(entityId, identity.getPrincipal)) match {
      case Some(u) => u
      case None =>
        Option(entityRepository.findOne(entityId)) match {
          case Some(e) =>
            logger.info(s"Install user for entity $e and identity $identity.")
            userRepository.saveAndFlush(new User(entityId, UserType.INTERNAL, identity, userState))
          case None => throw new IllegalArgumentException("Unable to install user: entity not found.")
        }
    }
  }

  def associate(parent: User, child: User) = {
    Option(associationRepository.findByParentAndChild(parent, child)) match {
      case Some(a) => a
      case None => associationRepository.saveAndFlush(new UserAssociation(parent, child))
    }
  }

}