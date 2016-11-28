package org.helianto.core.service

import org.helianto.core.domain.City
import org.helianto.core.repository.CityRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CityInstallService(cityRepository: CityRepository) {

  val logger: Logger = LoggerFactory.getLogger(classOf[CityInstallService])

  @Value("${helianto.contextName}") val contextName: String = ""

  def list(stateCode: String) = cityRepository.findByContextNameAndStateCode(contextName, stateCode, new Sort("cityName"))

  def installCity(command: City): City =
    Option(cityRepository)
      .map(_.findByContextNameAndStateCodeAndCityCode(command.getContextName, command.getStateCode, command.getCityCode)) match {
      case Some(city) =>
        logger.debug(s"Not created, found city  $city ")
        city
      case None =>
        logger.debug(s"Installing city $command.")
        cityRepository.saveAndFlush(command.validateAll)
    }

}
