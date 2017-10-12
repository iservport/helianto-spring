package org.helianto.installer.service

import java.net.URI

import com.fasterxml.jackson.databind.ObjectMapper
import org.helianto.installer.RootProperties
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

import scala.util.{Failure, Success, Try}

@Service
class DefaultInstaller(contextRunner: ContextInstallRunner) extends InitializingBean {

  val logger: Logger = LoggerFactory.getLogger(classOf[DefaultInstaller])

//  val DEFAULT_CONTEXT_NAME: String = "DEFAULT"
//
//  @Autowired val restTemplate: RestTemplate = null
//  @Autowired val cityInstaller: CityInstaller = null
////  @Autowired val entityInstaller: EntityInstallService = null
//
//  @Value("${helianto.api.url}") val apiUrl: String = null
//  @Value("${helianto.defaultCountry}") val defaultCountry: String = null
//
////  @Autowired private val identityInstaller: IdentityInstallService = null
//  @Autowired @Lazy private val rootData: RootProperties = null
//
  @throws[Exception]
  def afterPropertiesSet {
  contextRunner.run()
//    val appUri: URI = UriComponentsBuilder.fromHttpUrl(apiUrl).path(s"api/context").build(true).toUri
//    Try(restTemplate.getForEntity(appUri, classOf[String])) match {
//      case Success(responseEntity) =>
//        val mapper = new ObjectMapper()
//        val context = mapper.readValue(responseEntity.getBody, classOf[java.util.Map[String, String]])
//        println(context.get("contextName"))
//      case Failure(e) =>
//        throw new IllegalArgumentException(s"Unable to install to $apiUrl")
//    }
  }
//  final protected def install(contextName: String) {
//    cityInstaller.installStatesAndCities(contextName, defaultCountry)
////    val entityData: Entity = rootData.getEntity
////    val identityData = rootData.getIdentity
////    val rootIdentity: Identity = identityInstaller.install(identityData)
////    val rootEntity: Entity =
////      entityInstaller.install(entityData.getCityCode, entityData.getAlias, rootIdentity)
////    runAfterInstall(rootEntity)
//  }
}