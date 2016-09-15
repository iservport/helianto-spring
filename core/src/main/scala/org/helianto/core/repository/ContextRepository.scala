package org.helianto.core.repository

import java.util.Date

import org.helianto.core.domain.Context
import org.springframework.data.jpa.repository.JpaRepository

trait ContextRepository extends JpaRepository[Context, Integer] {

  def findByContextName(contextName: String): Context

  def findByInstallDateIsLessThanEqual(installDate: Date): java.util.List[Context]

}
