package taczombie.test.model

import org.specs2.mutable.Specification
import taczombie.model.GameFactory
import taczombie.model.GameState
import taczombie.model.MoveUp
import taczombie.model.NextPlayer
import taczombie.model.Human
import taczombie.model.Zombie
import taczombie.model.MoveDown
import taczombie.model.defaults
import taczombie.model.MoveLeft
import taczombie.model.MoveRight
import taczombie.model.NextToken
import taczombie.model.RespawnToken

/**
 * Tests the game as if it was controlled by the Controller
 */
class GameSpec extends Specification {
  sequential
  
  "A game on the gametest Level" should {
      val testGame = GameFactory.newGame(random = false, 
      		file = TestObjects.testfile_gametest.getFile(), humans = 2, zombies = 3)

    "allow the human to start and move up until he wins" in {
      testGame.players.currentPlayer.isInstanceOf[Human] must be_==(true)
      
      var updatedGame = testGame
      var counter = 0
      while(counter < 100 && updatedGame.gameState == GameState.InGame) {
        counter += 1
        updatedGame = updatedGame.executeCommand(MoveUp)
      }
      updatedGame.gameState must be_==(GameState.Win) 		
    }    
    
    "dont reduce moves remaining when walking against a wall" in {
      var updatedGame = testGame
      updatedGame = updatedGame.executeCommand(MoveDown)
      												 .executeCommand(MoveLeft)
      												 .executeCommand(MoveRight)
      
      testGame.players.currentPlayer.movesRemaining must 
      	be_==(updatedGame.players.currentPlayer.movesRemaining)
    }
    
    "switch the player to zombie" in {
      var updatedGame = testGame.executeCommand(NextPlayer)
      updatedGame.players.currentPlayer.isInstanceOf[Zombie] must be_==(true)
    }
  }
    
  "A game on the gametest Level, kill the zombies and switch players" should {
    
    val testGame = GameFactory.newGame(random = false, 
    		file = TestObjects.testfile_gametest.getFile(), humans = 2, zombies = 3)
    
    var updatedGame = testGame
    var counter = 0
    while(counter < 100 && (updatedGame.players.nextPlayer.deadTokenCount(updatedGame.gameField) == 0)) {
      counter += 1
      updatedGame = updatedGame.executeCommand(MoveUp)
    }
    val updatedGame1 = updatedGame
    val updatedGame2 = updatedGame.executeCommand(NextPlayer)
    
    "killed the zombies before switching" in {
      (updatedGame1.players.nextPlayer.deadTokenCount(updatedGame1.gameField) 
       must be_>=(updatedGame1.players.nextPlayer.playerTokenIds.size))      
    }
    
    "have the zombies respawned" in {
      updatedGame2.players.currentPlayer.isInstanceOf[Zombie] must be_==(true)
      updatedGame2.players.currentPlayer.deadTokenCount(updatedGame2.gameField) must be_==(0)
    }
    
    "have the zombies respawned frozen" in {
      (updatedGame2.players.currentPlayer.currentToken(updatedGame2.gameField).frozenTime 
      must be_==(defaults.defaultSpawnFreeze))
      val respawnedZombies =updatedGame2.gameField.findPlayerTokensById(
          updatedGame2.players.currentPlayer.playerTokenIds)
      respawnedZombies.filter(zombie => zombie.frozenTime == 0).size must be_==(0)      
    }
      
    "the gamestate suggesting a token switch" in {
      updatedGame2.gameState must be_==(GameState.NeedTokenSwitch)
    }
    
    var updatedGame3 = updatedGame2
    for(i <- (0 until updatedGame3.players.currentPlayer.totalTokens))
      updatedGame3 = updatedGame3.executeCommand(NextToken)
    val updatedGame4 = updatedGame3
      
    "allow no move for any token" in {
      (updatedGame4.players.currentPlayer.movesRemaining 
      must be_==(updatedGame2.players.currentPlayer.movesRemaining))
    }
    
  	val updatedGame5 = updatedGame4.executeCommand(NextPlayer)
															 		 .executeCommand(NextPlayer)
    "switch to human and back to zombie and frozentime is gone" in {

      																 
      val respawnedZombies =updatedGame5.gameField.findPlayerTokensById(
          updatedGame5.players.currentPlayer.playerTokenIds)
      respawnedZombies.filter(zombie => zombie.frozenTime != 0).size must be_==(0)               
    }
  }
  
