package taczombie.test.model

import org.specs2.mutable.Specification

import TestObjects.gameField
import TestObjects.zombie
import taczombie.model.Human
import taczombie.model.Player
import taczombie.model.defaults

class PlayerSpec extends Specification {
	val gf = TestObjects.gameField
  val humanPlayer : Player = TestObjects.human
  val zombiePlayer : Player = zombie
  
  "Human without Tokens" should {
    val player : Player = new Human("", List[Int]())
    "totalTokens must be 0" in {
      player.totalTokens must be_==(0)
    }
    "currentTokenId must be 0" in {
      player.currentTokenId must be_==(0)
    } 
  }
  
  "Human with one token" should {
    val player : Player = new Human("", List[Int](TestObjects.livingHumanToken.id))
    "totalTokens must be 1" in {
      player.totalTokens must be_==(1)
    }
    
    "currentToken has to return the token" in {
      player.currentToken(TestObjects.gameField).id must be_==(TestObjects.livingHumanToken.id)
    }
    
    "currentToken must not change on cycle" in {
      player.updatedCycledTokens.currentToken(TestObjects.gameField).id must be_==(player.currentToken(TestObjects.gameField).id)
    }
  }	

	
	"Human with 4 Tokens on a gameField treated as Player" should {
	  
	  "totalTokens must be 4" in {
	    humanPlayer.totalTokens must be_==(4)
	  }
	  "lifes must be default at beginning" in {
	    humanPlayer.lifes must be_==(defaults.defaultHumanLifes)
	  }	 	  
 	  "coins must be 0 at beginning" in {
	    humanPlayer.coins(gameField) must be_==(0)
	  }
	  "score must be 0 at beginning" in {
	    humanPlayer.score(gameField) must be_==(0)
	  }
	  "dead token count must be 1" in {
	    humanPlayer.deadTokenCount(gameField) must be_==(1)
	  }
	  
	  "dead tokens must yield all dead tokens" in {
	    humanPlayer.deadTokens(gf).size must be_==(humanPlayer.deadTokenCount(gameField))
	  }
	}
	
	"Zombie with 3 Tokens treated as Player" should {
	  "totalTokens must be 3" in {
	    zombiePlayer.totalTokens must be_==(3)
  	}	  
	}	
	
	"Rotating the Tokens token-count-times" should {
	  "give the same currentTokenId as before" in {
  	  var uhp : Player = humanPlayer
  	  val currentTokenId = uhp.currentTokenId
  	  for(i <- (0 until humanPlayer.totalTokens))
  	    uhp = uhp.updatedCycledTokens
  	  
  	  humanPlayer.currentTokenId must be_==(currentTokenId)
	  }	   
	}
	
	"Player with default moves remaining" should { 
	  sequential
	  
	  var uhp = humanPlayer
	  var uzp = zombiePlayer
	  
	  "must have default moves remaining at start" in {
	    "for human " in {
	      uhp.movesRemaining must be_==(defaults.defaultHumanMoves)
	    }
	    "for zombie " in  {
	      uzp.movesRemaining must be_==(defaults.defaultZombieMoves)
	    }
	  }
	  
	  "have one less moves remaining after updatedMoves" in {
	    "for human" in {
	    	uhp = uhp.updatedMoved
	    	uhp.movesRemaining must be_==(defaults.defaultHumanMoves-1)
	    }
	    "for zombie" in {
	      uzp = uzp.updatedMoved
	    	uzp.movesRemaining must be_==(defaults.defaultZombieMoves-1)
	    }
	  }
	  
    "have them reset to defaults after reset" in {
	    "for human" in {
	    	uhp = uhp.updatedResetMovesRemaining
	    	uhp.movesRemaining must be_==(defaults.defaultHumanMoves)
	    }
	    "for zombie" in {
	      uzp = uzp.updatedResetMovesRemaining
	    	uzp.movesRemaining must be_==(defaults.defaultZombieMoves)
	    }
	  }
	}
	
	"Coins" should {
		sequential
	  var uhp = humanPlayer
	  var uzp = zombiePlayer
	  var gf = gameField
	   
	  "0 on start" in {
	    "for human" in {
	    	uhp.coins(gf) must be_==(0)
	    }
	    "for zombie" in {
	    	uzp.coins(gf) must be_==(0)
	    }
	  }	  
	}
	
	"Score" should {
		sequential
	  var uhp = humanPlayer
	  var uzp = zombiePlayer
	  var gf = gameField
	   
	  "0 on start" in {
	    "for human" in {
	    	uhp.score(gf) must be_==(0)
	    }
	    "for zombie" in {
	    	uzp.score(gf) must be_==(0)
	    }
	  }	  
	}

	"Lifes" should {
		sequential
	  var uhp = humanPlayer
	  var uzp = zombiePlayer
	  var gf = gameField
	   
	  "default on start" in {
	    "for human" in {
	    	uhp.lifes must be_==(defaults.defaultHumanLifes)
	    }
	    
	    
	    "for zombie" in {
	    	uzp.lifes must be_==(0)
	    }
	  }	
  	"decreasable by one for human" in {
  		uhp.updatedDecreasedLifes.lifes must be_==(uhp.lifes-1)
  	}
	}
	
	"Current token" should {
	  
	  var uhp = humanPlayer
	  var uzp = zombiePlayer
	  var gf = gameField
	  
	  "yield the first token in list on creation" in {
	    uhp.currentToken(gf).id must be_==(TestObjects.poweredUpHumanToken.id)
	  	uzp.currentToken(gf).id must be_==(TestObjects.livingZombieToken.id)
	  }
	  
	  "yield to the second one after beeing rotated once" in {
	    uhp.updatedCycledTokens.currentToken(gf).id must be_==(TestObjects.livingHumanToken.id)
	  	uzp.updatedCycledTokens.currentToken(gf).id must be_==(TestObjects.livingZombieToken2.id)	    
	  }
	}
}