package org.helianto.core.repository

import org.helianto.core.domain.Identity
import org.springframework.data.jpa.repository.JpaRepository

trait IdentityRepository extends JpaRepository[Identity, String] {

  def findByPrincipal(principal: String): Identity

}
