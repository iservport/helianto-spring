package org.helianto.security.service

import org.helianto.security.domain.UserDetailsAdapter
import org.helianto.security.repository.SecretRepository
import org.helianto.user.repository.UserProjection
import org.helianto.user.repository.UserRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.{UserDetails, UsernameNotFoundException}

class AbstractDetailsService {

  @Autowired val secretRepository: SecretRepository = null
  @Autowired val userRepository: UserRepository = null

  val logger: Logger = LoggerFactory.getLogger(classOf[AbstractDetailsService])

  protected def loadIdentitySecretByKey(identityKey: String) =
    Option(secretRepository.findByIdentityKeyOrEmail(identityKey)) match {
      case Some(s) => s
      case None => error(identityKey)
    }

  protected def loadLastUserByIdentityId(identityId: String): UserProjection =
    Option(userRepository.findByIdentity_IdOrderByLastEventDesc(identityId)) match {
      case Some(list) if !list.isEmpty => list.get(0)
      case _ => error(identityId)
    }

  def updateLastEvent(userDetails: UserDetailsAdapter): Unit = {
    Option(userRepository.findOne(userDetails.getUserId)) match {
      case Some(u) =>
        userRepository.saveAndFlush(u.updateLastEvent())
      case _ => error(userDetails.getUserId)
    }
  }

  protected def error(source: String) = {
    logger.error("Unable to load by user name with {}.", source)
    throw new UsernameNotFoundException("Unable to find user name for " + source)
  }

}