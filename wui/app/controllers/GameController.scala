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
import taczombie.model.Quit
import taczombie.model.Restart
import taczombie.model.util.LevelCreator

object GameController {

	val levelHeight = 21
	val levelWidth = 21
  
	class GameHelper(g : Game) {
	  def isSet = g != null
	}
	
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
	      
	    case "restartGame" =>
	      currentGame.executeCommand(Restart)
	      currentGame
	      
	    case "quit" =>
	      currentGame.executeCommand(Quit)
	      currentGame
	      
	    case "nextGame" => 
	    	if (currentGame.gameState == GameState.Win)
	    	  // TODO: GameFactory needs to take old score 
	    	  // GameFactory.nextLevel(currentGame)
	    	  GameFactory.newGame(random = false, file = "../model/src/test/scala/taczombie/test/model/TestLevel_correct", humans = 1, zombies = 1)
	    	else
	    	  // new Start
	    	  GameFactory.newGame(random = false, file = "../model/src/test/scala/taczombie/test/model/TestLevel_correct", humans = 1, zombies = 1)
	  }
	}
	
	
	
	
}