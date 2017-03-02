package org.helianto.security.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component("preAuthProvider")
class CustomPreAuthProvider() extends PreAuthenticatedAuthenticationProvider {

  @Autowired
  private val userService: AuthenticationUserDetailsService[PreAuthenticatedAuthenticationToken] = null

  @PostConstruct
  def init() = super.setPreAuthenticatedUserDetailsService(userService)

}