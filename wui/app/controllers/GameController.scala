package controllers

import play.api.libs.json.JsValue
import taczombie.model.Game
import taczombie.model.GameFactory
import taczombie.model.GameState
import taczombie.model.MoveDown
import taczombie.model.MoveLeft
import taczombie.model.MoveRight
import taczombie.model.MoveUp
import taczombie.model.NextPlayer
import taczombie.model.NextToken
import taczombie.model.util.LevelCreator
import scala.language.implicitConversions
import taczombie.model.RespawnToken

object GameController {

	class GameHelper(g : Game) {
	  def isSet = g != null
	}
	
	var lastGeneratedGame : Game = null
	var myGame : Game = null
	
	implicit def gameWrapper(g : Game) =
	  new GameHelper(g)
	
	import taczombie.model.util.JsonHelper._
	
	def evaluateCommand(cmd : String) : String = {
	  cmd match {
	    case "moveLeft" =>
      	 myGame.executeCommand(MoveLeft).toJson(Updated)
      	  
	    case "moveRight" =>
	      myGame.executeCommand(MoveRight).toJson(Updated)
	      
	    case "moveUp" =>
	      myGame.executeCommand(MoveUp).toJson(Updated)
	      
	    case "moveDown" =>
	      myGame.executeCommand(MoveDown).toJson(Updated)
	      
	    case "nextPlayer" =>
	      myGame.executeCommand(NextPlayer).toJson(Updated)
	      
	    case "switchToken" =>
	      myGame.executeCommand(NextToken).toJson(Updated)
	      
	    case "restartGame" => {
	      if(lastGeneratedGame != null)
	    	  lastGeneratedGame.toJson(All)
          else myGame.toJson(All)
	    }
	      
	    case "respawnToken" =>
	      myGame.executeCommand(RespawnToken).toJson(Updated)
	      
	    case "nextGame" => {
//	      	val nextGame = GameFactory.newGame(random = false, 
//	      	    file = "../model/src/test/scala/taczombie/test/model/TestLevel_correct")
	      	val nextGame = GameFactory.newGame(random = true)	      
	      	myGame = nextGame
	      	myGame.toJson(All)
	    }
	  }
	}
	
	def getCurrentGame : String = {
	  myGame.toJson(All)
	}
	
}