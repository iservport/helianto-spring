import sbt.Keys._

val heliantoSpringBootVersion = settingKey[String]("Spring Boot version")

heliantoSpringBootVersion in ThisBuild := "1.4.0.RELEASE"

organization in ThisBuild := "org.helianto"

version in ThisBuild := "1.0.16.DEV"

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
    mainClass in (Compile, run) := Some("org.helianto.Application"),
    dockerBaseImage := "azul/zulu-openjdk:8",
    dockerUpdateLatest := true,
    dockerExposedPorts := Seq(8080),
    dockerRepository := Some("helianto")
  )
  .dependsOn(user, security)
  .aggregate(core,user,security,kafka)

lazy val core = (project in file("core"))
  .enablePlugins(JavaServerAppPackaging, UniversalDeployPlugin)
  .settings(commonSettings)
  .settings(
    name := "helianto-spring-core",
    libraryDependencies ++= Seq(
      "org.projectlombok"          % "lombok"                           % "1.16.8",
      "org.springframework.boot"   % "spring-boot-starter-web"          % heliantoSpringBootVersion.value,
      "org.springframework.boot"   % "spring-boot-starter-data-jpa"     % heliantoSpringBootVersion.value,
      "org.springframework.boot"   % "spring-boot-starter-test"         % heliantoSpringBootVersion.value % "test",
      "org.springframework.boot"   % "spring-boot-test-autoconfigure"   % heliantoSpringBootVersion.value % "test",
      "com.zaxxer" % "HikariCP" % "2.4.3",
      "org.scalactic" %% "scalactic" % "3.0.0"
    )
  )

lazy val user = (project in file("user"))
  .enablePlugins(JavaServerAppPackaging, UniversalDeployPlugin)
  .settings(commonSettings)
  .settings(
    name := "helianto-spring-user"
  )
  .dependsOn(core)

lazy val security = (project in file("security")).
  enablePlugins(JavaServerAppPackaging, UniversalDeployPlugin)
  .settings(commonSettings)
  .settings(
    name := "helianto-spring-security",
      libraryDependencies ++= Seq(
        "org.springframework.boot"           % "spring-boot-starter-security" % heliantoSpringBootVersion.value,
        "org.springframework.security.oauth" % "spring-security-oauth2"       % "2.0.11.RELEASE",
        "org.springframework.security"       % "spring-security-jwt"          % "1.0.5.RELEASE"
      )
  )
  .dependsOn(user)

lazy val kafka = (project in file("kafka")).
  enablePlugins(JavaServerAppPackaging, UniversalDeployPlugin)
  .settings(commonSettings)
  .settings(
    name := "helianto-spring-kafka",
    libraryDependencies ++= Seq(
      "org.springframework.kafka"       % "spring-kafka"          % "1.0.1.RELEASE"
    )
  )
  .dependsOn(user)

libraryDependencies ++= Seq(
  "org.springframework.boot"   % "spring-boot-starter-freemarker"   % heliantoSpringBootVersion.value,
  "org.springframework.boot"   % "spring-boot-starter-actuator"     % heliantoSpringBootVersion.value,
  "javax.servlet"              % "javax.servlet-api"  % "3.0.1"     % "provided",
  "commons-io"                 % "commons-io"         % "2.4",
  "mysql" % "mysql-connector-java" % "5.1.26"
)

libraryDependencies ++= Seq(
  "org.webjars.bower" % "angular"              % "1.5.8",
  "org.webjars.bower" % "angular-sanitize"     % "1.5.8",
  "org.webjars.bower" % "angular-resource"     % "1.5.8",
  "org.webjars.bower" % "angular-animate"      % "1.5.8",
  "org.webjars.bower" % "angular-i18n"         % "1.5.8",
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
      Some("Helianto Snapshots" at helianto + "release")
    else
      Some("Helianto Development"  at helianto + "devel")
  },
  credentials += Credentials(Path.userHome / ".sbt" / ".s3credentials"),
  publishMavenStyle := true
)

licenses in ThisBuild := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))




