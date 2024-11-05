ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

val ceVersion = "3.5.5"
val enumeratumVersion = "1.7.5"
val pureConfigVersion = "0.17.7"
val sttpVersion = "3.10.1"
val circeVersion = "0.14.1"

val catsEffect = List(
  "org.typelevel" %% "cats-effect"
).map(_ % ceVersion)

val enumeratum = List(
  "com.beachape" %% "enumeratum",
  "com.beachape" %% "enumeratum-circe"
).map(_ % enumeratumVersion)

val pureConfig = List(
  "com.github.pureconfig" %% "pureconfig-core"
).map(_ % pureConfigVersion)

val sttp = List(
  "com.softwaremill.sttp.client3" %% "core",
  "com.softwaremill.sttp.client3" %% "cats",
  "com.softwaremill.sttp.client3" %% "circe"
).map(_ % sttpVersion)

val circe = List(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

lazy val root = (project in file("."))
  .settings(
    name := "prewave",
    libraryDependencies ++=
      catsEffect ++
        enumeratum ++
        pureConfig ++
        sttp ++
        circe
  )
