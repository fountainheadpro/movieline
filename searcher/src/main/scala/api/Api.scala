package api

import web.{CoreActors, Core}
import akka.actor.Props
import spray.routing.RouteConcatenation
import api.RoutedHttpService

/**
 * The REST API layer. It exposes the REST services, but does not provide any
 * web server interface.<br/>
 * Notice that it requires to be mixed in with ``core.CoreActors``, which provides access
 * to the top-level actors that make up the system.
 */
trait Api extends RouteConcatenation {
  this: CoreActors with Core =>

  private implicit val _ = system.dispatcher

  val routes = new SearchService(profileChanger).route 

  //val props=Props(new RoutedHttpService(routes))
  
  //val rootService = system.actorOf(props)

}
