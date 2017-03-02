package org.helianto.security.controller

import javax.servlet.http.{HttpServletRequest, HttpServletResponse, HttpSession}

import org.helianto.ingress.controller.AuthorityExtractor
import org.helianto.security.service.UserSignInService
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation._

/**
  * User Sign-in controller.
  */
@RestController
@RequestMapping(Array("/api/user/signin"))
class UserSignInController(service: UserSignInService) extends AuthorityExtractor {

  @PostMapping
  def postSignin(implicit principal: OAuth2Authentication, @RequestBody userId: String, request: HttpServletRequest, session: HttpSession) =
    service.signin(_identityId, userId, request) match {
      case Some(toUserId) => s"""{"toUserId":"$toUserId"}"""
      case None => throw new IllegalArgumentException(s"Unable to sign in with user $userId")
    }

}
