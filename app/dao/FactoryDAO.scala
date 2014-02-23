package dao

import models.UserDAO

object FactoryDAO {

  def userDB:UserDAO = MongoUserDAO

}
