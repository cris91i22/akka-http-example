import akka.actor.{ActorSystem, DeadLetter}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.akka.http.api.routes.Routes
import com.akka.http.dl.storage.ExampleStorage
import com.akka.http.dl.{ExampleDB, TableQueries}
import com.akka.http.services.{DeadLettersCatcher, MessageServiceActor}
import com.akka.http.utils.ServicesTimeouts
import com.typesafe.config.ConfigFactory
import com.akka.http.dl.utils.ExtendedPostgresDriver.api._
import com.akka.http.model.User

object Main extends ServicesTimeouts with App with Routes {

  lazy val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  val httpInterface = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  implicit lazy val system = ActorSystem("AkkaHttpSystem", config)

  lazy val log = Logging(system, getClass)
  implicit lazy val executor = system.dispatcher
  implicit lazy val materializer = ActorMaterializer()

  log.info("ACAAAAAAAAAAAAAAAAAAaaa")
  // Database
  val db = new ExampleDB(config)
  db.instance.run(DBIO.seq(TableQueries.createActions(): _*))
  val storage = new ExampleStorage(db.instance, TableQueries)
  storage.users.create(User(None, "cris", "bla"))
  log.info("ACAAAAAAAAAAAAAAAAAAaaa2222222222222")
  // Services
  val messageService = system.actorOf(MessageServiceActor.props, name = "messageServiceActor")

  // Dead letters catcher
  val deadLettersCatcher = system.actorOf(DeadLettersCatcher.props, "dead-letters-catcher")
  system.eventStream.subscribe(deadLettersCatcher, classOf[DeadLetter])

  // Server
  Http().bindAndHandle(handler = logResult("log")(routes), interface = httpInterface, port = httpPort)

}
