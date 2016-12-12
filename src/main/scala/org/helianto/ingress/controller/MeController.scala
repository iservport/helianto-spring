package org.helianto.ingress.controller

import org.helianto.core.service.IdentityService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}

/**
  * Identity controller.
  */
@RestController
@RequestMapping(Array("/api/me"))
class MeController(identityService: IdentityService) extends AuthorityExtractor {

  /**
    * The authenticated user identity data.
    */
  @GetMapping
  def me(implicit principal: OAuth2Authentication) = identityService.findById(_identityId)

}
