package org.helianto.ingress.controller

import org.helianto.ingress.domain.Invite
import org.helianto.ingress.service.InviteService
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation._

/**
  * Invite controller.
  */
@RestController
@RequestMapping(Array("/api/invite"))
class InviteRestController(service: InviteService) extends AuthorityExtractor {

  @GetMapping(path = Array("/{token}"))
  def get(implicit principal: OAuth2Authentication, @PathVariable token: String) =
    service.get(token)

  @GetMapping(path = Array("/user/{userId}"))
  def getUser(implicit principal: OAuth2Authentication, @PathVariable userId: String) =
    service.getUser(userId)

  @PostMapping
  def save(implicit principal: OAuth2Authentication, @RequestBody command: Invite) =
    service.save(command)

}
