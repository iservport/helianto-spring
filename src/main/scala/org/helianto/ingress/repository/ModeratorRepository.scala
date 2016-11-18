package org.helianto.ingress.repository

import org.helianto.ingress.domain.Moderator
import org.springframework.data.jpa.repository.JpaRepository

trait ModeratorRepository extends JpaRepository[Moderator, String] {

  def findByUserId(token: String): Moderator

}
