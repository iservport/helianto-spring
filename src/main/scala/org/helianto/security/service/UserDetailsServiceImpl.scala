package org.helianto.security.service

import org.helianto.security.domain.{UserAuthority, UserDetailsAdapter}
import org.helianto.security.repository.UserAuthorityRepository
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService}
import org.springframework.stereotype.Service

@Service("userDetailsService")
@Qualifier("default")
class UserDetailsServiceImpl extends AbstractDetailsService with UserDetailsService {

  def loadUserByUsername(userKey: String): UserDetails = {
    val secret = loadIdentitySecretByKey(userKey)
    val user = loadLastUserByIdentityId(secret.getIdentity.getId)
    afterLoadUser(new UserDetailsAdapter(user, secret))
  }

}
