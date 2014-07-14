package actions.movieline.searcher

import actions.movieline.searcher.SrtIndex

object Searcher extends App{

  val index=SrtIndex("/Users/szelvenskiy/movieline/pulp_fiction.srt")
  index.buildIndex
  /*val suggestions=index.spellchecker.suggestSimilar("Yack", 5)
  val praseQ: Option[String]=suggestions.headOption
  
  if (praseQ.isDefined){ 
    val searcher=index.searcher
    val query = index.queryBuilder.createPhraseQuery("content", praseQ.get);
    val hits = searcher.search(query, null, 5).scoreDocs;
    
    hits.foreach(
      hit=>{
        println(searcher.doc(hit.doc))
        println(hit.score)
    })
  }*/
  index.close
    
}