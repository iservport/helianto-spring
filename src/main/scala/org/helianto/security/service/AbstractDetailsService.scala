package org.helianto.security.service

import org.helianto.core.service.ContextInstallService
import org.helianto.security.domain.{UserAuthority, UserDetailsAdapter}
import org.helianto.security.repository.{SecretRepository, UserAuthorityRepository}
import org.helianto.user.repository.{UserProjection, UserRepository}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException

class AbstractDetailsService {

  @Value("${helianto.contextName}") val contextName: String = ""
  @Autowired val secretRepository: SecretRepository = null
  @Autowired val userRepository: UserRepository = null
  @Autowired val userAuthorityRepository: UserAuthorityRepository = null
  @Autowired val contextInstallService: ContextInstallService = null

  val logger: Logger = LoggerFactory.getLogger(classOf[AbstractDetailsService])

  private[service]  def loadIdentitySecretByKey(identityKey: String) =
    Option(secretRepository.findByIdentityKeyOrEmail(identityKey)) match {
      case Some(s) => s
      case None => error(identityKey)
    }

  private[service]  def loadLastUserByIdentityId(identityId: String): UserProjection =
    Option(userRepository.findByIdentity_IdOrderByLastEventDesc(identityId)) match {
      case Some(list) if !list.isEmpty => list.get(0)
      case _ => error(identityId)
    }

  private[service] def updateLastEvent(userDetails: UserDetailsAdapter): Unit = {
    Option(userRepository.findOne(userDetails.getUserId)) match {
      case Some(u) =>
        userRepository.saveAndFlush(u.updateLastEvent())
      case _ => error(userDetails.getUserId)
    }
  }

  private[service] def updateAuthorities(user: UserDetailsAdapter) =
    Option(userAuthorityRepository.findByUserIdOrderByServiceCodeAsc(user.getUserId)) match {
      case Some(userAuthorities) =>
        import collection.JavaConversions._
        val authorities = userAuthorities
          .flatMap(getUserAuthoritiesAsString(_))
          .map(new SimpleGrantedAuthority(_))
          .distinct
          .:+(new SimpleGrantedAuthority(s"CONTEXT_${contextName}"))
          .:+(new SimpleGrantedAuthority(s"SELF_ID_${user.getIdentityId}"))
          .:+(new SimpleGrantedAuthority(s"USER_ID_${user.getUserId}"))
          .:+(new SimpleGrantedAuthority(s"ENTITY_ID_${user.getUser.getEntityId}"))
          .toList
        user.updateAuthorities(authorities)
      case None => user
    }

  private[service] def afterLoadUser(userDetails: UserDetailsAdapter) = {
    updateLastEvent(userDetails)
    updateAuthorities(userDetails)
  }

  /**
    * As an additional control, if an error occurs during login, the context installation service is invoked.
    *
    * @param source
    *
    * @throws UsernameNotFoundException
    */
  private[service] def error(source: String) = {
    contextInstallService.installDefaultContext()
    logger.error("Unable to load by user name with {}.", source)
    throw new UsernameNotFoundException("Unable to find user name for " + source)
  }

  private[service] def getUserAuthoritiesAsString(authority: UserAuthority) = {
    expandUserAuthorities(authority.getServiceCode.toUpperCase, authority.getServiceExtension)
  }

  private[service] def expandUserAuthorities(serviceCode: String, extensions: String): Iterable[String] = {
    Option(extensions) match {
      case Some(s) => s.replaceAll(" ", "").split(",").map(_.toUpperCase()).map(e => s"ROLE_${serviceCode}_${e}")
      case None => Seq(s"ROLE_${serviceCode}_")
    }
  }

}