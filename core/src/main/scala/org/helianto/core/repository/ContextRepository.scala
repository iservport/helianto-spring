package org.helianto.core.repository

import org.helianto.core.domain.Context
import org.springframework.data.jpa.repository.JpaRepository

trait ContextRepository extends JpaRepository[Context, Integer] {

  def findByContextName(contextName: String = "DEFAULT"): Context

}
