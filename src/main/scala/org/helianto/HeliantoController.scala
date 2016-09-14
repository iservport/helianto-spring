package org.helianto

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping}

@Controller
@RequestMapping(value = Array("/"))
@PreAuthorize("isAuthenticated()")
class HeliantoController extends InitializingBean {

  @Autowired val env:Environment = null

  @RequestMapping(value = Array("favicon.ico"))
  def favicon():String  = "forward:/public/images/favicon.ico"

  @GetMapping
  def index = "frame-bootstrap"

  override def afterPropertiesSet(): Unit = {
    println("Helianto")
  }
}
