package org.helianto.security.service

import org.springframework.security.oauth2.common.{DefaultOAuth2AccessToken, OAuth2AccessToken}
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import org.springframework.stereotype.Service

@Service
class CustomTokenEnhancer extends TokenEnhancer {

  def enhance(accessToken: OAuth2AccessToken, authentication: OAuth2Authentication): OAuth2AccessToken = {
    val additionalInfo = new java.util.HashMap[String, AnyRef]
    additionalInfo.put("organization", authentication.getName)
    (accessToken.asInstanceOf[DefaultOAuth2AccessToken]).setAdditionalInformation(additionalInfo)
    accessToken
  }

}