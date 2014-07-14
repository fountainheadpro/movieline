package actions.movieline.actors

import akka.actor.Actor
import akka.actor.actorRef2Scala
import org.apache.lucene.document.Document
import scala.Array.fallbackCanBuildFrom
import actions.movieline.searcher.SrtIndex


object SearchProtocol{
  case object Rebuild
  case object RebuildComplete
  case class Scene(id: String, urls: Seq[String], caption: String)
  case class Clip(scenes:Seq[Scene])
  case class SearchRequest(searchString:String)
}

class SearchActor extends Actor{
  import context._
  import SearchProtocol._
  
  val index=SrtIndex("/Users/szelvenskiy/movieline/pulp_fiction.srt")
  
  def receive: Receive={
    case SearchRequest(searchString)=>{	  
	  //index.buildIndex
	  val suggestions=index.spellchecker.suggestSimilar(searchString, 5)
	  val praseQ: Option[String]=suggestions.headOption	  
	  
	  if (praseQ.isDefined){ 
	    val searcher=index.searcher
	    val query = index.queryBuilder.createPhraseQuery("content", praseQ.get);
	    val hits = searcher.search(query, null, 5).scoreDocs;
	    val result = hits.map{hit=>	      
	      val doc=index.searcher.doc(hit.doc)
	      Scene(doc.get("key"), linksFromDoc(doc), doc.get("content"))
	    }
	    sender ! result
	  }
    }
    case Rebuild=>index.buildIndex; sender!RebuildComplete    
  }
  
  def linksFromDoc(doc: Document)=
    for(scene<-1 to doc.getField("numberOfScenes").numericValue().intValue()) 
    yield s"http://anim.livelin.es/animation_${doc.get("key")}_$scene.gif"
  
  override def postStop=index.close
  
}