package producer
//import ImagePHash
import scalax.file.Path
import scalax.file.PathMatcher
import scalax.file.ImplicitConversions.defaultPath2jfile
import java.io.FileInputStream
import scala.util.Try
import javax.imageio.ImageIO
import javax.imageio.stream.FileImageOutputStream
import java.io.File


object PHashPathOps{
  
 val pHash=new ImagePHash()
 
 implicit def pathToFileInputStream(p:Path)=new FileInputStream(p.path) 
     
 implicit class PHashPathOpsInner(p:Path){
   
   def apply[U](f: FileInputStream=>U)=Try(new FileInputStream(p.path))
     .map{in=>(in,Try(f(in)))}
	 .flatMap{case(in, res)=>in.close();res}

   def hash=apply {pHash.getHash(_)}
    
   def distance(other: Path)=for(from<-hash; to<-other.hash) yield pHash.distance(from, to)
   
   def asFile=new File(p.path)
   
 } 	   
}

object FilerOps{
  implicit class FilerOpsInner[A](in: Iterable[A]) {  
    def each(step: Int, offset: Int=0): Iterable[A] = {
      val out = collection.mutable.ListBuffer.empty[A]
      val it = in.iterator
      for (i <- 0 to in.size - 1) {
        if (i % step == offset) out += it.next
        else it.next
      }
      out
    }
  }
}


object Splitter extends App {
  
  import PHashPathOps._
  import FilerOps._
  
  val rootTmpPath: Path = Path.fromString("/Users/szelvenskiy/movieline/tmp")
  val currentSceneIndex=0
  val currentTmpPath=rootTmpPath / currentSceneIndex.toString
  val images=(currentTmpPath * "z\\d*.png".r)
  val distance = for(from<-images.headOption; to<-images.lastOption if from!=to) yield from.distance(to)
  val firstImage = ImageIO.read(images.head)
  val output =  new FileImageOutputStream((currentTmpPath / "output.gif").asFile)
  val writer = new GifSequenceWriter(output, firstImage.getType(), 291, true)
  //writer.writeToSequence(firstImage)
  for(image<-images.each(5)) {
    writer.writeToSequence(ImageIO.read(image.asFile))
  }
  writer.close()
  output.close()
  
}