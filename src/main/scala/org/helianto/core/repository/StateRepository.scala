package org.helianto.core.repository

import java.util.Date

import org.helianto.core.domain.State
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

trait StateRepository extends JpaRepository[State, Integer] {

  /**
    * @deprecated
    */
  def findByContextNameAndStateCode(contextName: String, stateCode: String): State

  def findByContextName(contextName: String, sort: Sort): java.util.List[State]

  def findByContextNameAndCountryCodeAndStateCode(contextName: String, countryCode: String, stateCode: String): State

  def findByInstallDateIsLessThanEqual(installDate: Date): java.util.List[State]

}