  "A game on the gametest where zombies blocks the upper 3 fields" should {
    val testGame = GameFactory.newGame(random = false, 
      		file = TestObjects.testfile_gametest.getFile(), humans = 2, zombies = 3)
      		
    val updatedGame1 = testGame.executeCommand(NextPlayer)
    													 .executeCommand(MoveDown)
    													 .executeCommand(NextToken)    													 
    													 .executeCommand(MoveUp)
    													 .executeCommand(NextPlayer)
    
    													 
    val updatedGame2 = updatedGame1.executeCommand(MoveUp)
    														   .executeCommand(MoveUp)
    														   .executeCommand(MoveUp)
    														   .executeCommand(MoveUp)
    														   .executeCommand(MoveUp)
   "walk the first human into death when he moves up 5 times" in {
      updatedGame2.gameState must be_==(GameState.NeedPlayerSwitch)
      updatedGame2.players.currentPlayer.isInstanceOf[Human] must be_==(true)
      updatedGame2.players.currentPlayer.currentToken(updatedGame2.gameField).dead must be_==(true)
      updatedGame2.players.currentPlayer.deadTokenCount(updatedGame2.gameField) must be_==(1)
    }
    
    val updatedGame3 = updatedGame2.executeCommand(NextPlayer)
    														   .executeCommand(NextPlayer)
    														   .executeCommand(NextToken)
    														   .executeCommand(MoveUp)
    														   .executeCommand(MoveUp)
    														   .executeCommand(MoveUp)
    														   .executeCommand(MoveUp)
    														   .executeCommand(MoveUp)
    "walk the second human into death" in {
      updatedGame3.gameState must be_==(GameState.NeedPlayerSwitch)
      updatedGame3.players.currentPlayer.isInstanceOf[Human] must be_==(true)
      updatedGame3.players.currentPlayer.currentToken(updatedGame3.gameField).dead must be_==(true)
      updatedGame3.players.currentPlayer.deadTokenCount(updatedGame3.gameField) must be_==(2)
    }
    
    val updatedGame4 = updatedGame3.executeCommand(NextPlayer)
    														   .executeCommand(NextPlayer)
    														   .executeCommand(NextToken)
    "no more tokens to move with but still all lifes remaining" in {
      updatedGame4.players.currentPlayer.isInstanceOf[Human] must be_==(true)
      updatedGame4.players.currentPlayer.lifes must be_==(defaults.defaultHumanLifes)
      updatedGame4.gameState must be_==(GameState.NeedTokenSwitch)
    }
    
    val updatedGame5 = updatedGame4.executeCommand(RespawnToken)
    														   .executeCommand(NextToken)
    														   .executeCommand(MoveUp)
    "respawn one human and be frozen" in {
      updatedGame5.players.currentPlayer.isInstanceOf[Human] must be_==(true)
      (updatedGame5.players.currentPlayer.currentToken(updatedGame5.gameField).frozenTime 
      must be_==(defaults.defaultSpawnFreeze))
      updatedGame5.players.currentPlayer.movesRemaining must be_==(defaults.defaultHumanMoves)
      updatedGame5.gameState must be_==(GameState.NeedTokenSwitch)
    }
    
    val updatedGame6 = updatedGame5.executeCommand(RespawnToken)
    														   .executeCommand(NextToken)
    														   .executeCommand(MoveUp)
    "respawn annother one and be frozen" in {
      updatedGame6.players.currentPlayer.isInstanceOf[Human] must be_==(true)
      (updatedGame6.players.currentPlayer.currentToken(updatedGame6.gameField).frozenTime 
      must be_==(defaults.defaultSpawnFreeze))
      updatedGame6.players.currentPlayer.movesRemaining must be_==(defaults.defaultHumanMoves)
      updatedGame6.gameState must be_==(GameState.NeedTokenSwitch)
      (updatedGame6.players.currentPlayer.currentToken(updatedGame6.gameField).dead 
      must be_==(false))
      updatedGame6.players.currentPlayer.lifes must be_==(defaults.defaultHumanLifes-2)
    }
    
    "lose the game" in {
      var toBeLostGame = updatedGame6.executeCommand(NextPlayer)
      															 .executeCommand(NextPlayer)
      
      val humanTokens = toBeLostGame.gameField.findPlayerTokensById(
          toBeLostGame.players.currentPlayer.playerTokenIds)
      humanTokens.filter(zombie => zombie.frozenTime != 0).size must be_==(0)
      
      toBeLostGame.players.currentPlayer.deadTokenCount(toBeLostGame.gameField) must be_==(0)
        															 
      var counter = 0	
      while(counter < 100 && (toBeLostGame.gameState != GameState.GameOver) ) {
        counter += 1
        var currentPlayer = toBeLostGame.players.currentPlayer
      	var currentToken = currentPlayer.currentToken(toBeLostGame.gameField)
      	
      	var counter2 = 0
      	while(counter2 < 100 && (!currentToken.dead && currentToken.frozenTime == 0)) {
      	  counter2 += 1
      	  toBeLostGame = toBeLostGame.executeCommand(MoveUp)
      	  currentPlayer = toBeLostGame.players.currentPlayer
      	  currentToken = currentPlayer.currentToken(toBeLostGame.gameField)
      	}
        
    	  toBeLostGame = toBeLostGame.executeCommand(NextPlayer)
    	  toBeLostGame = toBeLostGame.executeCommand(NextPlayer)
    	  toBeLostGame = toBeLostGame.executeCommand(RespawnToken)
    	  toBeLostGame = toBeLostGame.executeCommand(NextToken)
      }
      
      toBeLostGame.gameState must be_==(GameState.GameOver)
    }
  }
  
