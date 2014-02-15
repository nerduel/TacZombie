package controllers

import play.api.libs.json.JsValue
import taczombie.model.Game
import taczombie.model.util.LevelCreator
import taczombie.model.GameFactory
import taczombie.model.MoveUp
import taczombie.model.MoveDown
import taczombie.model.MoveRight
import taczombie.model.MoveLeft
import taczombie.model.GameState
import taczombie.model.NextToken
import taczombie.model.NextPlayer
import taczombie.model.Restart
import com.sun.xml.internal.bind.v2.model.annotation.Quick
import taczombie.model.Quit

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
      	 currentGame.executeCommand(MoveLeft)._1
      	  
	    case "moveRight" =>
	      currentGame.executeCommand(MoveRight)._1
	      
	    case "moveUp" =>
	      currentGame.executeCommand(MoveUp)._1
	      
	    case "moveDown" =>
	      currentGame.executeCommand(MoveDown)._1
	      
	    case "nextPlayer" =>
	      currentGame.executeCommand(NextPlayer)._1
	      
	    case "switchToken" =>
	      currentGame.executeCommand(NextToken)._1
	      
	    case "restartGame" =>
	      currentGame.executeCommand(Restart)._1
	      currentGame
	      
	    case "quit" =>
	      currentGame.executeCommand(Quit)._1
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