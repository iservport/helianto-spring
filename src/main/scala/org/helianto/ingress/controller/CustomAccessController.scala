package org.helianto.ingress.controller

import org.springframework.security.oauth2.provider.{AuthorizationRequest, ClientDetailsService}
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, RequestMapping, SessionAttributes}

@Controller
@RequestMapping(Array("/oauth/confirm_access"))
@SessionAttributes(Array("authorizationRequest"))
class CustomAccessController(clientDetailsService: ClientDetailsService) {

    @GetMapping def approval(@ModelAttribute clientAuth: AuthorizationRequest, model: Model) = {
      val client = clientDetailsService.loadClientByClientId(clientAuth.getClientId)
      model.addAttribute("client", client)
      "frame-approval"
    }

}
