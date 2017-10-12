package org.helianto.installer.service

import java.nio.file.{Files, Paths}

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser.Feature
import com.fasterxml.jackson.databind.`type`.TypeFactory
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import org.helianto.core.domain.State
import org.helianto.core.service.{ContextInstallService, StateInstallService}
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import scala.reflect._

/**
  * Default context install runner.
  */
@Component
@Scope("prototype")
class ContextInstallRunner(contextService: ContextInstallService, stateService: StateInstallService) {

  import collection.JavaConversions._

  def run() = {
    val context = contextService.installDefaultContext()
    readStates.map(stateService.installState)
  }

  def readStates =  readValue[State]("/states.json").map(updateInstallDate)

  def readValue[T: ClassTag](source: String): List[T] = {
    Option(getClass.getResource(source)) match {
      case Some(resource) =>
        val data = Files.readAllBytes(Paths.get(resource.toURI))
        val mapper = new ObjectMapper(new JsonFactory().configure(Feature.ALLOW_COMMENTS, true))
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val list: java.util.List[T] = mapper.readValue(data
          , TypeFactory.defaultInstance.constructCollectionType(classOf[java.util.List[T]], classTag[T].runtimeClass))
        list.toList
      case _ => List[T]()
    }
  }

  def updateInstallDate(state: State) = {
    state.setInstallDate(state.getInstallDate) ; state
  }

}
