package org.helianto.ingress.repository

import org.helianto.ingress.domain.Registration
import org.springframework.data.jpa.repository.JpaRepository

trait RegistrationRepository extends JpaRepository[Registration, String] {

}
