package org.helianto.user.controller

import org.helianto.ingress.controller.AuthorityExtractor
import org.helianto.user.service.UserAccessService
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation._

/**
  * User Access controller.
  */
@RestController
@RequestMapping(Array("/api/user/access"))
class UserAccessController(service: UserAccessService) extends AuthorityExtractor {

  @GetMapping
  def gstUserAccess(implicit principal: OAuth2Authentication) = service.userAccess(_identityId)

  @GetMapping(params = Array("alias"))
  def getUserAccess(implicit principal: OAuth2Authentication, @RequestParam alias: String) = service.userAccess(_identityId)

}
