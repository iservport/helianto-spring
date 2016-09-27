package org.helianto

import java.security.Principal

import org.helianto.ingress.controller.FreemarkerMixIn
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping}

@Controller
@RequestMapping(value = Array("/"))
@PreAuthorize("isAuthenticated()")
class HeliantoController extends FreemarkerMixIn {

  @GetMapping(value = Array("favicon.ico"))
  def favicon():String  = "forward:/images/favicon.ico"

  @GetMapping
  def index(principal: Principal, model: Model) = response(principal, model, "home")

}
