package org.helianto.message.domain

import scala.beans.BeanProperty

/**
  * Contact data.
  *
  * @param contactEmail
  * @param contactName
  * @param entityName
  * @param imageUrl
  * @param address
  */
case class ContactData
(@BeanProperty contactEmail: String
 , @BeanProperty contactName: String
 , @BeanProperty entityName: String = "&nbsp;"
 , @BeanProperty imageUrl: String = "&nbsp;"
 , @BeanProperty address: String = "&nbsp;") {

  def this() {
    this("", "", "", "", "")
  }

}
