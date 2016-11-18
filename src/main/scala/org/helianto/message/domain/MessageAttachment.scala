package org.helianto.message.domain

import scala.beans.BeanProperty

case class MessageAttachment
(  @BeanProperty content: String
 , @BeanProperty filename: String
 , @BeanProperty `type`: String = ""
 , @BeanProperty disposition: String = ""
 , @BeanProperty content_id: String = "")
