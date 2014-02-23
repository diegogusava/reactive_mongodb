import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.Play.current

package object dao {

  /** Returns the current instance of the driver. */
  private def driver = ReactiveMongoPlugin.driver
  /** Returns the current MongoConnection instance (the connection pool manager). */
  private def connection = ReactiveMongoPlugin.connection
  /** Returns the default database (as specified in `application.conf`). */
  private def db = ReactiveMongoPlugin.db

  private [dao] def userDB: JSONCollection = db.collection[JSONCollection]("user")

}
