import akka.actor.{ActorSystem, DeadLetter}
import akka.http.scaladsl.Http
import akka.routing.FromConfig
import akka.stream.ActorMaterializer
import com.akka.http.api.routes.Routes
import com.akka.http.dl.storage.ExampleStorage
import com.akka.http.dl.utils.ExtendedPostgresDriver.api._
import com.akka.http.dl.{ExampleDB, TableQueries}
import com.akka.http.services.{DeadLettersCatcher, MessageServiceActor, UserServiceActor}
import com.akka.http.utils.ServicesTimeouts
import com.typesafe.config.ConfigFactory

object Main extends ServicesTimeouts with App with Routes {

  lazy val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  val httpInterface = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  implicit lazy val system = ActorSystem("AkkaHttpSystem", config)
  implicit lazy val executor = system.dispatcher
  implicit lazy val materializer = ActorMaterializer()

  /**
    * Start database with docker
    * docker run --name some-postgres -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=local -d -p 5432:5432 postgres
    * psql -h public-ip-server -p 5432 -U postgres
    */
  // Database
  val db = new ExampleDB(config)
  db.instance.run(DBIO.seq(TableQueries.createActions(): _*))
  val storage = new ExampleStorage(db.instance, TableQueries)


  // Services
  val messageService = system.actorOf(FromConfig.props(MessageServiceActor.props(storage)), name = "message-service")
  val userService = system.actorOf(FromConfig.props(UserServiceActor.props(storage)), name = "user-service")

  // Dead letters catcher
  val deadLettersCatcher = system.actorOf(DeadLettersCatcher.props, "dead-letters-catcher")
  system.eventStream.subscribe(deadLettersCatcher, classOf[DeadLetter])

  // Server
  Http().bindAndHandle(handler = logResult("log")(routes), interface = httpInterface, port = httpPort)

}
