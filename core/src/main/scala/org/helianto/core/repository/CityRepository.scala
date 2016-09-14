package org.helianto.core.repository

import org.helianto.core.domain.City
import org.springframework.data.jpa.repository.JpaRepository

trait CityRepository extends JpaRepository[City, Integer] {

  def findByContextNameAndCityCode(contextName: String, cityCode: String)

}
