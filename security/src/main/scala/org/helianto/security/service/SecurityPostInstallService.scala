package org.helianto.security.service

import org.helianto.core.domain.Identity
import org.helianto.core.service.IdentityPostInstallService
import org.helianto.security.domain.{Secret, UserAuthority}
import org.helianto.security.repository.{SecretRepository, UserAuthorityRepository}
import org.helianto.user.domain.User
import org.helianto.user.domain.enums.UserType
import org.helianto.user.repository.UserRepository
import org.helianto.user.service.UserPostInstallService
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import scala.util.{Success, Try}

@Service
class SecurityPostInstallService extends IdentityPostInstallService with UserPostInstallService {

  val logger: Logger = LoggerFactory.getLogger(classOf[SecurityPostInstallService])

  @Autowired val secretRepository: SecretRepository = null

  @Autowired val encoder: PasswordEncoder = null

  @Autowired val userRepository: UserRepository = null

  @Autowired val userAuthorityRepository: UserAuthorityRepository = null

  @Autowired val env: Environment = null

  override def identityPostInstall (identity: Identity) = {
    val secret = Option(secretRepository.findByIdentityId(identity.getId)) match {
      case Some(s) => s
      case None =>
        val secret = new Secret(identity, identity.getPrincipal)
        Try(env.getRequiredProperty("helianto.password.initial")) match {
          case Success(password) => secretRepository.saveAndFlush(secret.encode(encoder, password))
          case _ =>
            throw new IllegalArgumentException(s"Default password must be " +
              "supplied as a configuration property under the key helianto.password.initial")
        }
    }
    identity
  }

  override def userPostInstall(user: User) = {
    // TODO user post install
    user
  }

  override def systemPostInstall(group: User): User =
    Option(group) match {
      case Some(g) if g.getUserType==UserType.SYSTEM =>
        Option(userAuthorityRepository.findByUserIdAndServiceCode(group.getId, group.getUserKey)) match {
          case Some(authority) => group
          case _ =>
            Try(env.getRequiredProperty(s"helianto.root.system.${group.getUserKey}")) match {
              case Success(extension) =>
                val authority = new UserAuthority(group.getId, group.getUserKey, extension)
                logger.info(s"Installing mandatory system group authority $authority")
                userAuthorityRepository.saveAndFlush(authority)
                group
              case _=>
                throw new IllegalArgumentException(s"System group extension for ${group.getUserKey} must be " +
                  "supplied as a configuration property under the key helianto.root.system")
            }
        }
      case _ => throw new IllegalArgumentException(s"System group required")
    }

}
