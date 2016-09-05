package org.helianto.security.repository

import org.helianto.security.domain.UserAuthority
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.{JpaRepository, Query}

trait UserAuthorityRepository extends JpaRepository[UserAuthority, Integer] {

  @Query("select a_ from UserAuthority a_ where a_.userGroup in ?1 "
    + "order by a_.serviceCode ASC ")
  def findByUserGroupIdOrderByServiceCodeAsc(userGroups: java.util.List[Integer]): java.util.List[UserAuthorityProjection]

}


trait UserAuthorityProjection {

  def getId: Int

  @Value("#{target.userGroup.id}")
  def getUserGroupId: Int

  def getServiceCode: String

  def getServiceExtension: String

  @Value("#{target.userGroup.userName}")
  def getUserName: String

}