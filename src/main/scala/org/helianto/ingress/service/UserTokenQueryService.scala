package org.helianto.ingress.service

import java.util.Date

import org.helianto.core.repository._
import org.helianto.user.domain.UserToken
import org.helianto.user.repository.UserTokenRepository
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
  * User token query service.
  *
  * @author mauriciofernandesdecastro
  */
@Service
class UserTokenQueryService {

  val logger: Logger = LoggerFactory.getLogger(classOf[UserTokenQueryService])

  @Autowired val identityRepository: IdentityRepository = null

  @Autowired val userTokenRepository: UserTokenRepository = null

  /**
    * Create or refresh token.
    *
    * @param principal
    */
  def createOrRefreshToken(principal: String, tokenSource:String): UserToken = {
    Option(identityRepository.findByPrincipal(principal)) match {
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

}