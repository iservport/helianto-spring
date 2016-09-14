package org.helianto.user.repository

import org.helianto.user.domain.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.{JpaRepository, Query}

trait UserRepository extends JpaRepository[User, String] {

  def findByEntityIdAndUserKey(entityId: String, userKey: String): User

  def findByEntityAliasAndIdentity_Id(entityAlias: String, identityId: Int): User

  def findByIdentity_IdOrderByLastEventDesc(identityId: String): java.util.List[UserProjection]

  @Query("select a_.parent.id from UserAssociation a_ where a_.child.id = ?1 ")
  def findParentsByChildId(childId: String): java.util.List[String]

}

trait UserProjection {

  def getUserId: String

  def getEntityId: String

  def getIdentityId: String

  def isAccountNonExpired: Boolean

  @Value("#{(target.userState=='A' ? true: false)}")
  def isAccountNonLocked: Boolean

  def getUserName: String


}
