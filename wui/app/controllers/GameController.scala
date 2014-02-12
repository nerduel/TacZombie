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
	      //currentGame.executeCommand(NextPlayer())
	      currentGame
	      
	    case "switchToken" =>
	      //currentGame.executeCommand(new switchToken())
	      currentGame
	      
	    case "nextGame" => 
	    	if (currentGame.gameState == GameState.Win)
	    	  // TODO: GameFactory needs to take old score 
	    	  // GameFactory.nextLevel(currentGame)
	    	  GameFactory.newGame(random = false, humans = 1, zombies = 1)
	    	else
	    	  // new Start
	    	  GameFactory.newGame(random = false, humans = 1, zombies = 1)
	  }
	}
	
	
	
	
}