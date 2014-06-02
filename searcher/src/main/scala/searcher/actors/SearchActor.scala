package searcher.actors

import akka.actor.Actor
import akka.actor.actorRef2Scala
import searcher.SrtIndex


object SearchProtocol{
  case class Rebuild
  case class SearchResults(urls:List[String])
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
	    hits.map{_.doc}
	  }
    }
    case r:Rebuild=>index.buildIndex    
  }
  
  override def postStop=index.close
  
}