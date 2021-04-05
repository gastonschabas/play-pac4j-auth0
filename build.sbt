ThisBuild / scalaVersion     := "2.13.5"
ThisBuild / organization     := "com.gaston"
ThisBuild / organizationName := "gaston-schabas"

val playPac4jVersion = "10.0.2"
val pac4jVersion = "4.4.0"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)
  .settings(
    Defaults.itSettings,
    name    := "play-pac4j-auth0",
    version := "0.0.1",
    wartremoverErrors in (Compile, compile) ++= Warts.unsafe,
    wartremoverExcluded += baseDirectory.value / "target",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "it, test",
      "org.pac4j"              %% "play-pac4j"         % playPac4jVersion exclude ("com.fasterxml.jackson.core", "jackson-databind"),
      "org.pac4j"               % "pac4j-http"         % pac4jVersion exclude ("com.fasterxml.jackson.core", "jackson-databind"),
      "org.pac4j"               % "pac4j-jwt"          % pac4jVersion exclude ("com.fasterxml.jackson.core", "jackson-databind"),
      "org.apache.shiro"        % "shiro-core"         % "1.7.1"
    )
  )
