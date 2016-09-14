logLevel := Level.Warn

dependencyOverrides += "org.scala-sbt" % "sbt" % "0.13.9"

addSbtPlugin("com.earldouglas" % "xsbt-web-plugin" % "2.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.4")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.8.0")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.12")

