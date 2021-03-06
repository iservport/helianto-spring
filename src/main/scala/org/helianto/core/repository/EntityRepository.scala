package org.helianto.core.repository

import java.util.Date

import org.helianto.core.domain.Entity
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

trait EntityRepository extends JpaRepository[Entity, String] {

  def findById(entityId: String): EntityProjection

  def findByContextName(contextName: String, sort: Sort): java.util.List[EntityProjection]

  def countByContextNameAndAliasIgnoreCase(contextName: String, alias: String): Long

  def findByContextNameAndAliasIgnoreCase(contextName: String, alias: String): Entity

}

trait EntityProjection {

  def getId(): Int

  @Value("#{target.operator.id}")
  def getContextId(): Int

  @Value("#{target.alias}")
  def getEntityAlias(): String

  def getEntityCode(): String

  def getEntityName(): String

  def getInstallDate(): Date

  def getSummary(): String

  def getEntityDomain(): String

  def getExternalLogoUrl(): String

  def getActivityState(): Char

  def getEntityType(): Char

  @Value("#{target.city.id}")
  def getCityId(): Int

  @Value("#{target.city.cityName}")
  def getCityName(): String

  @Value("#{target.city.command.id}")
  def getStateId(): Int

  @Value("#{target.city.command.stateCode}")
  def getStateCode(): String

  @Value("#{target.city.command.stateName}")
  def getStateName(): String

  @Value("#{target.city.command.country.id}")
  def getCountryId(): Int

}

