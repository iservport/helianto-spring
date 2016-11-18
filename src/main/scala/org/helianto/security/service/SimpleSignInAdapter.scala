package org.helianto.security.service

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.springframework.security.web.WebAttributes
import org.springframework.security.web.savedrequest.RequestCache
import org.springframework.social.connect.Connection
import org.springframework.social.connect.web.SignInAdapter
import org.springframework.web.context.request.NativeWebRequest

/**
  * Sign-in adapter.
  *
  * @author mauriciofernandesdecastro
  */
class SimpleSignInAdapter (val requestCache: RequestCache, val userSignInService: UserSignInService)
  extends SignInAdapter {

  /**
    * Do sign-in.
    *
    * @param userId
    * @param connection
    * @param request
    */
  def signIn (userId: String, connection: Connection[_], request: NativeWebRequest): String = {
    userSignInService.signin (userId)
    extractOriginalUrl (request)
  }

  private[service] def extractOriginalUrl (request: NativeWebRequest): String = {
    val nativeReq = request.getNativeRequest(classOf[HttpServletRequest] )
    val nativeRes = request.getNativeResponse(classOf[HttpServletResponse] )
    Option(requestCache.getRequest(nativeReq, nativeRes)) match {
      case Some(saved) =>
        requestCache.removeRequest(nativeReq, nativeRes)
        Option(nativeReq.getSession(false) ) match {
          case Some(session) => session.removeAttribute (WebAttributes.AUTHENTICATION_EXCEPTION)
          case None =>
        }
        saved.getRedirectUrl
      case None => null
    }
  }

}