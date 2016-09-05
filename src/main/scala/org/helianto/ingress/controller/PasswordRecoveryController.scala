/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helianto.ingress.controller

import java.util.Locale

import org.helianto.ingress.service.{ResponseService, UserTokenQueryService}
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation._

// TODO implement notification

/**
  * Forgot password?
  */
@Controller
@RequestMapping(value = Array("/recovery"))
class PasswordRecoveryController
(responseService: ResponseService
 , userTokenQueryService: UserTokenQueryService){

  /**
    * Get e-mail recovery page.
    *
    * @param error
    * @param model
    * @param locale
    */
  @GetMapping
  def getRecoveryPage(error: String, model: Model, locale: Locale) = {
    Option(error) match {
      case Some(e) => model.addAttribute("error", error)
      case None =>
    }
    responseService.recoveryResponse(model, locale)
  }

  /**
    * Post e-mail recovery page.
    *
    * @param model
    * @param principal
    * @param locale
    */
  @PostMapping
  def postRecoveryPage(model: Model, @RequestParam(required = false) principal: String, locale: Locale) = {
    userTokenQueryService.createOrRefreshToken(principal, "PASSWORD_RECOVERY")
    responseService.confirmationResponse(model, locale)
  }

}