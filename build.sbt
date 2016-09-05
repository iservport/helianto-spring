lazy val root = (project in file(".")).
  enablePlugins(JavaAppPackaging).
  enablePlugins(DockerPlugin).
  settings(
    name := "helianto-spring",
    scalaVersion := "2.11.7",
    version := "1.0.0.SNAPSHOT",
    sbtVersion := "0.13.9",
    dockerBaseImage := "azul/zulu-openjdk:8",
    dockerUpdateLatest := true,
    dockerExposedPorts := Seq(8080),
    dockerRepository := Some("helianto")
  )


val springBootVersion = "1.4.0.RELEASE"

libraryDependencies ++= Seq(
  "org.scala-lang"             % "scala-library"                    % "2.11.7",
  "org.projectlombok"          % "lombok"                           % "1.16.8",
  "org.springframework.boot"   % "spring-boot-starter-web"          % springBootVersion,
  "org.springframework.boot"   % "spring-boot-starter-data-jpa"     % springBootVersion,
  "org.springframework.boot"   % "spring-boot-starter-tomcat"       % springBootVersion,
  "org.springframework.boot"   % "spring-boot-starter-security"     % springBootVersion,
  "org.springframework.boot"   % "spring-boot-starter-freemarker"   % springBootVersion,
  "org.springframework.boot"   % "spring-boot-starter-actuator"     % springBootVersion,
  "org.springframework.boot"   % "spring-boot-starter-test"         % springBootVersion % "test",
  "org.springframework.boot"   % "spring-boot-test-autoconfigure"   % springBootVersion % "test",
  "javax.servlet"              % "javax.servlet-api"  % "3.0.1"     % "provided",
  "commons-io"                 % "commons-io"         % "2.4",
  "mysql" % "mysql-connector-java" % "5.1.26",
  "com.zaxxer" % "HikariCP" % "2.4.3"
)
