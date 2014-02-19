package taczombie.test.model

import org.specs2.mutable.Specification
import taczombie.model.GameFactory

class GameFieldSpec extends Specification {
	"For a manually created gameField" should {
	  val gf = TestObjects.gameField
	  val h1 = TestObjects.human
	  val z1 = TestObjects.zombie
	  
	  "findOnePlayerTokenById able to find one playerToken with id" in {
	    ((gf.findOnePlayerTokenById(h1.currentTokenId)).id 
	    		must be_==(h1.currentTokenId))
	  }
	  
	  "findOnePlayerTokenById return null for an invalid id" in {
	    gf.findOnePlayerTokenById(1234) must be_==(null)	      
    }
	  
	  "find all human tokens" in {
	    gf.findHumanTokens.size must be_==(h1.totalTokens)
	  }
	  
	  "find all zombie tokens" in {
	    gf.findPlayerTokensById(z1.playerTokenIds).size must be_==(z1.totalTokens)
	  }
	  
	  "successfully decrement movesRemaining for a player" in {
	    val prePoweruptime = h1.currentToken(gf).powerupTime
	    val updatedGf = gf.updatedDecrementedCounters(h1)
	    val postPoweruptime = h1.currentToken(updatedGf).powerupTime
	    postPoweruptime must be_==(prePoweruptime-1)
	  }
	}
	
	"For a gameField generated from a correct TestLevel" should {
	  //val game = GameFactory.newGame(random = false, humans = 2, zombies = 4)
	}
}