import actions.movieline.web._
import org.scalatra._
import javax.servlet.ServletContext
import _root_.akka.actor.{ActorSystem, Props}
import actions.movieline.actors.SearchActor

class ScalatraBootstrap extends LifeCycle {
  
  val system = ActorSystem("movieline")
  val searcher = system.actorOf(Props[SearchActor], "searcher")
  
  override def init(context: ServletContext) {
    context.mount(new QuoteSearch(system, searcher), "/search/*")
  }
  
  override def destroy(context:ServletContext) {
    system.shutdown()
  }
  
  
}
