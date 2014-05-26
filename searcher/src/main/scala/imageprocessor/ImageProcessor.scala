package imageprocessor

import scala.sys.process._
import searcher.SrtIndex
import org.apache.lucene.document.Document
import java.io.PrintWriter
import java.io.File

object ImageProcessor extends App{ 
  
  val file_name="pulp_fiction"
  val index=SrtIndex(s"/Users/szelvenskiy/movieline/$file_name.srt")
  val writer = new PrintWriter(new File("conversion.sh" ))
  index.foreach(
      (doc:Document, i: Int)=>{
        val tmpdir=s"mkdir ../tmp/$i"
        writer.println(tmpdir)
        //tmpdir.!
        
        val start=(doc.getField("start").numericValue().intValue()).toFloat/1000
        val length=doc.getField("length").numericValue().intValue().toFloat/1000        
        
        val splitCommand=s"ffmpeg -ss $start -i $file_name.mp4 -t $length -s 480x270  -f image2 ./tmp/$i/z%05d.png"
        writer.println(splitCommand)
        //println(Process(splitCommand, new java.io.File("/Users/szelvenskiy/movieline/")).!!)
        
        val gatherCommad=s"ruby src/split_images.rb $i"
        writer.println(gatherCommad)
        //Process(gatherCommad, new java.io.File("/Users/szelvenskiy/movieline/")).!
      }
  )
  writer.close()
  

}