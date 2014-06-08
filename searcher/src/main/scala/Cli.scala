import api.Api
import web.{BootedCore, CoreActors}
import akka.pattern.ask
import searcher.actors.SearchProtocol._
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.dispatch.ExecutionContexts
import akka.dispatch.Futures
import scala.concurrent.Await

object Cli extends App with BootedCore with CoreActors with Api{
  
  implicit val timeout = Timeout(10 seconds)

  val rebuild=(searchActor ? Rebuild)
  val results=(searchActor ? SearchRequest("Zed dead")).mapTo[Clip]
  
  println(Await.result(rebuild, 20 seconds))
  println(Await.result(results, 10 seconds))
  
    
}