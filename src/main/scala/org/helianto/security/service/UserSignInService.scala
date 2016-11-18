package org.helianto.security.service

import org.helianto.security.domain.UserDetailsAdapter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

/**
  * Authenticate and authorize a given user.
  */
@Service("userSigninService")
class UserSignInService extends AbstractDetailsService {

  def signin(userId: String) =
    Option(userRepository.findById(userId)) match {
      case Some(user) =>
        val userDetails = afterLoadUser(new UserDetailsAdapter(user))
        val auth = new UsernamePasswordAuthenticationToken(userDetails, null.asInstanceOf[Any], userDetails.getAuthorities)
        SecurityContextHolder.getContext.setAuthentication(auth)
      case None =>
    }

}
