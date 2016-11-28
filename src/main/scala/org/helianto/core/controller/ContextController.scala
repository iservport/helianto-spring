package org.helianto.core.controller

import org.helianto.core.domain.{City, State}
import org.helianto.core.service.{CityInstallService, ContextInstallService, StateInstallService}
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation._

/**
  * Controller for context related entities, like the context itself, states and cities.
  */
@RestController
@RequestMapping(Array("/api/context"))
class ContextController
(service: ContextInstallService
 , stateService: StateInstallService
 , cityService: CityInstallService) {

  /**
    * GET /api/context
    *
    * @return an empty (non-persisted) context, if not exists, or the default context
    */
  @GetMapping
  def getContext() = service.getDefaultContext()

  /**
    * GET /api/context/:contextName
    *
    * @param contextName the context name
    *
    * @return an empty (non-persisted) context, if not exists, or the existing context
    */
  @GetMapping(path = Array("/{contextName}"))
  def getContext(@PathVariable contextName: String) = service.getContext(contextName)

  /**
    * PUT /api/context/
    *
    * @return the new default context, if not exists, or the existing context
    */
  @PutMapping
  def postContext() = service.installDefaultContext()

  /**
    * POST /api/context/state
    *
    * @param command the state request
    *
    * @return a new persisted command, if not exists, or the existing command
    */
  @PostMapping(value = Array("/state"))
  def postState(@RequestBody command: State, error: BindingResult): State = stateService.installState(command)

  /**
    * GET /api/context/state
    *
    * @return a list of states
    */
  @GetMapping(value = Array("/state"))
  def getStates() = stateService.list()

  /**
    * POST /api/context/city/
    *
    * @param command the city request
    *
    * @return a new persisted city, if not exists, or the existing city
    */
  @PostMapping(path = Array("/city"))
  def postCity(@RequestBody command: City, error: BindingResult) = cityService.installCity(command)

  /**
    * GET /api/context/city
    *
    * @param stateCode the stateCode
    *
    * @return a list of cities in the default context
    */
  @GetMapping(value = Array("/city/{stateCode}"))
  def getCities(@PathVariable stateCode: String) = cityService.list(stateCode)

}
