package models

import org.joda.time.DateTime
import play.api.libs.json.Json
import scala.concurrent.Future
import java.util.Date

case class User(login: String,
                name: String,
                email: String,
                password: String,
                created: DateTime,
                active:Boolean)

object User {
  implicit val format = Json.format[User]
}

trait UserDAO {

  def insert(user: User): Future[Boolean]

  def find(login: String): Future[Option[User]]

  def update(login:String, user: User): Future[Boolean]

  def delete(login: String): Future[Boolean]

  def findAll(): Future[Seq[User]]

}