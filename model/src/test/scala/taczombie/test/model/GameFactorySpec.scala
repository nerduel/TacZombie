package taczombie.test.model

import org.specs2.mutable.Specification
import taczombie.model.GameFactory
import taczombie.model.GameState
import taczombie.model.Game


class GameFactorySpec extends Specification {

  val testfile_correct = getClass().getResource("/TestLevel_correct")
  val testfile_incorrect = getClass().getResource("/TestLevel_incorrect")
  val testfile_incorrect2 = getClass().getResource("/TestLevel_incorrect2")
  
  
  "GameFactory" should {
    
    "produce unique ids" in {
      var idSet = Set[Int]() 
      for(i <- (0 until 1000))
        idSet += GameFactory.generateId
      
      idSet.size must be_==(1000)        
    }
    
    "be able to load a correct level" in {
      val game = GameFactory.newGame(random = false, file = testfile_correct.getFile())
      game.isInstanceOf[Game] must be_==(true)
    }
    
    "fail to load not rengular level" in {
      val game = GameFactory.newGame(random = false, file = testfile_incorrect.getFile())
      game must be_==(null)
    }
     
    "fail to load not a level with wrong char" in {
      val game = GameFactory.newGame(random = false, file = testfile_incorrect2.getFile())
      game must be_==(null)
    }
    
    "GameState must be InGame at begin" in {
      val game = GameFactory.newGame(random = false, file = testfile_correct.getFile())
      game.gameState must be_==(GameState.InGame)     
    }
  }
}