package taczombie.test.model

import org.specs2.mutable.Specification
import TestObjects.coin
import TestObjects.deadHumanToken
import TestObjects.deadZombieToken
import TestObjects.livingHumanToken
import TestObjects.livingZombieToken
import TestObjects.powerUp
import TestObjects.poweredUpHumanToken
import taczombie.model.HumanToken
import taczombie.model.PlayerToken
import taczombie.model.ZombieToken
import taczombie.model.defaults

class GameObjectSpec extends Specification {
	
  "HumanToken visited by a HumanToken" should {
    val resultTuple = livingHumanToken.isVisitedBy(livingHumanToken)
    "HumanToken must be dead" in {
      resultTuple._1.asInstanceOf[HumanToken].dead must be_==(false)
    }
    "HumanToken must be alive" in {
      resultTuple._2.asInstanceOf[HumanToken].dead must be_==(false)
    }    
  }

  "ZombieToken visited by a ZombieToken" should {
    val resultTuple = livingZombieToken.isVisitedBy(livingZombieToken)
    "ZombieToken must be dead" in {
      resultTuple._1.asInstanceOf[ZombieToken].dead must be_==(false)
    }
    "ZombieToken must be alive" in {
      resultTuple._2.asInstanceOf[ZombieToken].dead must be_==(false)
    }    
  }    
  
  "HumanToken visited by a ZombieToken" should {
    val resultTuple = livingHumanToken.isVisitedBy(livingZombieToken)
    "HumanToken must be dead" in {
      resultTuple._1.asInstanceOf[HumanToken].dead must be_==(true)
    }
    "ZombieToken must be alive" in {
      resultTuple._2.asInstanceOf[ZombieToken].dead must be_==(false)
    }    
  }
  
  "HumanToken visited by a dead ZombieToken" should {
    val resultTuple = livingHumanToken.isVisitedBy(deadZombieToken)
    "HumanToken must be alive" in {
      resultTuple._1.asInstanceOf[HumanToken].dead must be_==(false)
    }
    "ZombieToken must be dead" in {
      resultTuple._2.asInstanceOf[ZombieToken].dead must be_==(true)
    }    
  }  
  
  "HumanToken with powerup visited by a ZombieToken" should {
    val resultTuple = poweredUpHumanToken.isVisitedBy(livingZombieToken)
    "HumanToken must be alive" in {
      resultTuple._1.asInstanceOf[HumanToken].dead must be_==(false)
    }
    "HumanToken with Score > 0" in {
    	(resultTuple._1.asInstanceOf[HumanToken].score > 0) must be_==(true)
    }        
    "ZombieToken must be dead" in {
      resultTuple._2.asInstanceOf[ZombieToken].dead must be_==(true)
    }
  }
  
  "ZombieToken visited by HumanToken with powerup" should {
    val resultTuple = livingZombieToken.isVisitedBy(poweredUpHumanToken)
    "ZombieToken must be dead" in {
      resultTuple._1.asInstanceOf[ZombieToken].dead must be_==(true)
    }    
    "HumanToken must be alive" in {
      resultTuple._2.asInstanceOf[HumanToken].dead must be_==(false)
    }
    "HumanToken with Score > 0" in {
    	(resultTuple._2.asInstanceOf[HumanToken].score > 0) must be_==(true)
    }    
  }    
  
  "A dead HumanToken visited by a ZombieToken" should {
    val resultTuple = deadHumanToken.isVisitedBy(livingZombieToken)
    "HumanToken must be dead" in {
      resultTuple._1.asInstanceOf[HumanToken].dead must be_==(true)
    }
    "ZombieToken must be alive" in {
      resultTuple._2.asInstanceOf[ZombieToken].dead must be_==(false)
    }    
  } 
  
  "A dead ZombieToken  visited by a HumanToken" should {
    val resultTuple = deadZombieToken.isVisitedBy(livingHumanToken)
    "ZombieToken must be dead" in {
      resultTuple._1.asInstanceOf[ZombieToken].dead must be_==(true)
    }
    "HumanToken must be alive" in {
      resultTuple._2.asInstanceOf[HumanToken].dead must be_==(false)
    }    
  }   
  
  "A Powerup visited by a HumanToken" should {
    val resultTuple = powerUp.isVisitedBy(livingHumanToken)
    "powerup must be gone" in {
      (resultTuple._1 == null) must be_==(true)
    }
    "HumanToken with Poweruptime default" in {
    	resultTuple._2.asInstanceOf[HumanToken].powerupTime must be_==(defaults.defaultPowerupTime)
    }
  }

  "A Coin visited by a HumanToken" should {
    val resultTuple = coin.isVisitedBy(livingHumanToken)
    "coin must be gone" in {
      (resultTuple._1 == null) must be_==(true)
    }
    "HumanToken with coin 1" in {
    	resultTuple._2.asInstanceOf[HumanToken].coins must be_==(1)
    }
    "HumanToken with score 1" in {
    	resultTuple._2.asInstanceOf[HumanToken].score must be_==(1)
    }    
  }
  
  "ZombieToken gets frozen and decremented " should {
    val zombie = livingZombieToken.updated(newFrozenTime = 2).updatedDecrementCounters
    "frozenTime must be 1" in {
      zombie.frozenTime must be_==(1)
    }
  }
  
  "ZombieToken as PlayerTokens gets decremented twice" should {
    val playerToken : PlayerToken = livingZombieToken
    val pT = playerToken.updatedDecrementCounters.updatedDecrementCounters
    "frozenTime must be 0" in {
      pT.frozenTime must be_==(0)
    }
  }

  "HumanToken as PlayerTokens gets decremented twice" should {
    val playerToken : PlayerToken = livingHumanToken
    val pT = playerToken.updatedDecrementCounters.updatedDecrementCounters
    "frozenTime must be 0" in {
      pT.frozenTime must be_==(0)
    }
  }   
  
}