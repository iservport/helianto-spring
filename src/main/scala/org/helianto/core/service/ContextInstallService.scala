package org.helianto.core.service

import org.helianto.core.domain.Context
import org.helianto.core.repository.ContextRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ContextInstallService(contextRepository: ContextRepository) {

  val logger: Logger = LoggerFactory.getLogger(classOf[ContextInstallService])

  @Value("${helianto.contextName:DEFAULT}")
  val defaultContextName: String = ""

  def getDefaultContext(): Context = getContext(defaultContextName)

  def getContext(contextName: String): Context =
    Option(contextRepository).map(_.findByContextName(contextName)) match {
      case Some(context) => context
      case None =>
        logger.warn(s"No context found with name $contextName, returning empty.")
        new Context("")
    }

  def installDefaultContext(): Context =
    Option(contextRepository).map(_.findByContextName(defaultContextName)) match {
      case Some(context) => context
      case None =>
        logger.info(s"Installed $defaultContextName.")
        contextRepository.saveAndFlush(new Context(defaultContextName))
    }

}