  "A game on the gametest Level" should {
    val testGame = GameFactory.newGame(random = false, 
  		file = TestObjects.testfile_gametest.getFile(), humans = 2, zombies = 3)
    
    "not respawn any tokens" in {
      var updatedGame = testGame.executeCommand(RespawnToken)
      
     (updatedGame.players.currentPlayer.deadTokenCount(updatedGame.gameField) 
      must be_==(testGame.players.currentPlayer.deadTokenCount(testGame.gameField)))
    }
    
    "lead to no moves remaining in default moves by moving up and down" in {
      var counter = 0
      var updatedGame = testGame

      while(counter <= (defaults.defaultHumanMoves+1) && updatedGame.players.currentPlayer.movesRemaining > 0) {
        if(counter % 2 == 0)
        	updatedGame = updatedGame.executeCommand(MoveUp)
				else
				  updatedGame = updatedGame.executeCommand(MoveDown)
				counter += 1
      }
      counter must be_==(defaults.defaultHumanMoves)
    }
  }
  
  "A game on the gametest Level with 3 humans" should {
  	val testGame2 = GameFactory.newGame(random = false, 
  			file = TestObjects.testfile_gametest.getFile(), humans = 3, zombies = 3)
  
    val updatedGame1 = testGame2.executeCommand(NextPlayer)
    														.executeCommand(MoveDown)
    														.executeCommand(NextPlayer)    
    														.executeCommand(MoveUp)
  														  .executeCommand(MoveUp)
  														  .executeCommand(MoveUp)
  														  .executeCommand(MoveUp)
  														  .executeCommand(MoveUp)
  														  .executeCommand(NextPlayer)
  														  .executeCommand(NextPlayer)
  
  	"not select a dead token when cycling tokens" in {
  	  
  	  var tmpGame1 = updatedGame1
  	  var deadTokenSelected = false
  	  val currentPlayer = tmpGame1.players.currentPlayer
  	  currentPlayer.deadTokenCount(tmpGame1.gameField) must be_==(1)
  	  for(i <- 0 until currentPlayer.totalTokens) {
  	    tmpGame1 = tmpGame1.executeCommand(NextToken)
  	    if(tmpGame1.players.currentPlayer.currentToken(tmpGame1.gameField).dead)
  	      deadTokenSelected = true
  	  } 
  	  deadTokenSelected must be_==(false)  
  	}
  }
  
