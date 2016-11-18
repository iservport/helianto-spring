package org.helianto.ingress.repository

import org.helianto.ingress.domain.UserToken
import org.springframework.data.jpa.repository.JpaRepository

trait UserTokenRepository extends JpaRepository[UserToken, Integer] {

  def findByToken(token: String): UserToken

  def findByTokenSourceAndPrincipal(tokenSource: String, principal: String): UserToken

}
