package org.helianto.security.service

import java.security.Principal

import org.helianto.security.domain.UserDetailsAdapter
import org.helianto.user.repository.UserProjection
import org.springframework.security.core.Authentication


trait UserDetailsConverter {

  def getUserProjection(implicit principal: Principal): UserProjection = {
    val authentication = principal.asInstanceOf[Authentication]
    val adapter = authentication.getPrincipal.asInstanceOf[UserDetailsAdapter]
    adapter.getUser
  }

}
