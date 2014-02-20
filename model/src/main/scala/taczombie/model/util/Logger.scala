package taczombie.model.util

import scala.collection.mutable.ListBuffer

trait Logger {
  object logger {
    
    private var printOnGet : Boolean = false
    
    private val data : ListBuffer[String] = ListBuffer[String]()
  	
    def clear = data.clear()
    
  	def +=(s : String, print : Boolean = false) = {
  	  data += s
  	  if(print) println(s)
  	}
  	
  	def init(s : String, print : Boolean = false, printOnGet : Boolean = false) =  { 
  	  clear
  	  this.printOnGet = printOnGet
  	  +=(s, print)
  	}
  		
  	def get : List[String] = {
  	  if(printOnGet) print
  	  data.toList
  	}
  	
  	def merge(l : Logger) = {
  	  data.++=(l.logger.get) 
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