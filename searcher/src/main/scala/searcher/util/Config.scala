package searcher.util

import java.net.URI

/**
 * This class represents our app configuration.
 */
case class AnalyticsConfig(dbURL: String, username: String, password: String, port: Option[Int]){
    
  override def toString:String=dbURL 
}

object AnalyticsConfig {
  
    val C=this()
  
    def apply(): AnalyticsConfig = {
        val dbUri = new URI(System.getenv("DATABASE_URL"))
        val username = dbUri.getUserInfo().split(":")(0)
        val password = dbUri.getUserInfo().split(":")(1)
        val dbURL = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()        
        val port = Option(System.getenv("PORT")) map { s => Integer.parseInt(s) }
        val config = AnalyticsConfig(dbURL, username, password, port)
        println("Configuration is: " + config)
        config
    }
    
    
}