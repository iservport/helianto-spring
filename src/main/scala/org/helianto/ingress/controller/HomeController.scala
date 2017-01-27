package org.helianto.ingress.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, RequestMapping}

@Controller
@RequestMapping(Array("/"))
class HomeController {

  @GetMapping(value = Array("favicon.ico"))
  def favicon():String  = "forward:/images/favicon.png"

  @Value("${helianto.title}") val title = ""

  @Value("${helianto.base-name}") val baseName = ""

  @Value("${helianto.api.url}") val apiUrl = ""

  @ModelAttribute("baseName") def getBaseName = baseName

  @ModelAttribute("title") def getTitle = title

  @GetMapping def index = "frame-bootstrap"

}
