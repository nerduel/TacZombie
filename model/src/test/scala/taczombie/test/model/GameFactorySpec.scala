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
    
    val testGame = GameFactory.newGame(random = false, 
      file = TestObjects.testfile_correct.getFile(), humans = 2, zombies = 4)    
    
    "Human must begin" in {
      testGame.players.currentPlayer.isInstanceOf[Human]
    }
    
    "produce Token Counts according to arguments" in {
      testGame.gameField.findPlayerTokensById(testGame.players.currentPlayer.playerTokenIds).size must be_==(2)
      testGame.gameField.findPlayerTokensById(testGame.players.nextPlayer.playerTokenIds).size must be_==(4)
    }
  }
}