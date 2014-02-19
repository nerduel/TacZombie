package taczombie.test.model

import org.specs2.mutable.Specification

import TestObjects.gameField
import TestObjects.human
import TestObjects.humanWithDeadToken
import TestObjects.zombie
import TestObjects.zombieWithDeadToken
import taczombie.model.Human
import taczombie.model.Player
import taczombie.model.defaults

class PlayerSpec extends Specification {
  
  "Human without Tokens" should {
    val player : Player = new Human("", List[Int]())
    "totalTokens must be 0" in {
      player.totalTokens must be_==(0)
    }
    "currentTokenId must be 0" in {
      player.currentTokenId must be_==(0)
    } 
  }
  
	"Human with two Tokens treated as Player" should {
	  val humanPlayer : Player = human
	  "totalTokens must be 2" in {
	    humanPlayer.totalTokens must be_==(2)
	  }
	  "lifes must be default at beginning" in {
	    humanPlayer.lifes must be_==(defaults.humanLifes)
	  }	 	  
 	  "coins must be 0 at beginning" in {
	    humanPlayer.coins(gameField) must be_==(0)
	  }
	  "score must be 0 at beginning" in {
	    humanPlayer.score(gameField) must be_==(0)
	  }
	}
	
	"Zombie with two Tokens treated as Player" should {
	  val zombiePlayer : Player = zombie
	  "totalTokens must be 2" in {
	    zombiePlayer.totalTokens must be_==(2)
	  }
	  
	  
	}	
	
	"Human and Zombie treated as Player with one living and dead token each" should {
	  val humanPlayer : Player = humanWithDeadToken
	  val zombiePlayer : Player = zombieWithDeadToken
	  "totalTokens must be 2" in {
	    humanPlayer.totalTokens must be_==(2)
	    zombiePlayer.totalTokens must be_==(2)
	  }
	  "totalTokens must be 2" in {
	    humanPlayer.deadTokenCount(gameField) must be_==(1)
	    zombiePlayer.deadTokenCount(gameField) must be_==(1)
	  }	  
	}	
	
	"Rotating the Tokens token-count-times" should {
	  "give the same currentTokenId as before" in {
  	  var humanPlayer : Player = human.updatedCycledTokens
  	  var currentTokenId = humanPlayer.currentTokenId
  	  for(i <- (0 until humanPlayer.totalTokens))
  	    humanPlayer = humanPlayer.updatedCycledTokens
  	  
  	  humanPlayer.currentTokenId must be_==(currentTokenId)
	  }	   
	}
}