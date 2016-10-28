package org.helianto.core.utils

import org.springframework.data.jpa.repository.JpaRepository

trait CommandMixin[T <: Mergeable[T]] {

  val targetRepository: JpaRepository[T, String]

  def saveOrUpdate(command: T): T = {
    val target: T = Option(targetRepository.findOne(command.getId)) match {
      case Some(t) => if (validateTarget(t, command)) t else throw new IllegalArgumentException(s"Invalid $t")
      case None => getNewTarget(command)
    }
    saveTarget(target, command)
  }

  protected def validateTarget(target: T, command: T): Boolean = true

  protected def getNewTarget(command: T): T

  protected def saveTarget(target: T, command: T) = mergeTarget(target, command)

  protected def mergeTarget(target: T, command: T) = targetRepository.saveAndFlush(target.merge(command))

}
