package taczombie.test.model

import org.specs2.mutable.Specification

import taczombie.model.GameFieldCell
import taczombie.model.GameObject
import taczombie.model.HumanToken
import taczombie.model.ZombieToken
import taczombie.model.Coin
import taczombie.model.Powerup
import taczombie.model.Wall
import taczombie.model.GameFactory

import TestObjects._

class GameObjectSpec extends Specification {
	
  "HumanToken visited by a ZombieToken" should {
    val resultTuple = livingHumanToken.isVisitedBy(livingZombieToken)
    "HumanToken must be dead" in {
      resultTuple._1.asInstanceOf[HumanToken].dead must be_==(true)
    }
    "ZombieToken must be alive" in {
      resultTuple._2.asInstanceOf[ZombieToken].dead must be_==(false)
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
    "HumanToken with Poweruptime > 0" in {
    	(resultTuple._2.asInstanceOf[HumanToken].powerupTime > 0) must be_==(true)
    }
  }  
}