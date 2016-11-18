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

import org.helianto.ingress.domain.Registration
import org.helianto.ingress.service.{RegistrationService, ResponseService, UserTokenService}
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation._

/**
  * Registration controller.
  */
@Controller
@RequestMapping(Array("/register"))
class RegistrationController(registrationService: RegistrationService) {

  /**
    * Get the registration page.
    *
    * @param model
    * @param confirmationToken
    * @param locale
    */
  @GetMapping(params = Array("confirmationToken"))
  def getRegistrationPage(model: Model, @RequestParam confirmationToken: String, locale: Locale) =
    registrationService.get(confirmationToken, model, locale)

  /**
    * Post the registration before the user atually submits.
    *
    * @param model
    * @param registration
    * @param locale
    *
    */
  @PostMapping
  def postRegistration(model: Model, @ModelAttribute registration : Registration , locale: Locale) =
  registrationService.createAdmin(registration, model , locale)

  /**
    * Post the administrator submission.
    *
    * @param model
    * @param registration
    * @param locale
    *
    */
  @PostMapping(path = Array("/admin"))
  def postAdminSubmission(model: Model, @ModelAttribute registration : Registration , locale: Locale) =
  registrationService.createAdmin(registration, model , locale)

  /**
    * Post a new member submission.
    *
    * @param model
    * @param registration
    * @param locale
    *
    */
  @PostMapping(path = Array("/member"))
  def postMemberSubmission(model: Model, @ModelAttribute registration : Registration , locale: Locale) =
    registrationService.subscribe(registration, model , locale)

}