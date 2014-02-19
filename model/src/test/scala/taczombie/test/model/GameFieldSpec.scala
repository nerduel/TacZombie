package taczombie.test.model

import org.specs2.mutable.Specification
import taczombie.model.GameFactory
import taczombie.model.util.CoordinateHelper._
import taczombie.model.defaults

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
	
	"For a gameField generated from the gametest-level" should {
	  
    val testGame = GameFactory.newGame(random = false, 
    file = TestObjects.testfile_gametest.getFile(), humans = 2, zombies = 3)
    
    val testGameField = testGame.gameField
    val testGamePlayers = testGame.players
    val firstPlayer = testGamePlayers.currentPlayer
    val firstPlayerToken = firstPlayer.currentToken(testGameField)

    val humanBaseCell = testGameField.gameFieldCells.apply(testGameField.humanBase)      
    "humanbase must contain only human" in {
      humanBaseCell.isEmpty must be_==(false)
      humanBaseCell.containsHumanToken must be_==(true)
      humanBaseCell.containsLivingHumanToken must be_==(true)
      humanBaseCell.containsZombieToken must be_==(false)
      humanBaseCell.containsLivingZombieToken must be_==(false)
      humanBaseCell.containsPowerup must be_==(false)
      humanBaseCell.containsCoin must be_==(false)      
    }
    
    val aboveHumanBase = testGameField.gameFieldCells.apply(testGameField.humanBase.aboveOf)  
    "above must contain only coin" in {
      aboveHumanBase.isEmpty must be_==(false)
      aboveHumanBase.containsHumanToken must be_==(false)
      aboveHumanBase.containsLivingHumanToken must be_==(false)
      aboveHumanBase.containsZombieToken must be_==(false)
      aboveHumanBase.containsLivingZombieToken must be_==(false)
      aboveHumanBase.containsPowerup must be_==(false)
      aboveHumanBase.containsCoin must be_==(true)      
    }    
    
    "the humanToken must be able to move up and collect the coin" in {
      val resultGameField = testGameField.move(firstPlayerToken, aboveHumanBase.coords)
      val updatedToken = resultGameField.findOnePlayerTokenById(firstPlayerToken.id)
      val destinationCell = resultGameField.gameFieldCells.apply(aboveHumanBase.coords)         
      updatedToken.coords must be_==(aboveHumanBase.coords)
      updatedToken.coins must be_==(firstPlayerToken.coins+1)
    }
    
    "a human token without powerup be able to collect the powerup" in {
      val resultGameField1 = testGameField.move(firstPlayerToken, (3,1))
      val resultTokenWithPowerup = resultGameField1.findOnePlayerTokenById(firstPlayerToken.id)
      
      firstPlayerToken.powerupTime must be_==(0)
      resultTokenWithPowerup.powerupTime must be_==(defaults.defaultPowerupTime)
      
      val resultGameField2 = resultGameField1.move(resultTokenWithPowerup, resultGameField1.zombieBase)
      val resultTokenWithHigherKillScore = resultGameField2.findOnePlayerTokenById(resultTokenWithPowerup.id)
      val zombieResultTokens = resultGameField2.findPlayerTokensById(testGamePlayers.nextPlayer.playerTokenIds)
      
      "and then move to a zombie and kill him" in {
        for(zombie <- zombieResultTokens)
          zombie.dead must be_==(true)  
        resultTokenWithHigherKillScore.score must be_>(resultTokenWithPowerup.score)
      }
      
    	var resultGameField3 = resultGameField2
      "respawned zombies" should {
      	for(deadZombieToken <- zombieResultTokens)
      		resultGameField3 = resultGameField3.respawn(deadZombieToken.id)
      		
      	val respawnedZombies = resultGameField3.findPlayerTokensById(testGamePlayers.nextPlayer.playerTokenIds)	

        "be alive" in {
      	  testGamePlayers.nextPlayer.deadTokens(resultGameField3).size must be_==(0)
      	  respawnedZombies.filter(zombie => zombie.dead == true).size must be_==(0)
        }
      	
      	"be frozen" in {
      	  respawnedZombies.filter(zombie => zombie.frozenTime == defaults.defaultSpawnFreeze).size must be_==(respawnedZombies.size)      	  
      	}
      	
      	"be on different cells" in {
      		respawnedZombies.foldLeft(Set[(Int,Int)]())({(set, zombie) => 
      		  set + zombie.coords
  		    }).size must be_==(respawnedZombies.size)
      	}
      }
    }
	}
}