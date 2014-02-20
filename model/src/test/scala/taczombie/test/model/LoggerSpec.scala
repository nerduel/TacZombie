package taczombie.test.model

import org.specs2.mutable.Specification
import taczombie.model.GameFactory
import taczombie.model.MoveDown

class LoggerSpec extends Specification {
  		
  "Creating a new Game which extends Logger" should {    
    "without initialisation yield an empty logger" in {
    	val game = GameFactory.newGame(random = false, 
          file = TestObjects.testfile_correct.getFile())
      game.logger.print    
      game.logger.get.toList.size must be_==(0)
    }
    
    "otherwise not" in {
      val game = GameFactory.newGame(random = false, 
          file = TestObjects.testfile_correct.getFile())
      game.logger.init("We run "+this, false, true)
      game.logger.get.toList.size must be_>(0)
    }
  }     	
}