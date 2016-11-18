package org.helianto.ingress.controller

import org.helianto.core.service.IdentityService
import org.springframework.web.bind.annotation._

/**
  * Identity controller.
  */
@RestController
@RequestMapping(Array("/identity"))
class IdentityController(identityService: IdentityService) {

  /**
    * True if identity already exists.
    *
    * @param principal
    */
  @GetMapping(params = Array("principal"))
  def isIdentityExisting(@RequestParam principal: String) =
    s"""{"canCreate":${identityService.findOption(principal).nonEmpty}}"""

}