  "A game on the gametest Level with 3 humans when let the zombie kill all humans" should {
  	val testGame2 = GameFactory.newGame(random = false, 
  			file = TestObjects.testfile_gametest.getFile(), humans = 3, zombies = 3)
  
    val updatedGame1 = testGame2.executeCommand(MoveUp)
  														  .executeCommand(MoveUp)
  														  .executeCommand(NextToken)  														  
  														  .executeCommand(MoveUp)
  														  .executeCommand(MoveUp)
  														  .executeCommand(NextToken)
  														  .executeCommand(MoveUp)
  														  .executeCommand(MoveUp)
  														  .executeCommand(MoveUp)  														  
  														  .executeCommand(NextPlayer)
  														  .executeCommand(MoveDown)
  														  .executeCommand(MoveDown)
  														  .executeCommand(MoveDown)
  														  .executeCommand(MoveDown)
  														  .executeCommand(NextPlayer)
  														  .executeCommand(RespawnToken)
  
  	"respawn selects the new living token even if he wasn't selected previously" in {
  	  var tmpGame1 = updatedGame1
  	  val currentPlayer = tmpGame1.players.currentPlayer
  	  currentPlayer.deadTokenCount(tmpGame1.gameField) must be_==(2)
  	  currentPlayer.currentToken(tmpGame1.gameField).dead must be_==(false)
  	  
  	}
  }
  
    "A game on the gametest where zombies blocks the upper 3 fields" should {
    val testGame = GameFactory.newGame(random = false, 
      		file = TestObjects.testfile_gametest.getFile(), humans = 5, zombies = 4)
      		
      		
    val updatedGame1 = testGame.executeCommand(MoveUp)
    													 .executeCommand(NextToken)
    													 .executeCommand(MoveUp)    													 
    													 .executeCommand(MoveUp)
    													 .executeCommand(NextToken)
    													 .executeCommand(MoveUp)
    													 .executeCommand(MoveUp)
    													 .executeCommand(MoveUp)
    													 .executeCommand(NextPlayer)
    													 .executeCommand(MoveUp)
    													 .executeCommand(NextToken)
    													 .executeCommand(MoveDown)
    													 .executeCommand(NextToken)
    													 .executeCommand(MoveDown)
    													 .executeCommand(MoveDown)
    													 .executeCommand(NextPlayer)
    "with all fields occupied and one dead token" in {
      val updatedGame2 = updatedGame1.executeCommand(NextToken)
      															 .executeCommand(MoveUp)
          													 .executeCommand(MoveUp)
          													 .executeCommand(MoveUp)
          													 .executeCommand(MoveUp)
          													 .executeCommand(NextPlayer)
          													 .executeCommand(NextPlayer)
      val currentToken = updatedGame2.players.currentPlayer.currentToken(updatedGame2.gameField)
      currentToken.dead must be_==(true)
      
      "trying to respawn" should {
        "fail" in {
          val updatedGame3 = updatedGame2.executeCommand(RespawnToken)
          (updatedGame3.players.currentPlayer.deadTokenCount(updatedGame3.gameField)
           must be_==(updatedGame2.players.currentPlayer.deadTokenCount(updatedGame2.gameField)))
        }
      }
    }												 
  }
}