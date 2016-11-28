package org.helianto.core.service

import org.helianto.core.domain.State
import org.helianto.core.repository.StateRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class StateInstallService(stateRepository: StateRepository) {

  val logger: Logger = LoggerFactory.getLogger(classOf[StateInstallService])

  @Value("${helianto.contextName}") val contextName: String = ""

  def list() = stateRepository.findByContextName(contextName, new Sort("stateAlias"))

  def installState(command: State): State =
    Option(stateRepository)
      .map(_.findByContextNameAndCountryCodeAndStateCode(command.getContextName, command.getCountryCode, command.getStateCode)) match {
      case Some(state) =>
        logger.debug(s"Not created, found state $state ")
        state
      case None =>
        logger.debug(s"Installing city $command ")
        stateRepository.saveAndFlush(command.validateAll())
    }

}
