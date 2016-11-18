package org.helianto.message.domain

import scala.beans.BeanProperty

/**
  * Message defaults.
  */
case class MessageDefaults
(@BeanProperty greeting: String = "Hello, "
 , @BeanProperty apiHome: String = "https://localhost:8443"
 , @BeanProperty seeOnline: String = "See on-line"
 , @BeanProperty sentByText: String = "Sent by "
 , @BeanProperty disclaimer: String = "&nbsp;"
 , @BeanProperty unsubscribeText: String = "&nbsp;"
 , @BeanProperty unsubscribeCaption: String = "&nbsp;"
 , @BeanProperty unsubscribeService: String = "unsubscribe"
 , @BeanProperty ensure: String = "&nbsp;"
 , @BeanProperty copyright: String = "&nbsp;"
) {

  def this() {
    this("", "", "", "", "", "", "", "", "", "")
  }

}
