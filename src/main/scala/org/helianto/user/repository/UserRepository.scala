package org.helianto.user.repository

import java.util.Date

import org.helianto.user.domain.User
import org.helianto.user.domain.enums.UserState
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}

trait UserRepository extends JpaRepository[User, String] {

  def findById(userId: String): UserProjection

  def findByIdAndIdentity_Id(userId: String, identityId: String): UserProjection

  def findByEntityIdAndUserKey(entityId: String, userKey: String): User

  def findByEntityIdAndIdentity_Id(entityId: String, identityId: Int): User

  def findByIdentity_IdOrderByLastEventDesc(identityId: String): java.util.List[UserProjection]

  def findByUserKeyOrderByLastEventDesc(userKey: String): java.util.List[UserProjection]

  @Query("select a_.parent.id from UserAssociation a_ where a_.child.id = ?1 ")
  def findParentsByChildId(childId: String): java.util.List[String]

  @Query("select c_.id as userId, e_.alias as alias, e_.entityDesc as entityDesc from User c_, UserAssociation a_, User p_, Entity e_ where a_.parent.id = p_.id AND a_.child.id = c_.id AND p_.entityId = e_.id AND lower(p_.userKey) = 'user' AND c_.identity.id = ?1 AND e_.activityState = ?2 AND c_.userState = ?3  order by c_.lastEvent DESC ")
  def findUserAccessByIdentityIdOrderByLastEventDesc(identityId: String, acitivtyState: Char, userState: UserState): java.util.List[UserAccessProjection]

  @Query("select c_.id as userId, e_.alias as alias, e_.entityDesc as entityDesc from User c_, UserAssociation a_, User p_, Entity e_ where a_.parent.id = p_.id AND a_.child.id = c_.id AND p_.entityId = e_.id AND lower(p_.userKey) = 'user' AND c_.identity.id = ?1 AND e_.activityState = 'A' AND c_.userState = 'ACTIVE' AND lower(e_.alias) like %?2 order by c_.lastEvent DESC ")
  def findUserAccessByIdentityIdOrderByLastEventDesc(identityId: String, searchText: String): java.util.List[UserAccessProjection]

  @Modifying @Query("update User u_ set u_.lastEvent = ?3 where u_.id = ?1 AND u_.identity.id = ?2 ")
  def setUserLastInfoByIdAndIdentity_Id(userId: String, identityId: String, lastEvent: Date): Unit
}

trait UserAccessProjection {

  def getUserId: String

  def getAlias: String

  def getEntityDesc: String

}

trait UserProjection {

  @Value("#{target.id}")
  def getUserId: String

  def getEntityId: String

  @Value("#{target.identity.id}")
  def getIdentityId: String

  def isAccountNonExpired: Boolean

  def getUserState: UserState

  @Value("#{target.identity.principal}")
  def getUserName: String

}
