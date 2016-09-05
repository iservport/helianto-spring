package org.helianto.user.repository

import org.helianto.user.domain.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.{JpaRepository, Query}

trait UserRepository extends JpaRepository[User, Integer] {

  def findByIdentity_IdOrderByLastEventDesc(identityId: Int): java.util.List[UserProjection]

  @Query("select association.parent.id from UserAssociation association " +
    "where association.child.id = ?1 ")
  def findParentsByChildId(childId: Int): java.util.List[Integer]

}

trait UserProjection {

  def getUserId: Int

  def getContextId: Int

  def getEntityId: Int

  def getIdentityId: Int

  def isAccountNonExpired: Boolean

  @Value("#{(target.userState=='A' ? true: false)}")
  def isAccountNonLocked: Boolean

  def getUserName: String


}
