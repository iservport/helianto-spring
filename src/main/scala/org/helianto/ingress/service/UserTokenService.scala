package org.helianto.ingress.service

import java.util.Date

import org.helianto.core.domain.Identity
import org.helianto.core.service.IdentityService
import org.helianto.ingress.domain.UserToken
import org.helianto.ingress.repository.UserTokenRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * User token query service.
  *
  * @author mauriciofernandesdecastro
  */
@Service
class UserTokenService {

  val logger: Logger = LoggerFactory.getLogger(classOf[UserTokenService])

  @Autowired val identityService: IdentityService = null

  @Autowired val userTokenRepository: UserTokenRepository = null

  // TODO if principal is not in identity
  def install(principal: String, tokenSource:String) = {
    identityService.findOption(principal) match {
      case Some(identity) =>
        Option(userTokenRepository.findByTokenSourceAndPrincipal(tokenSource, principal)) match {
          case Some(userToken) => userToken
          case None => throw new IllegalArgumentException("Unable to install token: registration must have valid e-mail.") //install(identity, tokenSource)
        }
      case _ => throw new IllegalArgumentException("Unable to install token: registration must have valid e-mail.")
    }
  }

  /**
    * Create or refresh token.
    *
    * @param principal
    */
  def createOrRefreshToken(principal: String, tokenSource:String): UserToken = {
    identityService.findOption(principal) match {
      case Some(identity) =>
        Option(userTokenRepository.findByTokenSourceAndPrincipal(tokenSource, principal)) match {
          case Some(userToken) =>
            userToken.setIssueDate(new Date())
            userTokenRepository.saveAndFlush(userToken)
          case _ =>
            userTokenRepository.saveAndFlush(new UserToken(tokenSource, principal).appendFirstName(identity.getDisplayName))
        }
      case None => new UserToken
    }
  }

  /**
    * Exchange a token by its corresponding identity.
    *
    * @param confirmationToken
    */
  def getIdentityOption(confirmationToken: String): Option[Identity] = {
    Option(userTokenRepository.findByToken(confirmationToken)) match {
      case Some(userToken) => identityService.findOption(userToken.getPrincipal)
      case None => None
    }
  }

  // TODO
  def findPreviousSignupAttempts(token: String, attempts: Int): Identity = ???

  // TODO
  def identityExists(principal: String): Boolean = ???

  /**
    * Save the signup form and create a Token.
    *
    * @param command
    * @param ipAddress
    */
  def saveOrUpdate(command: UserToken, ipAddress: String): UserToken = {
    //TODO criar campo ip para UserToken
    val identity:Identity = identityService.findOption(command.getPrincipal) match {
      case Some(id) =>
        logger.warn("Identity {} trying userToken already existing", command.getPrincipal)
        id
      case None =>
        logger.info("New identity {} created", command.getPrincipal)
        identityService.install(command)
    }
    Option(userTokenRepository.findByTokenSourceAndPrincipal(UserToken.TokenSources.SIGNUP.name(), identity.getPrincipal)) match {
      case Some(userToken) => userToken
      case None =>
        logger.info("New userToken for {} created", command.getPrincipal)
        userTokenRepository.saveAndFlush(
          new UserToken(UserToken.TokenSources.SIGNUP, command.getPrincipal).appendFirstName(command.getFirstName))
    }
  }


}