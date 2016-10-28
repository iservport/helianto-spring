package org.helianto.ingress

case class Message
( subject: String
  , senderEmail: String
  , senderName: String
  , recipientEmail: String
  , recipientFirstName: String
  , title: String = "Title"
  , entityName: String = "entityName"
  , callBackUri: String = "localhost"
  , onlineCaption: String = "See on-line"
  , greeting: String = "Hello, "
  , procedure: String = "Please, do something."
  , actionCaption: String = "Use this button"
  , actionUri: String = "localhost/doit"
  , fallBack: String = "Otherwise"
  , trailingInfo: String = "")