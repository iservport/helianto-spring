package org.helianto.core.repository

import org.helianto.core.domain.State
import org.springframework.data.jpa.repository.JpaRepository

trait StateRepository extends JpaRepository[State, Integer] {

  def findByContextNameAndStateCode(contextName: String, stateCode: String)

}
