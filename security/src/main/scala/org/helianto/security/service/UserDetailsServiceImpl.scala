package org.helianto.security.service

import org.helianto.security.domain.{Secret, UserAuthority, UserDetailsAdapter}
import org.helianto.security.repository.UserAuthorityRepository
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService}
import org.springframework.stereotype.Service

@Service("userDetailsService")
@Qualifier("default")
class UserDetailsServiceImpl extends AbstractDetailsService with UserDetailsService {

  import UserDetailsServiceImpl._

  @Autowired val userAuthorityRepository: UserAuthorityRepository = null

  def loadUserByUsername(userKey: String): UserDetails = {
    val secret = loadIdentitySecretByKey(userKey)
    val user = loadLastUserByIdentityId(secret.getIdentity.getId)
    val userDetails = new UserDetailsAdapter(user, secret)
    updateLastEvent(userDetails)
    updateAuthorities(userDetails)
  }

  def updateAuthorities(user: UserDetailsAdapter) =
    Option(userAuthorityRepository.findByUserIdOrderByServiceCodeAsc(user.getUserId)) match {
      case Some(userAuthorities) =>
        import collection.JavaConversions._
        val authorities = userAuthorities
          .flatMap(getUserAuthoritiesAsString(_))
          .map(new SimpleGrantedAuthority(_))
          .distinct
          .:+(new SimpleGrantedAuthority(s"SELF_ID_${user.getIdentityId}"))
          .toList
        user.updateAuthorities(authorities)
      case None => user
    }

}

object UserDetailsServiceImpl {

  def getUserAuthoritiesAsString(authority: UserAuthority) = {
    expandUserAuthorities(authority.getServiceCode.toUpperCase, authority.getServiceExtension)
  }

  private def expandUserAuthorities(serviceCode: String, extensions: String): Iterable[String] = {
    Option(extensions) match {
      case Some(s) => s.replaceAll(" ", "").split(",").map(_.toUpperCase()).map(e => s"ROLE_${serviceCode}_${e}")
      case None => Seq(s"ROLE_${serviceCode}_")
    }
  }

}