package taczombie.test.wui

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import taczombie.model.util.JsonHelper._
import controllers.GameController
import spray.json.DefaultJsonProtocol
import spray.json._

class GameControllerSpec extends Specification {
	sequential
	
	private def gameControllerWithGame = {
	  GameController.evaluateCommand("newGame")
	}
	
	private def gameControllerWithNoGame = {
	  GameController.myGame = null
	  GameController.lastGeneratedGame = null
	}
  
	import taczombie.model.util.JsonHelper.ErrorJsonProtocol._
	import taczombie.model.util.JsonHelper.GameDataJsonProtocol._
	
	"GameController without an existing game" should {
	  
	  "send an error on command 'moveLeft' " in {
	    gameControllerWithNoGame
	    val retVal = GameController.evaluateCommand("moveLeft").asJson
	    val error : Error = retVal.convertTo[Error]
	    error.cmd must be_==("error")
	  }
	  
	  "send an error on command 'moveRight' " in {
	    gameControllerWithNoGame
	    val retVal = GameController.evaluateCommand("moveRight").asJson
	    val error : Error = retVal.convertTo[Error]
	    error.cmd must be_==("error")
	  }
	  
	  "send an error on command 'moveUp' " in {
	    gameControllerWithNoGame
	    val retVal = GameController.evaluateCommand("moveUp").asJson
	    val error : Error = retVal.convertTo[Error]
	    error.cmd must be_==("error")	    
	  }
	  
	  "send an error on command 'moveDown' " in {
	    gameControllerWithNoGame
	    val retVal = GameController.evaluateCommand("moveDown").asJson
	    val error : Error = retVal.convertTo[Error]
	    error.cmd must be_==("error")	    
	  }
	  
	  "send an error on command 'nextPlayer' " in {
	    gameControllerWithNoGame
	    val retVal = GameController.evaluateCommand("nextPlayer").asJson
	    val error : Error = retVal.convertTo[Error]
	    error.cmd must be_==("error")	    
	  }
	  
	  "send an error on command 'switchToken' " in {
	    gameControllerWithNoGame
	    val retVal = GameController.evaluateCommand("switchToken").asJson
	    val error : Error = retVal.convertTo[Error]
	    error.cmd must be_==("error")	    
	  }
	  
	  "send an error on command 'restartGame' " in {
	    gameControllerWithNoGame
	    val retVal = GameController.evaluateCommand("restartGame").asJson
	    val error : Error = retVal.convertTo[Error]
	    error.cmd must be_==("error")	    
	  }
	  
	  "send an error on command 'respawnToken' " in {
	    gameControllerWithNoGame
	    val retVal = GameController.evaluateCommand("respawnToken").asJson
	    val error : Error = retVal.convertTo[Error]
	    error.cmd must be_==("error")	    
	  }
	}
	
	"GameController with existing game" should {

	  "return the updated game data as Json string on Command 'moveLeft'" in {
	    gameControllerWithGame
		val retVal = GameController.evaluateCommand("moveLeft").asJson 
		retVal must be_!=("") 
		retVal.convertTo[Data].cmd must be_==("updated")
	  }
	  
	  "return the updated game data as Json string on Command 'moveRight'" in {
	    gameControllerWithGame
		val retVal = GameController.evaluateCommand("moveRight").asJson 
		retVal must be_!=("")
		retVal.convertTo[Data].cmd must be_==("updated")
	  }
	  
	  "return the updated game data as Json string on Command 'moveUp'" in {
	    gameControllerWithGame
		val retVal = GameController.evaluateCommand("moveUp").asJson 
		retVal must be_!=("") 
		retVal.convertTo[Data].cmd must be_==("updated")
	  }
	  
	  "return the updated game data as Json string on Command 'moveDown'" in {
	    gameControllerWithGame
		val retVal = GameController.evaluateCommand("moveDown").asJson 
		retVal must be_!=("") 
		retVal.convertTo[Data].cmd must be_==("updated")
	  }
	  
	  "return the updated game data as Json string on Command 'nextPlayer'" in {
	    gameControllerWithGame
		val retVal = GameController.evaluateCommand("nextPlayer").asJson 
		retVal must be_!=("") 
		retVal.convertTo[Data].cmd must be_==("updated")
	  }
	  
	  "return the updated game data as Json string on Command 'switchToken'" in {
	    gameControllerWithGame
		val retVal = GameController.evaluateCommand("switchToken").asJson 
		retVal must be_!=("") 
		retVal.convertTo[Data].cmd must be_==("updated")
	  }
	  
	  "return the updated game data as Json string on Command 'restartGame'" in {
	    gameControllerWithGame
		val retVal = GameController.evaluateCommand("restartGame").asJson 
		GameController.myGame must be_!=(null)
	    GameController.myGame must be_==(GameController.lastGeneratedGame)
		retVal must be_!=("") 
		retVal.convertTo[Data].cmd must be_==("all")
	  }
	
	  "return the updated game data as Json string on Command 'respawnToken'" in {
	    gameControllerWithGame
		val retVal = GameController.evaluateCommand("respawnToken").asJson
		retVal must be_!=("")
		retVal.convertTo[Data].cmd must be_==("updated")
	  }
	  
	  "return the updated game data as Json string on Command 'newGame'" in {
	    gameControllerWithGame
		val retVal = GameController.evaluateCommand("newGame").asJson 
		GameController.myGame must be_!=(null)
	    GameController.myGame must be_==(GameController.lastGeneratedGame)
		retVal must be_!=("") 
		retVal.convertTo[Data].cmd must be_==("all")
	  }  
	}
	
	"GameController" should {
	  
	  "return an error message when it receives a wrong command" in {
	    gameControllerWithGame
	    val retVal = GameController.evaluateCommand("FOO").asJson
	    retVal.convertTo[Error].cmd must be_==("error") 
	  }
	  
	  "create a new level and send level data as Json String on command 'newGame'" in {
	    gameControllerWithGame
	    val retVal = GameController.evaluateCommand("newGame").asJson
	    val data : Data = retVal.convertTo[Data]
	    GameController.myGame must be_!=(null)
	    GameController.myGame must be_==(GameController.lastGeneratedGame)
	    data.cmd must be_==("all")	    
	  }
	}
	
	"GameController" can {
	  "return current game as Json string" in {
	    val retVal = GameController.getCurrentGame.asJson
	    retVal.convertTo[Data].cmd must be_==("all")
	  } 
	}
}