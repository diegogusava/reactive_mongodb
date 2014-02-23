package models

import org.joda.time.DateTime
import play.api.libs.json.Json
import scala.concurrent.Future

case class User(login: String,
                email:String,
                name: String,
                password: String,
                created: DateTime)

object User {
  implicit val format = Json.format[User]
}

trait UserDAO {

  def insert(user: User): Future[Boolean]

  def find(login: String): Future[Option[User]]

  def update(user: User): Future[Boolean]

  def delete(login: String): Future[Boolean]

  def findAll(): Future[Seq[User]]

}