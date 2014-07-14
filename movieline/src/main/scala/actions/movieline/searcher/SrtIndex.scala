package actions.movieline.searcher

import org.apache.lucene.document.Document
import scala.collection.mutable.ArrayBuffer
import org.apache.lucene.document.TextField
import org.apache.lucene.document.IntField
import org.apache.lucene.document.Field
import scala.io.Source
import org.apache.lucene.store.FSDirectory
import java.io.File
import org.apache.lucene.util.Version
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.util.QueryBuilder
import java.io.Closeable
import org.apache.lucene.analysis.en.EnglishAnalyzer
import scalax.file.Path
import scalax.file.ImplicitConversions._
import org.apache.lucene.search.spell.SpellChecker
import org.apache.lucene.search.spell.PlainTextDictionary


class SrtIndex(srtFileName: String) extends Closeable{
  
  implicit class Regex(sc: StringContext) {
    def r = new scala.util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }
 	
  private def mkDocFromBlock(blockList: Iterator[String]): Document={
    val MILLS_PER_HOUR=60*60*1000
    val MILLS_PER_MIN=60*1000
    val MILLS_PER_SEC=1000        
    val doc=new Document
    var speech=ArrayBuffer[String]()
    var key=0
    blockList.foreach(line=>line match{
      case r"\A(\d+)${position}\z" => {
        key=position.toInt-1
        doc.add(new IntField("key",key, Field.Store.YES))        
        doc.add(new IntField("key",key, Field.Store.YES))        
      }
      case r"\A(\d\d)${hs}:(\d\d)${ms}:(\d\d)${ss},(\d\d\d)$mms --> (\d\d)${he}:(\d\d)${me}:(\d\d)${se},(\d\d\d)$mme" =>{
        val start  = hs.toInt*MILLS_PER_HOUR+ms.toInt*MILLS_PER_MIN+ss.toInt*MILLS_PER_SEC+mms.toInt
        val end    = he.toInt*MILLS_PER_HOUR+me.toInt*MILLS_PER_MIN+se.toInt*MILLS_PER_SEC+mme.toInt        
        val length = end-start        
	    doc.add(new IntField("start", start, Field.Store.YES))
	    doc.add(new IntField("end", end, Field.Store.YES))
	    doc.add(new IntField("length", length, Field.Store.YES))        
       }
       case speechLine:String => speech+=speechLine              
     })   
     doc.add(new TextField("content", speech.mkString(" "), Field.Store.YES))
     val numberOfScenes=(".." \ "animations" * s"*${key}*.gif").size
     doc.add(new IntField("numberOfScenes",numberOfScenes, Field.Store.YES))
     doc
  }
     
  val analyzer=new EnglishAnalyzer(Version.LUCENE_48)
  lazy val directory=FSDirectory.open(new File("./index"))
  lazy val indexReader =  DirectoryReader.open(directory)
  
  def buildIndex():Unit={
    val lines=Source.fromFile(srtFileName)(io.Codec("UTF-8")).getLines
    val indexWriter = new IndexWriter(directory,
                                    new IndexWriterConfig(Version.LUCENE_48, analyzer));
    try{
      indexWriter.deleteAll()
      while(lines.hasNext){
       val doc=mkDocFromBlock(lines.takeWhile(_.length>0))
       indexWriter.addDocument(doc)
      }
    }
    finally{
      indexWriter.close()
      createDictionary
    }
    
    
  }
    
  def searcher: IndexSearcher={   
    new IndexSearcher(indexReader)    
  }
  
  def queryBuilder = new QueryBuilder(analyzer);
  
  def close:Unit={
    indexReader.close
    directory.close
  }
  
  def foreach(f: (Document,Int)=>Unit)={
    for(i<-0 until indexReader.maxDoc()){
      val doc=indexReader.document(i)
      f(doc,i)
    }
  }
  
  val dictFileName=s"./dictionary/${Path.fromString(srtFileName).name}.dict"
  
  def createDictionary()={    
	/*  
    val path: Path = Path.fromString(dictFileName)
    path.createFile(failIfExists=false)

    val dict=SortedSet[String]()
    foreach((document: Document, i: Int)=>{
      val tokenStream=document.getField("content").tokenStream(analyzer)
      val offsetAttribute = tokenStream.getAttribute(classOf[OffsetAttribute])
      val termAttribute = tokenStream.getAttribute(classOf[CharTermAttribute])
      tokenStream.reset()
      while(tokenStream.incrementToken()){
        val startOffset = offsetAttribute.startOffset()
        val endOffset = offsetAttribute.endOffset()
        dict+=termAttribute.toString
      }
      tokenStream.close()
    })
    Resource.fromFile(dictFileName).writeStrings(dict, "\n")*/
    new SpellChecker(
      FSDirectory.open(new File("./dictionary/"))
      ).indexDictionary(
        new PlainTextDictionary(new File(srtFileName)),
        new IndexWriterConfig(Version.LUCENE_48,analyzer),
        true
      )   
  }
  
  val spellchecker=new SpellChecker(FSDirectory.open(new File("./dictionary/")))
  
  val animDirectory=s"../animation/"
      
    
}

object SrtIndex{
  
  def apply(srtFileName: String):SrtIndex={
    new SrtIndex(srtFileName)
  }
  
}  

