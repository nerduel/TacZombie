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
  
  protected val startAmount : Int
  protected val frozenTime : Int
  protected val dead : Boolean
  
  def updated(newCoords : (Int,Int)) : PlayerToken = updated(newCoords)
}

case class HumanToken(id : Int, 
                      coords : (Int,Int),
                  		coins : Int = 0,
                  		powerupTime : Int = 0,
                  		score : Int = 0,
                  		frozenTime : Int = 0,
                  		dead : Boolean = false)
                  		extends PlayerToken {
   
  protected val startAmount : Int = 1
  
  def updated(newCoords : (Int,Int) = this.coords,
      				addedCoins : Int = 0,
      				addedScore : Int = 0, 
      				addedPowerupTime : Int = 0,
      				addedFrozenTime : Int = 0,
      				dead : Boolean = false) = {
    new HumanToken(this.id, newCoords, this.coins+addedCoins, 
        					 this.powerupTime+addedPowerupTime,
        					 this.score, this.frozenTime+addedFrozenTime,
        					 this.dead)
  }
      
  def isVisitedBy (versatileGameObject : VersatileGameObject) = 
    versatileGameObject match {
      case zombieToken : ZombieToken => this.powerupTime match {
        case 0 => (this.updated(addedScore = -killScore, dead = true),
            			 zombieToken.updated(dead = true))
        case _ => (this.updated(addedScore = killScore), null)
    }
  }
}
   
case class ZombieToken(id : Int,
    									 coords : (Int,Int),
    									 frozenTime : Int = 0,
    									 dead : Boolean = false)
    									 extends NonHuman with PlayerToken {
  
  protected val startAmount = 3
  
  def updated(addedFrozenTime : Int = 0,
      				dead : Boolean = false) = {
    new ZombieToken(this.id, this.coords,
        					  this.frozenTime+addedFrozenTime,
        					  dead)
  }
  
  def isVisitedBy (versatileGameObject : VersatileGameObject) = 
    versatileGameObject match {
      case humanToken : HumanToken => humanToken.powerupTime match {
        case 0 => (this,
            			 humanToken.updated(addedScore = -killScore, dead = true))
        case _ => (this.updated(dead = true),
            			 humanToken.updated(addedScore = killScore))
      }
  }
}
