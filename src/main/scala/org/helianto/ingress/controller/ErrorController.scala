package org.helianto.ingress.controller

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.boot.autoconfigure.web.{ErrorAttributes, ErrorController}
import org.springframework.web.bind.annotation.{GetMapping, RestController}
import org.springframework.web.context.request.ServletRequestAttributes

import scala.beans.BeanProperty

@RestController
class CustomErrorController(errorAttributes: ErrorAttributes) extends ErrorController {

  final val PATH = "/error"

  override def getErrorPath: String = PATH

  @GetMapping(Array(PATH))
  def error(request: HttpServletRequest, response: HttpServletResponse) = {
    val m = getErrorAttributes(request, true)
    ErrorWrapper(response.getStatus, m.get("error"), m.get("message"), m.get("timestamp"), m.get("trace"))
  }

  def getErrorAttributes(request: HttpServletRequest, includeStackTrace: Boolean): java.util.Map[String, AnyRef] = {
    val requestAttributes = new ServletRequestAttributes(request)
    errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace)
  }

}

case class ErrorWrapper(@BeanProperty status: Int,
                        @BeanProperty error: AnyRef,
                        @BeanProperty message: AnyRef,
                        @BeanProperty timestamp: AnyRef,
                        @BeanProperty trace: AnyRef) {

//  @BeanProperty val traceLines = trace.toString.replaceAll("\\r\\n|\\r|\\n", " ").split("at ").take(5)
}
