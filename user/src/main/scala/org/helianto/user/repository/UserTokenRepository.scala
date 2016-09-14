package org.helianto.user.repository

import org.helianto.user.domain.UserToken
import org.springframework.data.jpa.repository.JpaRepository

trait UserTokenRepository extends JpaRepository[UserToken, Integer] {

  def findByToken(token: String): UserToken

  def findByTokenSourceAndPrincipal(tokenSource: String, principal: String): UserToken

}
