package controllers

import scala.collection.mutable.ListBuffer
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Concurrent
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import play.api.mvc.Controller
import play.api.mvc.WebSocket
import play.api.mvc.Action

object Application extends Controller {

	def index = Action {
		Ok(views.html.index("Your new application is ready."))
	}

//	def updateView = Action {
//	  Websocket.updateTest
//		Ok(views.html.index("Your new application is ready."))
//	}
//
//			
//	def ws = WebSocket.using[String] { request =>
//	  	val (out,channel) = Concurrent.broadcast[String]
//			Websocket.join(out, channel)
//	}
	
	
	
	val outchannels = Map[String, 
                        ListBuffer[play.api.libs.iteratee.Concurrent.Channel[String]]
                       ]("lobby" -> new ListBuffer[play.api.libs.iteratee.Concurrent.Channel[String]]())
                       
  def broadcast =  WebSocket.async[String] { request =>
    
    concurrent.future {
      val (out,channel) = Concurrent.broadcast[String]
      
      println(request.id)
      
      outchannels.apply("lobby").append(channel)
      
      val in = Iteratee.foreach[String] { msg => 
        println("receiving message: " + msg)
        channel.push("your id: " + request.id + ", sent: " + msg)           
      }
      
      (in, out)
    }
  }
}
