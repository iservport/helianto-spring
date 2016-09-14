package org.helianto.core.service

import org.helianto.core.domain.{Identity, PersonalData}
import org.helianto.core.repository.IdentityRepository
import org.springframework.stereotype.Service

@Service
class IdentityInstallService {

  val identityRepository: IdentityRepository = null

  def installIdentity(principal: String, displayName: String, personalData: PersonalData): Identity = {
    Option(identityRepository.findByPrincipal(principal)) match {
      case Some (i) => i
      case _ => identityRepository.saveAndFlush(new Identity(principal, displayName, personalData))
    }
  }

}
