package cashpad

import scala.slick.driver.MySQLDriver.simple._
import scala.concurrent.Future
import scala.reflect.ClassTag

trait SlickDatabaseModule extends io.buildo.base.ConfigModule {
  case class DBConfig(
    host: String,
    port: Int,
    username: String,
    password: String,
    database: String)

  private val dbConfig = config.get { conf =>
    DBConfig(
      host = conf.getString(s"$projectName.db.host"),
      port = conf.getInt(s"$projectName.db.port"),
      username = conf.getString(s"$projectName.db.username"),
      password = conf.getString(s"$projectName.db.password"),
      database = conf.getString(s"$projectName.db.database")
    )
  }

  Class.forName("com.mysql.jdbc.Driver");

  private def dburl = s"jdbc:mysql://${dbConfig.host}:${dbConfig.port}/${dbConfig.database}"

  val db = Database.forURL(dburl, user = dbConfig.username, password = dbConfig.password)

  object Slick {
    private val globalExecutionContext = 
      scala.concurrent.ExecutionContext.global
    def withAsyncSession[T](fun: Session => T): Future[T] =
     Future(scala.concurrent.blocking(db.withSession(fun)))(globalExecutionContext)
    def withSession[T](fun: Session => T): T = db.withSession(fun)
    def withAsyncTransaction[T](fun: Session => T): Future[T] =
     Future(scala.concurrent.blocking(db.withTransaction(fun)))(globalExecutionContext)
    def withTransaction[T](fun: Session => T): T = db.withTransaction(fun)
  }
}


