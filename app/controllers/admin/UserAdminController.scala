package controllers.admin

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import dao.FactoryDAO
import play.api.data.Form
import play.api.data.Forms._
import models.User
import org.joda.time.DateTime
import play.api.libs.Crypto
import play.api.i18n.Messages
import controllers._
import controllers.routes
import scala.Some
import play.api.mvc.SimpleResult

object UserAdminController extends Controller {

  def dao = FactoryDAO.userDB

  val Home: SimpleResult = Redirect(routes.UserAdminController.index())

  val elementForm: Form[User] = Form(
    mapping(
      "login" -> nonEmptyText,
      "name" -> nonEmptyText,
      "email" -> email,
      "password" -> text,
      "created" -> ignored(DateTime.now()),
      "active" -> boolean
    )(User.apply)(User.unapply)
  )

  def index() = Action.async {
    implicit request =>
      dao.findAll() map {
        elements =>
          Ok(views.html.admin.user.list(elements))
      } recover {
        case ex: Throwable => BadRequest
      }
  }

  def create() = Action.async {
    implicit request =>
      Future(Ok(views.html.admin.user.edit(None, elementForm)))
  }

  def insert() = Action.async {
    implicit request =>
      elementForm.bindFromRequest.fold(
        errors => {
          Future(BadRequest(views.html.admin.user.edit(None, errors)).flashing(ErrorMessage))
        },
        element => {
          if (element.password.isEmpty) {
            val form = elementForm.fill(element).withGlobalError(Messages("password_is_required"))
            Future(BadRequest(views.html.admin.user.edit(None, form)))
          } else {
            dao.insert(element.copy(password = Crypto.sign(element.password)))
            Future(Home.flashing(SuccessMessage))
          }
        }
      )
  }

  def edit(login: String) = Action.async {
    implicit request =>
      dao.find(login) map {
        case Some(element) => Ok(views.html.admin.user.edit(Some(login), elementForm.fill(element.copy(password = ""))))
        case None => BadRequest
      } recover {
        case ex: Throwable => BadRequest
      }
  }

  def update(login: String) = Action.async {
    implicit request =>

      def newCopy(element: User, found: User): User = {
        if (element.password.isEmpty) {
          found.copy(login = element.login, name = element.name, email = element.email, active = element.active)
        } else {
          found.copy(login = element.login, name = element.name, email = element.email, active = element.active, password = Crypto.sign(element.password))
        }
      }

      elementForm.bindFromRequest.fold(
        errors => {
          Future(BadRequest(views.html.admin.user.edit(Some(login), errors)))
        },
        element => {

          val result: Future[Boolean] = for {
            found <- dao.find(login)
            userUpdated = newCopy(element, found.get)
            result <- dao.update(login, userUpdated)
          } yield result

          result map {
            case true => Home.flashing(SuccessMessage)
            case false => Home.flashing(ErrorMessage)
          } recover {
            case ex: Throwable => BadRequest(views.html.admin.user.edit(Some(login), elementForm.fill(element).withGlobalError(ErrorMessage._2)))
          }

        }
      )
  }

  def delete(login: String) = Action.async {
    request =>
      dao.delete(login) map {
        case true => Home.flashing(SuccessMessage)
        case false => Home.flashing(NotFoundMessage)
      } recover {
        case ex: Throwable => BadRequest
      }
  }

}
