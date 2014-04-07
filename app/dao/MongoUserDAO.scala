package dao

import models.{User, UserDAO}
import scala.concurrent.Future
import reactivemongo.bson.BSONDocument
import play.modules.reactivemongo.json.BSONFormats._
import reactivemongo.core.commands.LastError
import scala.concurrent.ExecutionContext.Implicits.global

object MongoUserDAO extends UserDAO {

  override def delete(login: String): Future[Boolean] = {
    val query = BSONDocument("login" -> login)
    userDB.remove(query).map {
      error: LastError => error.ok
    }
  }

  override def update(login: String, user: User): Future[Boolean] = {
    val query = BSONDocument("login" -> login)
    userDB.update(query, user).map {
      error: LastError => error.ok
    }
  }

  override def find(login: String): Future[Option[User]] = {
    val query = BSONDocument("login" -> login)
    userDB.find(query).one[User]
  }

  override def insert(user: User): Future[Boolean] = {
    userDB.insert(user).map {
      error: LastError => error.ok
    }
  }

  override def findAll(): Future[Seq[User]] = {
    userDB.find(BSONDocument()).cursor[User].collect[Seq]()
  }

}
