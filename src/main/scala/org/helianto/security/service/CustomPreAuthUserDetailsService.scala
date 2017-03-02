package org.helianto.security.service

import org.helianto.security.domain.UserDetailsAdapter
import org.helianto.user.repository.UserProjection
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Service

/**
  * Pre-authentication service to allow the last authorized user to be presented via UserDetails interface, as
  * required by Spring Security.
  */
@Service
class CustomPreAuthUserDetailsService extends AbstractDetailsService with AuthenticationUserDetailsService[PreAuthenticatedAuthenticationToken] {

  def loadUserDetails(token: PreAuthenticatedAuthenticationToken): UserDetails =
    Option(loadLastUserByUserKey(token.getName)) match {
      case Some(user) => afterLoadUser(new UserDetailsAdapter(user))
      case None => null

  }
}