package api

import spray.routing.Directives
import scala.concurrent.ExecutionContext
import akka.actor.ActorRef
import akka.util.Timeout
import spray.http.{StatusCodes, StatusCode}
import api.DefaultJsonFormats
import spray.http._
import spray.routing.HttpService
import spray.routing.Route
import spray.http.HttpHeader
import spray.http.HttpHeaders.RawHeader
import spray.util.LoggingContext
import akka.event.Logging
import spray.routing.ExceptionHandler
import spray.routing.PathMatchers._
import sun.misc.BASE64Decoder;
import searcher.actors.SearchProtocol._




class SearchService(searchActor: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats{
  
  import akka.pattern.ask
  import scala.concurrent.duration._
  implicit val timeout = Timeout(10.seconds)
  
  //val log = Logging(context.system, this)
  val  decoder = new BASE64Decoder();

    
  val route =
    path("search"){
      get{
        respondWithHeaders(
          RawHeader("Access-Control-Allow-Origin", "*"),
          RawHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS"),
          RawHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type, X-Prototype-Version")
          ){parameters('encodedString){(encodedString:String)=>            
    	      complete { 	    	      
    	        (searchActor ? SearchRequest(new String(decoder.decodeBuffer(encodedString)))).mapTo[SearchResults] 
    	      }
            }          
          }    
      }    
  }

}  