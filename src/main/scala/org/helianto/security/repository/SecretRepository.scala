package org.helianto.security.repository

import org.helianto.security.domain.Secret
import org.springframework.data.jpa.repository.{JpaRepository, Query}

trait SecretRepository extends JpaRepository[Secret, Integer] {

  def findByIdentityKey(identityKey: String): Secret

  @Query("select s_ from Secret s_ where s_.identity.principal = ?1 or s_.identity.email = ?1 ")
  def findByIdentityKeyOrEmail(principal: String): Secret

}