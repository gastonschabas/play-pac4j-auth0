ThisBuild / scalaVersion     := "2.13.4"
ThisBuild / organization     := "com.gaston"
ThisBuild / organizationName := "gaston-schabas"

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
    libraryDependencies += guice
  )
