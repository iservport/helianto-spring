package org.helianto.security.repository

import org.helianto.security.domain.UserAuthority
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.{JpaRepository, Query}

trait UserAuthorityRepository extends JpaRepository[UserAuthority, Integer] {

  @Query("select a_ from UserAuthority a_ " +
    "where a_.userId in (" +
    "  select select a_.parent.id from UserAssociation a_ where a_.child.id = ?1  " +
    ") order by a_.serviceCode ASC ")
  def findByUserIdOrderByServiceCodeAsc(childId: String): java.util.List[UserAuthority]

}
