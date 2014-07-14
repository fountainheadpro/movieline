package actions.movieline.web

import org.scalatra.{ScalatraServlet, FutureSupport, AsyncResult}
import _root_.akka.dispatch._
import _root_.akka.pattern.ask
import _root_.akka.actor.{ActorSystem, ActorRef}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.json._
import org.json4s.{DefaultFormats, Formats}
import org.slf4j.{Logger, LoggerFactory}
import org.scalatra.CorsSupport
import actions.movieline.actors.SearchProtocol._
import _root_.akka.util.Timeout
import org.json4s.jackson.Serialization._
import scala.concurrent.ExecutionContext
import org.json4s.FieldSerializer
import org.json4s.Serializer

class QuoteSearch(system:ActorSystem, searcher:ActorRef)  
  extends MovielineStack 
  with JacksonJsonSupport 
  with FutureSupport
  with CorsSupport 
{

  import _root_.akka.pattern.ask
  implicit val defaultTimeout = Timeout(1000)
  
  protected implicit val jsonFormats: Formats = DefaultFormats
  val logger =  LoggerFactory.getLogger(getClass)
  protected implicit def executor: ExecutionContext = system.dispatcher
  
  before() {
    contentType = formats("json")
  }
  
  get("/:query"){
    new AsyncResult { 
      val is= searcher ? SearchRequest(params("query"))
    }
  }
  
  options("/*"){
    response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"))
    response.setHeader("Access-Control-Allow-Methods", "GET");
  }

}
