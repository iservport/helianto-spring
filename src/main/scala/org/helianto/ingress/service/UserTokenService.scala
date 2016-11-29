package org.helianto.ingress.service

import java.util.Date

import org.helianto.core.domain.Identity
import org.helianto.core.service.IdentityService
import org.helianto.ingress.domain.UserToken
import org.helianto.ingress.repository.UserTokenRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.stereotype.Service

/**
  * User token query service.
  *
  * @author mauriciofernandesdecastro
  */
@Service
class UserTokenService {

  val logger: Logger = LoggerFactory.getLogger(classOf[UserTokenService])

  @Value("${helianto.user-token-validity:5}") val userTokenValidity = 5

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
    getUserTokenOption(confirmationToken, 0) match {
      case Some(userToken) => identityService.findOption(userToken.getPrincipal)
      case None => None
    }
  }

  // TODO consider attempts
  def getUserTokenOption(confirmationToken: String, attempts: Int): Option[UserToken] =
  Option(userTokenRepository.findByToken(confirmationToken)) match {
    case Some(userToken) if userToken.isValidTo(userTokenValidity)=> Some(userToken)
    case _ => None
  }

  // TODO
  def identityExists(principal: String): Boolean = ???

  /**
    * Save the signup form and create a Token.
    *
    * @param command
    * @param ipAddress
    */
  def saveOrUpdate(command: UserToken, ipAddress: String): UserToken = {
    //TODO create ip field on UserToken
    val identity:Identity = identityService.findOption(command.getPrincipal) match {
      case Some(id) =>
        logger.warn("Identity {} trying userToken already existing", command.getPrincipal)
        id
      case None => throw new IllegalArgumentException("Token must reference an existing identity.")
    }
    Option(userTokenRepository.findByTokenSourceAndPrincipal("SIGNUP", identity.getPrincipal)) match {
      case Some(userToken) => userToken
      case None =>
        logger.info("New userToken for {} created", command.getPrincipal)
        userTokenRepository.saveAndFlush(
          new UserToken("SIGNUP", command.getPrincipal).appendFirstName(command.getFirstName))
    }
  }

}