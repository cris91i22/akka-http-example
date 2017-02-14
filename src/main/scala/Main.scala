import akka.actor.{ActorSystem, DeadLetter}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.akka.http.api.routes.Routes
import com.akka.http.services.{DeadLettersCatcher, MessageServiceActor}
import com.akka.http.utils.ServicesTimeouts
import com.typesafe.config.ConfigFactory

object Main extends ServicesTimeouts with App with Routes {

  lazy val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  val httpInterface = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  implicit lazy val system = ActorSystem("AkkaHttpSystem", config)

  lazy val log = Logging(system, getClass)
  implicit lazy val executor = system.dispatcher
  implicit lazy val materializer = ActorMaterializer()

  // Services
  val messageService = system.actorOf(MessageServiceActor.props, name = "messageServiceActor")

  // Dead letters catcher
  val deadLettersCatcher = system.actorOf(DeadLettersCatcher.props, "dead-letters-catcher")
  system.eventStream.subscribe(deadLettersCatcher, classOf[DeadLetter])

  // Server
  Http().bindAndHandle(handler = logResult("log")(routes), interface = httpInterface, port = httpPort)

}
