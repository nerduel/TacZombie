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
      	  myGame = myGame.executeCommand(MoveLeft)
      	  myGame.toJson(Updated)
      	  
	    case "moveRight" =>
	      myGame = myGame.executeCommand(MoveRight)
	      myGame.toJson(Updated)
	      
	    case "moveUp" =>
	      myGame = myGame.executeCommand(MoveUp)
	      myGame.toJson(Updated)
	      
	    case "moveDown" =>
	      myGame = myGame.executeCommand(MoveDown)
	      myGame.toJson(Updated)
	      
	    case "nextPlayer" =>
	      myGame = myGame.executeCommand(NextPlayer)
	      myGame.toJson(Updated)
	      
	    case "switchToken" =>
	      myGame = myGame.executeCommand(NextToken)
	      myGame.toJson(Updated)
	      
	    case "restartGame" => {
	      if(lastGeneratedGame != null) {
	    	  myGame = lastGeneratedGame
	    	  myGame.toJson(All)
	      }
          else{
            myGame.toJson(All)
          } 
	    }
	      
	    case "respawnToken" =>
	      myGame.executeCommand(RespawnToken)
	      myGame.toJson(Updated)
	      
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