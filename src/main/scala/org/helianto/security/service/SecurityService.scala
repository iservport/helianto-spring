package org.helianto.security.service

import org.helianto.core.domain.Identity
import org.helianto.ingress.service.{ResponseService, UserTokenService}
import org.helianto.security.domain.Secret
import org.helianto.security.repository.SecretRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
  * Security service.
  *
  * @author mauriciofernandesdecastro
  */
@Service
class SecurityService(val identitySecretRepository: SecretRepository, val encoder: PasswordEncoder){

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
  def changePassword(principal: String, password: String): Secret =
  Option(identitySecretRepository.findByIdentityKeyOrEmail(principal)) match {
    case Some(secret) => identitySecretRepository.saveAndFlush(secret.encode(encoder,password))
    case _ => throw new IllegalArgumentException("Unable to find secret to change.")
  }


}
