package searcher.actors

import akka.actor.Actor
import akka.actor.actorRef2Scala
import searcher.SrtIndex


object SearchProtocol{
  case object Rebuild
  case object RebuildComplete
  case class Scene(id: String, url: String, caption: String)
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
	    
	    sender ! Clip(hits.map{hit=>	      
	      val doc=index.searcher.doc(hit.doc)
	      Scene(doc.get("key"), linkFromDoc(doc.get("key")), doc.get("content"))
	    })
	  }
    }
    case Rebuild=>index.buildIndex; sender!RebuildComplete    
  }
  
  def linkFromDoc(key: String)=s"http://anim.livelin.es/animation_${key.toInt-1}_1.gif"
  
  override def postStop=index.close
  
}