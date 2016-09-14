package org.helianto.user.repository

import org.helianto.user.domain.{UserAssociation, User}
import org.springframework.data.jpa.repository.JpaRepository

trait UserAssociationRepository extends JpaRepository[UserAssociation, Integer] {

  def findByParentAndChild(parent: User, child: User): UserAssociation

  def findByParent(parent: User): java.util.List[UserAssociation]

}
