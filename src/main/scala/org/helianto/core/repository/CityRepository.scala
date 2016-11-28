package org.helianto.core.repository

import org.helianto.core.domain.City
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

trait CityRepository extends JpaRepository[City, String] {

  def findByContextNameAndStateCodeAndCityCode(contextName: String, stateCode: String, cityCode: String): City

  def findByStateId(stateId: String, sort: Sort): java.util.List[City]

  def findByContextNameAndStateCode(contextName: String, stateCode: String, sort: Sort): java.util.List[City]

}
