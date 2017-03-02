package org.helianto.security.service

import java.util.Date
import javax.servlet.http.{HttpServletRequest, HttpServletResponse, HttpSession}

import org.helianto.security.domain.UserDetailsAdapter
import org.helianto.user.domain.User
import org.helianto.user.repository.UserProjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2ClientContext
import org.springframework.security.oauth2.common.exceptions.{InvalidGrantException, InvalidTokenException}
import org.springframework.security.web.context.{HttpSessionSecurityContextRepository, SecurityContextPersistenceFilter, SecurityContextRepository}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
  * Authenticate and authorize a given user.
  */
@Service("userSigninService")
class UserSignInService extends AbstractDetailsService {

  val key = "SPRING_SECURITY_CONTEXT"
  var repo: SecurityContextRepository = new HttpSessionSecurityContextRepository()

  @Autowired
  val oauth2ClientContext: OAuth2ClientContext = null

  def signin(userId: String) =
    Option(userRepository.findById(userId)) match {
      case Some(user) =>
        val userDetails = afterLoadUser(new UserDetailsAdapter(user))
        val auth = new UsernamePasswordAuthenticationToken(userDetails, null.asInstanceOf[Any], userDetails.getAuthorities)
        SecurityContextHolder.getContext.setAuthentication(auth)
      case None =>
    }

  /**
    * Ensure the userId shares the same identity with the currently authorized user.
    *
    * @param identityId
    * @param toUserId
    */
  @Transactional
  def signin(identityId: String, toUserId: String, request: HttpServletRequest): Option[String] = {
    println(s"""BEFORE SIGNIN
                |
                |${SecurityContextHolder.getContext.getAuthentication}
                |
              """.stripMargin)
    Option(userRepository.setUserLastInfoByIdAndIdentity_Id(toUserId, identityId, new Date())) match {
      case Some(()) =>
//        Option(request.getCookies).getOrElse(Array()).foreach(_.setMaxAge(0))
//        SecurityContextHolder.clearContext()
//        Option(request.getSession(false)) match {
//          case Some(session) => session.invalidate()
//          case None =>
//        }
        signin(toUserId)
        println(s"""AFTER SIGNIN
                    |
                    |${SecurityContextHolder.getContext.getAuthentication}
                    |
              """.stripMargin)
//        session.setAttribute(key, SecurityContextHolder.getContext)
        Some(toUserId)
      case None => None
  }}

}
