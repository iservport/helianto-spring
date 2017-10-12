package org.helianto.ingress.controller

import org.helianto.core.service.EntityInstallService
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}

/**
  * Entity controller.
  */
@RestController
@RequestMapping(Array("/api/entity"))
class EntityController(service: EntityInstallService) extends AuthorityExtractor {

  /**
    * The authenticated entity data.
    */
  @GetMapping(path = Array("/list"))
  def list() = service.findByContext()

  /**
    * The authenticated entity data.
    */
  @GetMapping
  def entity(implicit principal: OAuth2Authentication) = service.findById(_entityId)

}
