lazy val akkaHttpVersion = "10.0.11"
lazy val akkaVersion = "2.5.11"

val AIRFRAME_VERSION = "19.2.0"

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.4"
    )),
    name := "AkkaHttpPlayground",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe" % "config" % "1.3.1",

      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.1" % Test
    )
  )

lazy val createImage =
  root.enablePlugins(JavaAppPackaging, DockerPlugin)
    .settings(
      // Dockerのimageにパッケージング化するための設定
      version in Docker := "1.0", // imageのバージョン
      packageName in Docker := "my-http-server", // imageの名前
      dockerBaseImage := "java:openjdk-8-jdk", // image生成元のimage
      dockerRepository := Some("yuichi.nagakura"), // レジストリ名
      dockerExposedPorts := Seq(80, 8080) // 公開するport
    )

