package org.helianto.core.repository

import java.util.Date

import org.helianto.core.domain.State
import org.springframework.data.jpa.repository.JpaRepository

trait StateRepository extends JpaRepository[State, Integer] {

  def findByContextNameAndStateCode(contextName: String, stateCode: String)

  def findByInstallDateIsLessThanEqual(installDate: Date): java.util.List[State]

}
