package taczombie.model.util

import scala.collection.mutable.ListBuffer

trait Logger {
  object logger {
    
    private var printlog : Boolean = false
    
    private val data : ListBuffer[String] = ListBuffer[String]()
  	
    def clear = data.clear()
    
  	def +=(s : String, print : Boolean = false) = {
  	  data += s
  	  if(print || printlog) println(s)
  	}
  	
  	def init(s : String, print : Boolean = false) =  { 
  	  clear
  	  printlog = print
  	  +=(s, print)
  	}
  		
  	def get : List[String] = data.toList
  	
  	def merge(l : Logger) = {
  	  data.++=(l.logger.get)
  	  if(printlog) print 
  	  l
  	}
  	
  	def print = {
  	  if(data.size > 0) {
  	    println(data.apply(0))
    	  for(s <- data.tail)
    	  	println("\t" + s)
  	  } else  println("Empty logger")
  	}
  }
}