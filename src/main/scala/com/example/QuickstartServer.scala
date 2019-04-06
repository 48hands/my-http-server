package com.example

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

object QuickstartServer extends App with UserRoutes {

  val config = ConfigFactory.load()
  private val port = config.getInt("server.port")

  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val userRegistryActor: ActorRef = system.actorOf(UserRegistryActor.props, "userRegistryActor")
  lazy val routes: Route = userRoutes
  Http().bindAndHandle(routes, "0.0.0.0", port)

  println(s"Server online at http://localhost:$port/")
  Await.result(system.whenTerminated, Duration.Inf)
}

