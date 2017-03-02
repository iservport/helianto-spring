package org.helianto.ingress.controller

import org.helianto.ingress.domain.Invite
import org.helianto.ingress.service.InviteService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation._

/**
  * Invite controller.
  */
@Controller
@RequestMapping(Array("/invite"))
class InviteController(service: InviteService) extends AuthorityExtractor {

  @GetMapping
  def get(model: Model) = {
    model.addAttribute("pageName", "info/info-invite")
    "frame-static"
  }

  @PostMapping(path = Array("/{userType}/{entityId}"))
  def update(command: Invite) = service.update(command)

}
