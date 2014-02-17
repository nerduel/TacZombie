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
	
	implicit def gameWrapper(g : Game) =
	  new GameHelper(g)
	
	def evaluateCommand(cmd : String, currentGame : Game) : Game = {
	  cmd match {
	    case "moveLeft" =>
      	 currentGame.executeCommand(MoveLeft)
      	  
	    case "moveRight" =>
	      currentGame.executeCommand(MoveRight)
	      
	    case "moveUp" =>
	      currentGame.executeCommand(MoveUp)
	      
	    case "moveDown" =>
	      currentGame.executeCommand(MoveDown)
	      
	    case "nextPlayer" =>
	      currentGame.executeCommand(NextPlayer)
	      
	    case "switchToken" =>
	      currentGame.executeCommand(NextToken)
	      
	    case "restartGame" => {
	      if(lastGeneratedGame != null)
          lastGeneratedGame
        else currentGame
	    }
	      
	    case "respawnToken" =>
	      currentGame.executeCommand(RespawnToken)
	      
	    case "nextGame" => {
//	      	val nextGame = GameFactory.newGame(random = false, 
//	      	    file = "../model/src/test/scala/taczombie/test/model/TestLevel_correct")
	      	val nextGame = GameFactory.newGame(random = true)	      
	      	lastGeneratedGame = nextGame
	      	nextGame
	    }
	  }
	}
	
	
	
	
}