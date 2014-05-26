package searcher

import org.apache.lucene.document.Document
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import scala.collection.mutable.Set
import scala.collection.mutable.SortedSet


object Searcher extends App{

  val index=SrtIndex("/Users/szelvenskiy/movieline/pulp_fiction.srt")
  index.buildIndex
    
  val searcher=index.searcher
  //val query = index.queryBuilder.createMinShouldMatchQuery("content", "That hit the spot", 0.9f);
  //val query = index.queryBuilder.createMinShouldMatchQuery("content", "zed is dead", 0.999f);  
  val query = index.queryBuilder.createPhraseQuery("content", "zed dead");
  val hits = searcher.search(query, null, 5).scoreDocs;
    
  hits.foreach(
    hit=>{
      println(searcher.doc(hit.doc))
      println(hit.score)
  })
  
  val suggestions=index.spellchecker.suggestSimilar("zod", 5)
  println(suggestions.mkString("\n"))
  index.close
    
}