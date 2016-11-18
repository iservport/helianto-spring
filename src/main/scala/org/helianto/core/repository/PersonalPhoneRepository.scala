package org.helianto.core.repository

import org.helianto.core.domain.PersonalPhone
import org.springframework.data.jpa.repository.JpaRepository

trait PersonalPhoneRepository extends JpaRepository[PersonalPhone, String] {

  def findByIdentityId(identityId: String): java.util.List[PersonalPhone]

  def findByIdentityIdAndPhoneLabel(identityId: String, phoneLabel: String): PersonalPhone

}

