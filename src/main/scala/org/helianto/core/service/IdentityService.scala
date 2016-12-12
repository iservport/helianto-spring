package org.helianto.core.service

import org.helianto.core.domain.{Identity, IdentityData, PersonalData}
import org.helianto.core.repository.IdentityRepository
import org.helianto.core.utils.CommandMixin
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

import scala.util.{Success, Try}

@Service
class IdentityService(val targetRepository: IdentityRepository) extends CommandMixin[Identity] with IdentityInstaller {

  val logger = LoggerFactory.getLogger(classOf[IdentityService])

  @Autowired(required = false)
  val postInstaller: IdentityPostInstallService = null

  @Autowired val env: Environment = null

  def findOption(principal: String):Option[Identity] = Option(targetRepository.findByPrincipal(principal))

  def findById(id: String):Identity = Option(targetRepository.findOne(id)).getOrElse(throw new IllegalArgumentException)

  def install(root: IdentityData): Identity = {
    val password = Option(root.getPassword).filter(_.nonEmpty).getOrElse(getInitialPassword)
    install(root.getPrincipal, root.getDisplayName, root.getPersonalData, password)
  }

  def getInitialPassword =
    Try(env.getRequiredProperty("helianto.password.initial")) match {
      case Success(p) => p
      case _ =>
        throw new IllegalArgumentException("Default password must be " +
          "supplied as a configuration property under the key helianto.password.initial")
    }

  def install(principal: String, displayName: String, personalData: PersonalData, password: String): Identity = {
    val identity = findOption(principal) match {
      case Some (i) => i
      case _ =>
        logger.info(s"Installing identity: $displayName <$principal>")
        targetRepository.saveAndFlush(new Identity(principal, displayName, personalData))
    }
    Option(postInstaller) match {
      case Some(p) => p.identityPostInstall(identity, password)
      case None => identity
    }
  }

  override protected def getNewTarget(command: Identity): Identity =
    new Identity(command.getPrincipal, command.getDisplayName, command.getPersonalData)

}

trait IdentityInstaller {

  def install(root: IdentityData): Identity

  def install(principal: String, displayName: String, personalData: PersonalData, password: String): Identity

}
