package org.helianto.installer.service

import org.helianto.core.service.{ContextInstallService, EntityInstallService}
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
  * Default entity install runner.
  */
@Component
@Scope("prototype")
class EntityInstallRunner(service: EntityInstallService, contextService: ContextInstallService) {

  def run() = {
    contextService.installDefaultContext()
//    service.install()
  }

}
