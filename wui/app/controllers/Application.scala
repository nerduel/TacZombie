package controllers

import scala.collection.mutable.ListBuffer
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Concurrent
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import play.api.mvc.Controller
import play.api.mvc.WebSocket
import play.api.mvc.Action
import taczombie.model.GameFactory

object Application extends Controller {

  //var myGame = GameFactory.newGame(random = false, file = "model/src/test/scala/taczombie/test/model/starWars-map")
  GameController.evaluateCommand("nextGame")
  
  def index = Action {
    Ok(views.html.index("TacZombie"))
  }
  
  val outchannels = 
    Map[String, ListBuffer[play.api.libs.iteratee.Concurrent.Channel[String]]](
        "lobby" -> new ListBuffer[play.api.libs.iteratee.Concurrent.Channel[String]]()
    )
  
  def broadcast = WebSocket.async[String] { request =>
  	
    concurrent.future {
      val (out, channel) = Concurrent.broadcast[String]

      println(request.id)
      
      outchannels.apply("lobby").append(channel)

      val in = Iteratee.foreach[String] { msg =>
        println("[DEBUG]: receiving message: " + msg)
        
        if (msg == "getGameData") {
        	channel.push(GameController.getCurrentGame)
        }
        else {
	        val jsonString = GameController.evaluateCommand(msg)
	        	
	        outchannels("lobby").foreach(_.push(jsonString))
        }
      }

      (in, out)
    }
  }
}
