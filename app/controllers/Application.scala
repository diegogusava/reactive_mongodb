package controllers

import play.api.mvc._
import dao.FactoryDAO
import play.api.libs.json.Json
import models.User
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.Crypto

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}