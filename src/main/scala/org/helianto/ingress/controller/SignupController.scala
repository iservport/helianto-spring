package org.helianto.ingress.controller

import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

import org.helianto.ingress.domain.UserToken
import org.helianto.ingress.service.{ResponseService, SignupService}
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation._
import org.springframework.web.context.request.WebRequest

/**
  * Base classe to SignUpController.
  *
  * @author mauriciofernandesdecastro
  */
@Controller
@RequestMapping(value = Array("/signup"))
class SignupController(signUpService:SignupService, responseService:ResponseService) {

  /**
    * Signup request page.
    *
    * @param model
    * @param contextId
    * @param principal
    * @param request
    */
  @GetMapping
  def getSignupPage(model: Model, @RequestParam(defaultValue = "1") contextId: Integer, @RequestParam(required = false) principal: String, request: WebRequest):String =
    signUpService.signUpOrRegister(request, model)


  /**
    * Quando o usuário confirma o pedido de inclusão no cadastro.
    *
    * <p>
    *   Envia um e-mail de confirmação para o cliente. As condições são as seguintes:
    * </p>
    * <ol>
    *   <li>Verifica se não é uma submissão dupla e notifica adequadamente.</li>
    *   <li>Caso não exista a entidade, cria e convida o solicitante a tornar-se o administrador.</li>
    *   <li>Caso exista a entidade, verifica se o solicitante já não é usuário;</li>
    *   <li>caso não seja, convida-o para participar da entidade,</li>
    *   <li>caso seja e esteja ativo, notifica-o e redireciona para o login,</li>
    *   <li>caso inativo, informa-o para solicitar readmissão junto ao administrador.</li>
    * </ol>
    *
    * @param model
    * @param command
    * @param error
    * @param request
    */
  @PostMapping
  def submitSignupPage(model: Model, @Valid command: UserToken, error: BindingResult, request: HttpServletRequest): String =
    signUpService.submitSignupPage(command, request, model)

}