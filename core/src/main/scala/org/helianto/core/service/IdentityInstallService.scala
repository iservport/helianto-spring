package org.helianto.core.service

import org.helianto.core.domain.{Identity, IdentityData, PersonalData}
import org.helianto.core.repository.IdentityRepository
import org.helianto.core.utils.CommandMixin
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class IdentityInstallService (val targetRepository: IdentityRepository) extends CommandMixin[Identity] with IdentityInstaller {

  val logger = LoggerFactory.getLogger(classOf[IdentityInstallService])

  def install(root: IdentityData): Identity = {
    install(root.getPrincipal, root.getDisplayName, root.getPersonalData)
  }

  def install(principal: String, displayName: String, personalData: PersonalData): Identity = {
    Option(targetRepository.findByPrincipal(principal)) match {
      case Some (i) => i
      case _ =>
        logger.info(s"Installing identity: $displayName <$principal>")
        targetRepository.saveAndFlush(new Identity(principal, displayName, personalData))
    }
  }

  override protected def getNewTarget(command: Identity): Identity =
    new Identity(command.getPrincipal, command.getDisplayName, command.getPersonalData)

}

trait IdentityInstaller {

  def install(root: IdentityData): Identity

  def install(principal: String, displayName: String, personalData: PersonalData): Identity

}
