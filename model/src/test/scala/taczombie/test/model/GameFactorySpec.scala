package taczombie.test.model

import org.specs2.mutable.Specification
import taczombie.model.GameFactory
import taczombie.model.GameState
import taczombie.model.Game
import taczombie.model.Human


class GameFactorySpec extends Specification {
 
  "GameFactory" should {
   
    "produce unique ids" in {
      var idSet = Set[Int]() 
      for(i <- (0 until 1000))
        idSet += GameFactory.generateId
      
      idSet.size must be_==(1000)        
    }
    
    "be able to load a correct level" in {
      val game = GameFactory.newGame(random = false, 
          file = TestObjects.testfile_correct.getFile())
      game.isInstanceOf[Game] must be_==(true)
    }
    
    "fail to load not rengular level" in {
      val game = GameFactory.newGame(random = false, 
          file = TestObjects.testfile_incorrect.getFile())
      game must be_==(null)
    }
     
    "fail to load not a level with wrong char" in {
      val game = GameFactory.newGame(random = false, 
          file = TestObjects.testfile_incorrect2.getFile())
      game must be_==(null)
    }
    
    "GameState must be InGame at begin" in {
      val game = GameFactory.newGame(random = false, 
          file = TestObjects.testfile_correct.getFile())
      game.gameState must be_==(GameState.InGame)     
    }
    
    val gametestLevel = GameFactory.newGame(random = false, 
      file = TestObjects.testfile_gametest.getFile(), humans = 2, zombies = 4)    
    
    "Human must begin" in {
      gametestLevel.players.currentPlayer.isInstanceOf[Human]
    }
    
    "produce Token Counts according to arguments" in {
      gametestLevel.gameField.findPlayerTokensById(gametestLevel.players.currentPlayer.playerTokenIds).size must be_==(2)
      gametestLevel.gameField.findPlayerTokensById(gametestLevel.players.nextPlayer.playerTokenIds).size must be_==(4)
    }
  }
}