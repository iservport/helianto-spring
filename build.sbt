import com.typesafe.sbt.packager.docker.Cmd
import sbt.Keys._

val heliantoSpringBootVersion = settingKey[String]("Spring Boot version")

heliantoSpringBootVersion in ThisBuild := "1.4.0.RELEASE"

organization in ThisBuild := "org.helianto"

version in ThisBuild := "1.2.8.RELEASE"

sbtVersion in ThisBuild := "0.13.9"

scalaVersion in ThisBuild := "2.11.8"

scalaJSUseRhino in Global := false

lazy val root = (project in file("."))
  .enablePlugins(JavaServerAppPackaging, UniversalDeployPlugin)
  .enablePlugins(DockerPlugin)
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(
    name := "helianto-spring",
    mainClass in (Compile) := Some("org.helianto.Application"),
    libraryDependencies ++= Seq(
      "org.projectlombok"                  % "lombok"                         % "1.16.8",
      "org.springframework.boot"           % "spring-boot-starter-web"        % heliantoSpringBootVersion.value,
      "org.springframework.boot"           % "spring-boot-starter-data-jpa"   % heliantoSpringBootVersion.value,
      "org.springframework.boot"           % "spring-boot-starter-test"       % heliantoSpringBootVersion.value % "test",
      "org.springframework.boot"           % "spring-boot-test-autoconfigure" % heliantoSpringBootVersion.value % "test",
      "org.springframework.boot"           % "spring-boot-starter-security"   % heliantoSpringBootVersion.value,
      "org.springframework.boot"           % "spring-boot-starter-freemarker" % heliantoSpringBootVersion.value,
      "org.springframework.boot"           % "spring-boot-starter-social-facebook" % heliantoSpringBootVersion.value,
      "org.springframework.boot"           % "spring-boot-starter-social-linkedin" % heliantoSpringBootVersion.value,
      "org.springframework.boot"           % "spring-boot-starter-actuator"   % heliantoSpringBootVersion.value,
      "org.springframework.security.oauth" % "spring-security-oauth2"         % "2.0.11.RELEASE",
      "org.springframework.security"       % "spring-security-jwt"            % "1.0.5.RELEASE",
      "org.springframework.social"         % "spring-social-google"           % "1.0.0.RELEASE",
      "com.twilio.sdk"                     % "twilio"                         % "7.1.0",
      "io.springfox"                       % "springfox-swagger2"             % "2.6.0",
      "io.springfox"                       % "springfox-swagger-ui"           % "2.6.0",
      "io.swagger"                         % "swagger-core"                   % "1.5.10",
      "javax.servlet"  % "javax.servlet-api"    % "3.0.1"                     % "provided",
      "commons-io"     % "commons-io"           % "2.4",
      "com.zaxxer"     % "HikariCP"             % "2.4.3",
      "com.h2database" % "h2"                   % "1.4.192",
      "mysql"          % "mysql-connector-java" % "5.1.26",
      "org.scalactic" %% "scalactic"            % "3.0.0"
    ),
    dockerBaseImage := "anapsix/alpine-java:latest",
    dockerUpdateLatest := true,
    dockerExposedPorts := Seq(8081),
    dockerRepository := Some("iservport")
  )

// because we use Alpine
dockerCommands := dockerCommands.value.flatMap{
  case cmd@Cmd("FROM",_) => List(cmd, Cmd("RUN", "apk update && apk add bash"))
  case other => List(other)
}

lazy val kafka = (project in file("kafka")).
  enablePlugins(JavaServerAppPackaging, UniversalDeployPlugin)
  .settings(commonSettings)
  .settings(
    name := "helianto-spring-kafka",
    libraryDependencies ++= Seq(
      "org.springframework.kafka"       % "spring-kafka"          % "1.0.1.RELEASE"
    )
  )
  .dependsOn(root)

libraryDependencies ++= Seq(
  "org.webjars.bower" % "angular"              % "1.6.0",
  "org.webjars.bower" % "angular-sanitize"     % "1.6.0",
  "org.webjars.bower" % "angular-resource"     % "1.6.0",
  "org.webjars.bower" % "angular-animate"      % "1.6.0",
  "org.webjars.bower" % "angular-i18n"         % "1.6.0",
  "org.webjars.bower" % "angular-loading-bar"  % "0.9.0",
  "org.webjars.bower" % "slimScroll"           % "1.3.3"  exclude("org.webjars.bower", "jquery"),
  "org.webjars.bower" % "bootstrap"            % "3.3.7"  exclude("org.webjars.bower", "jquery"),
  "org.webjars.bower" % "jquery"               % "2.2.4",
  "org.webjars.bower" % "fontawesome"          % "4.6.3"
)

lazy val commonSettings = Seq(
  resolvers in ThisBuild ++= Seq(
    "Helianto Releases"  at "s3://maven.helianto.org/release",
    "Helianto Snapshots" at "s3://maven.helianto.org/snapshot",
    "Helianto Development" at "s3://maven.helianto.org/devel"
  ),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "org.mockito" % "mockito-all" % "1.10.19" % "test"
  ),
  publishTo in ThisBuild := {
    val helianto = "s3://maven.helianto.org/"
    if (version.value.trim.endsWith("SNAPSHOT"))
      Some("Helianto Snapshots" at helianto + "snapshot")
    else if (version.value.trim.endsWith("RELEASE"))
      Some("Helianto Releases" at helianto + "release")
    else
      Some("Helianto Development"  at helianto + "devel")
  },
  credentials += Credentials(Path.userHome / ".sbt" / ".s3credentials"),
  publishMavenStyle := true
)

licenses in ThisBuild := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))




