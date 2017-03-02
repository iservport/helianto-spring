package org.helianto.ingress.repository

import org.helianto.ingress.domain.Invite
import org.helianto.user.domain.User
import org.springframework.data.jpa.repository.{JpaRepository, Query}

trait InviteRepository extends JpaRepository[Invite, String] {

  @Query("select distinct u_ from Invite i_, User u_ where i_.userId = u_.id and i_.userId = ?1 ")
  def findInvitedUserByUserId(userId: String): User

}
