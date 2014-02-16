package taczombie.model

import taczombie.model.util.Logger

trait GameObject extends Logger {
  val id : Int
  val coords : (Int, Int)

  override def hashCode(): Int = id
}
  
trait StaticGameObject extends GameObject
trait VersatileGameObject extends GameObject {

  /// Calculate and return a leftover pair for (host, visitor) Gameobjects
  def isVisitedBy (versatileGameObject : VersatileGameObject) 
  	: (VersatileGameObject, VersatileGameObject)  
}

trait NonHuman extends VersatileGameObject
trait Collectable extends NonHuman

case class Wall(id : Int, coords : (Int,Int)) extends StaticGameObject

case class Coin(id : Int,
    						val coords : (Int,Int))
                extends Collectable {
  def isVisitedBy (versatileGameObject : VersatileGameObject) = 
    versatileGameObject match {
      case humanToken : HumanToken => 
        (null, humanToken.updated(newCoins = 
          humanToken.coins + 1, newScore = humanToken.score+1))
      case zombieToken : ZombieToken => (this, zombieToken)
  }
}
   
case class Powerup(id : Int,
    							 val coords : (Int,Int),
                   time : Int = 5)
                   extends Collectable{  
  def isVisitedBy (versatileGameObject : VersatileGameObject) = 
    versatileGameObject match {
      case humanToken : HumanToken => 
        (null, humanToken.updated(newPowerupTime = 
          humanToken.powerupTime+10, newScore = humanToken.score+1))
      case zombieToken : ZombieToken => (this, zombieToken)
  }
}

trait PlayerToken extends VersatileGameObject {
  val id : Int
  val frozenTime : Int
  require(frozenTime >= 0)
  
  val dead : Boolean
  
  def coins : Int
  def score : Int
  
  def updated(newCoords : (Int,Int) = coords,
      				newCoins : Int = coins,
      				newScore : Int = score, 
      				newPowerupTime : Int = 0,
      				newFrozenTime : Int = this.frozenTime,
      				newDead : Boolean = this.dead) : PlayerToken
  
  def updatedDecrementCounters() : PlayerToken
}

case class HumanToken(id : Int, 
                      coords : (Int,Int),
                  		coins : Int = 0,
                  		powerupTime : Int = 0,
                  		score : Int = 0,
                  		frozenTime : Int = 0,
                  		dead : Boolean = false)
                  		extends PlayerToken {
  
  require(powerupTime >= 0)
  

  override def updated(newCoords : (Int,Int) = this.coords,
      				newCoins : Int = this.coins,
      				newScore : Int = this.score, 
      				newPowerupTime : Int = this.powerupTime,
      				newFrozenTime : Int = this.frozenTime,
      				newDead : Boolean = this.dead) = {    
    // checks
    val checkedNewPowerUpTime = 
      if (newPowerupTime < 0) 0
      else newPowerupTime
    
    val checkedNewFrozenTime =
      if (newFrozenTime < 0) 0
      else newFrozenTime
    
    new HumanToken(this.id, newCoords, newCoins, checkedNewFrozenTime,
        					 newScore, checkedNewPowerUpTime, dead)
  }
  
  
  def updatedDecrementCounters() : HumanToken = {
    updated(newPowerupTime = this.powerupTime-1,
        		newFrozenTime = this.frozenTime-1)
  }  
    
  def isVisitedBy (versatileGameObject : VersatileGameObject) = { 
    versatileGameObject match {
      case zombieToken : ZombieToken => {
        (zombieToken.dead, this.powerupTime) match {
        case (false, 0) => (this.updated(newScore = 
          this.score-defaults.killScore, newDead = true),
            			 				 zombieToken)
        case (false, _) => (this.updated(newScore = 
          this.score+defaults.killScore), 
            								zombieToken.updated(newDead = true))
        case (true, _) => (this, zombieToken) // spawn!
        }
      }
      case humanToken : HumanToken => (this, humanToken)
    }
  }
}
   
case class ZombieToken(id : Int,
    									 coords : (Int,Int),
    									 frozenTime : Int = 0,
    									 dead : Boolean = false)
    									 extends NonHuman with PlayerToken {
  
  def coins = 0
  def score = 0
  def powerupTime = 0
  
  def updated(newCoords : (Int,Int) = this.coords,
      				newCoins : Int = this.coins,
      				newScore : Int = this.score, 
      				newPowerupTime : Int = this.powerupTime,
      				newFrozenTime : Int = this.frozenTime,
      				newDead : Boolean = this.dead) : ZombieToken = {
    
    val checkedNewFrozenTime =
      if (newFrozenTime < 0) 0
      else newFrozenTime
    
  	new ZombieToken(this.id, newCoords, checkedNewFrozenTime, newDead)
  }
  
  def updatedDecrementCounters() : ZombieToken = {
    updated(newFrozenTime = this.frozenTime-1)
  }    
  
  def isVisitedBy (versatileGameObject : VersatileGameObject) = 
    versatileGameObject match {
      case humanToken : HumanToken => {
        (humanToken.dead, humanToken.powerupTime) match {
          case (true, _) => (this, humanToken) // spawn!
          case (false, 0) => (this,
              			 humanToken.updated(newScore = 
              			   humanToken.score-defaults.killScore, newDead = true))              			 
          case (false, _) => (this.updated(newDead = true),
              			 humanToken.updated(newScore = 
              			   humanToken.score+defaults.killScore))        
        }
      }
      case zombieToken : ZombieToken => (this, zombieToken)
    }
}