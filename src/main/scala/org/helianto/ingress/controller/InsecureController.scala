package org.helianto.ingress.controller

import org.helianto.ingress.domain.Invite
import org.helianto.ingress.service.InviteService
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation._

/**
  * Insecure api controller.
  */
@RestController
@RequestMapping(Array("/api/insecure"))
class InsecureController(service: InviteService) {

  @GetMapping(path = Array("/invite"))
  def invite() = s"""{"insecure":true}"""

  @GetMapping(path = Array("/invite/{id}"))
  def get(implicit principal: OAuth2Authentication, @PathVariable id: String) = service.get(id)

  /**
    * Invite must only be updated if there is a previous valid record.
    *
    * @param command the invite
    */
  @PostMapping(path = Array("/invite"))
  def updateInvite(@RequestBody command: Invite) = service.update(command)

}
