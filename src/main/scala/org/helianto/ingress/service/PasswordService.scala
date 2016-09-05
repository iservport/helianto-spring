package org.helianto.ingress.service

import org.helianto.core.domain.Identity
import org.helianto.core.repository.IdentityRepository
import org.helianto.security.domain.Secret
import org.helianto.security.repository.SecretRepository
import org.helianto.user.repository.UserTokenRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
  * Password service.
  *
  * @author mauriciofernandesdecastro
  */
@Service
class PasswordService
( userTokenRepository: UserTokenRepository
  , identityRepository: IdentityRepository
  , identitySecretRepository: SecretRepository
  , encoder: PasswordEncoder
){

  /**
    * Exchange a token by its corresponding identity.
    *
    * @param confirmationToken
    */
  def getIdentityOption(confirmationToken: String): Option[Identity] = {
    Option(userTokenRepository.findByToken(confirmationToken)) match {
      case Some(userToken) => Option(identityRepository.findByPrincipal(userToken.getPrincipal))
      case None => None
    }
  }

  /**
    * True encrypted password matches the changing one.
    *
    * @param pass
    * @param identity
    */
  def ckeckPassword(pass: String, identity: Identity): Boolean = {
    val identitySecret: Secret = identitySecretRepository.findByIdentityKey(identity.getPrincipal)
    encoder.matches(pass, identitySecret.getIdentitySecret)
  }

  /**
    * Do change password.
    *
    * @param principal
    * @param password
    */
  def changePassword(principal: String, password: String): Secret = {
    Option(identitySecretRepository.findByIdentityKeyOrEmail(principal)) match {
      case Some(secret) => identitySecretRepository.saveAndFlush(secret.encode(encoder,password))
      case _ => throw new IllegalArgumentException("Unable to find secret to change.")
    }
  }

}
