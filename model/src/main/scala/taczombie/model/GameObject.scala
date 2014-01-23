package taczombie.model

trait GameObject {
  val id : Int
  val coords : (Int, Int)

  override def hashCode = id
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
        (null, humanToken.updated(addedCoins = 1, addedScore = 1))
      case _ => (this, versatileGameObject)
  }
}
   
case class Powerup(id : Int,
    							 val coords : (Int,Int),
                   time : Int = 5)
                   extends Collectable{  
  def isVisitedBy (versatileGameObject : VersatileGameObject) = 
    versatileGameObject match {
      case humanToken : HumanToken => 
        (null, humanToken.updated(addedPowerupTime = 10, addedScore = 1))
      case _ => (this, versatileGameObject)
  }
}

trait PlayerToken extends VersatileGameObject {
  protected val killScore = 3
  
  val frozenTime : Int
  require(frozenTime >= 0)
  
  val dead : Boolean
  
  def updated(coords : (Int,Int)) : PlayerToken
  def updatedMoved() : PlayerToken
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
  require(frozenTime >= 0)
  
  def updated(coords : (Int,Int)) = updated(newCoords = coords)
  def updatedMoved() = updated(addedPowerupTime = -1, addedFrozenTime = -1)
  def updated(newCoords : (Int,Int) = this.coords,
      				addedCoins : Int = 0,
      				addedScore : Int = 0, 
      				addedPowerupTime : Int = 0,
      				addedFrozenTime : Int = 0,
      				dead : Boolean = this.dead) = {
    
    // checks
    val newPowerUpTime = { var tmp = this.powerupTime+addedPowerupTime
      if (tmp < 0) 0
      else tmp
    }
    val newFrozenTime = { var tmp = this.frozenTime+addedFrozenTime
      if (tmp < 0) 0
      else tmp
    }
    
    new HumanToken(this.id, newCoords, this.coins+addedCoins, 
        					 newPowerUpTime,
        					 this.score, newFrozenTime,
        					 dead)
  }
      
  def isVisitedBy (versatileGameObject : VersatileGameObject) = 
    versatileGameObject match {
      case zombieToken : ZombieToken => 
        (zombieToken.dead, this.powerupTime) match {
        case (false, 0) => (this.updated(addedScore = -killScore, dead = true),
            			 				 zombieToken)
        case (false, _) => (this.updated(addedScore = killScore), 
            								zombieToken.updated(dead = true))
        case (true, _) => (this, zombieToken) // spawn!
    }
  }
}
   
case class ZombieToken(id : Int,
    									 coords : (Int,Int),
    									 frozenTime : Int = 0,
    									 dead : Boolean = false)
    									 extends NonHuman with PlayerToken {
  
  def updated(coords : (Int,Int)) = updated(newCoords = coords)
  def updatedMoved() = updated(addedFrozenTime = -1)
  def updated(newCoords : (Int, Int) = this.coords,
      				addedFrozenTime : Int = 0,
      				dead : Boolean = false) = {
    new ZombieToken(this.id, newCoords,
        					  this.frozenTime+addedFrozenTime,
        					  dead)
  }
  
  def isVisitedBy (versatileGameObject : VersatileGameObject) = 
    versatileGameObject match {
      case humanToken : HumanToken => 
        (humanToken.dead, humanToken.powerupTime) match {
          case (true, _) => (this, humanToken) // spawn!
          case (false, 0) => (this,
              			 humanToken.updated(addedScore = -killScore, dead = true))              			 
          case (false, _) => (this.updated(dead = true),
              			 humanToken.updated(addedScore = killScore))
      }
  }
}
