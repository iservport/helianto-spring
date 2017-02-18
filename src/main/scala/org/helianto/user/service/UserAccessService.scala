package org.helianto.user.service

import java.util

import org.helianto.user.domain.enums.UserState
import org.helianto.user.repository.{UserAccessProjection, UserRepository}
import org.springframework.stereotype.Service

@Service
class UserAccessService (repository: UserRepository) {

  def userAccess(identityId: String) =
    Option(repository.findUserAccessByIdentityIdOrderByLastEventDesc(identityId, 'A', UserState.ACTIVE)) match {
      case Some(list) => list
      case None => new util.ArrayList[UserAccessProjection]()
    }

  def userAccess(identityId: String, alias: String) =
    Option(repository.findUserAccessByIdentityIdOrderByLastEventDesc(identityId, alias)) match {
      case Some(list) => list
      case None => new util.ArrayList[UserAccessProjection]()
    }

}
