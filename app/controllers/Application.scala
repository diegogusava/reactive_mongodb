package controllers

import play.api.mvc._
import dao.FactoryDAO
import play.api.libs.json.Json
import models.User
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.Crypto

object Application extends Controller {

  def userDB = FactoryDAO.userDB

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def list = Action.async {
    request =>
      userDB.findAll() map {
        case users: Seq[User] => Ok(Json.toJson(users))
      } recover {
        case ex: Throwable => errorMessage(ex.getMessage)
      }
  }

  def find(login: String) = Action.async {
    request =>
      userDB.find(login) map {
        case Some(user) => Ok(Json.toJson(user))
        case None => errorMessage("User not found")
      } recover {
        case ex: Throwable => errorMessage(ex.getMessage)
      }
  }

  def delete(login: String) = Action.async {
    request =>
      userDB.delete(login) map {
        case _ => Ok
      } recover {
        case ex: Throwable => errorMessage(ex.getMessage)
      }
  }

  def insert() = Action.async(parse.json) {
    request =>

      request.body.validate[User] map {
        case user: User => userDB.insert(user.copy(password = Crypto.sign(user.password))) map {
          case _ => Ok
        } recover {
          case ex: Throwable => errorMessage(ex.getMessage)
        }
      } recoverTotal {
        error => Future(BadRequest(Json.toJson(Map("error_message" -> "Invalid JSON"))))
      }

  }

  def update() = Action.async(parse.json) {
    request =>

      request.body.validate[User] map {
        case user: User => userDB.update(user) map {
          case _ => Ok
        } recover {
          case ex: Throwable => errorMessage(ex.getMessage)
        }
      } recoverTotal {
        error => Future(BadRequest(Json.toJson(Map("error_message" -> "Invalid JSON"))))
      }

  }

  def errorMessage(message: String): SimpleResult =
    InternalServerError(Json.toJson(Map("error_message" -> message)))

}