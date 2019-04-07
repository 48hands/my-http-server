package com.example

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging

import scala.concurrent.duration._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.delete
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.directives.PathDirectives.path

import scala.concurrent.Future
import com.example.UserRegistryActor._
import akka.pattern.ask
import akka.util.Timeout

class MyAppException(message: String) extends Exception(message)

trait UserRoutes extends JsonSupport {
  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[UserRoutes])

  def userRegistryActor: ActorRef

  implicit lazy val timeout = Timeout(5.seconds)

  lazy val userRoutes: Route = {
    // 検証用に適当に入れた
    pathPrefix("logging") {
      val now = java.time.ZonedDateTime.now()
      concat(
        path("info") {
          get {
            log.info("意図的なInfoメッセージ")
            complete(s"This is Info message at $now")
          }
        },
        path("warn") {
          get {
            log.warning("意図的なWarnメッセージ")
            complete(s"This is Warning message at $now")
          }
        },
        path("error") {
          get {
            log.error(new MyAppException("意図的な例外エラー").getCause, "意図的なエラーメッセージ")
            complete(s"This is Error message at $now")
          }
        },
      )
    } ~
    pathPrefix("users") {
      concat(
        //#users-get-delete
        pathEnd {
          concat(
            get {
              val users: Future[Users] =
                (userRegistryActor ? GetUsers).mapTo[Users]
              complete(users)
            },
            post {
              entity(as[User]) { user =>
                val userCreated: Future[ActionPerformed] =
                  (userRegistryActor ? CreateUser(user)).mapTo[ActionPerformed]
                onSuccess(userCreated) { performed =>
                  log.info("Created user [{}]: {}", user.name, performed.description)
                  complete((StatusCodes.Created, performed))
                }
              }
            }
          )
        },
        path(Segment) { name =>
          concat(
            get {
              val maybeUser: Future[Option[User]] =
                (userRegistryActor ? GetUser(name)).mapTo[Option[User]]
              rejectEmptyResponse {
                complete(maybeUser)
              }
            },
            delete {
              val userDeleted: Future[ActionPerformed] =
                (userRegistryActor ? DeleteUser(name)).mapTo[ActionPerformed]
              onSuccess(userDeleted) { performed =>
                log.info("Deleted user [{}]: {}", name, performed.description)
                complete((StatusCodes.OK, performed))
              }
            }
          )
        }
      )
    }
  }
}
