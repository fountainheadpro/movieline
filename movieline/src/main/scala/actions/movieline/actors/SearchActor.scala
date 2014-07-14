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
      val words=searchString.split(" ")
      val searcher=index.searcher
      if (words.size>=3){
	    val suggestions=index.spellchecker.suggestSimilar(searchString, 5)
	    val praseQ: Option[String]=suggestions.headOption	    
	    if (praseQ.isDefined){ 	      
	      val query = index.queryBuilder.createPhraseQuery("content", praseQ.get);
	      val hits = searcher.search(query, null, 5).scoreDocs;
	      val result = hits.flatMap{hit=>	      
	        val doc=index.searcher.doc(hit.doc)
	        scenesFromDoc(doc)
	      }
	      sender ! result
		}}
	    else{
	      val query = index.queryBuilder.createPhraseQuery("content", searchString);
	      val hits = searcher.search(query, null, 100).scoreDocs
	      val result = hits.flatMap{hit=>	      
	        val doc=index.searcher.doc(hit.doc)
	        scenesFromDoc(doc)
	      }
	      sender ! result
	    }	    
    }
    case Rebuild=>index.buildIndex; sender!RebuildComplete    
  }
  
  def scenesFromDoc(doc: Document)=
    for(frame<-1 to doc.getField("numberOfScenes").numericValue().intValue()) 
     yield Scene(s"${doc.get("key")}_$frame", Seq(s"http://anim.livelin.es/animation_${doc.get("key")}_$frame.gif"), doc.get("content")) 
  
  override def postStop=index.close
  
}