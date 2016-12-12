package org.helianto.ingress.controller

import org.helianto.core.service.EntityInstallService
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}

/**
  * Entity controller.
  */
@RestController
@RequestMapping(Array("/api/entity"))
class EntityController(entityService: EntityInstallService) extends AuthorityExtractor {

  /**
    * The authenticated entity data.
    */
  @GetMapping
  def entity(implicit principal: OAuth2Authentication) = entityService.findById(_entityId)

}
