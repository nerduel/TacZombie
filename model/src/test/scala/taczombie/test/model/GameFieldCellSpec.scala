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

class GameFieldCellSpec extends Specification {
  
  "An empty cell" should {
    val gfc = emptyGfc
    
    "isEmpty return true" in {
      gfc.isEmpty must be_==(true)
    } 
    "containsCoin return false" in {
      gfc.containsCoin must be_==(false)
    }
    "containsHumanToken return false" in {
      gfc.containsHumanToken must be_==(false)
    }
    "containsLivingZombieToken return false" in {
      gfc.containsLivingZombieToken must be_==(false)
    }
    "containsLivingHumanToken return false" in {
      gfc.containsLivingHumanToken must be_==(false)
    }
    "containsPowerup return false" in {
      gfc.containsPowerup must be_==(false)
    }
    "containsWall return false" in {
      gfc.containsWall must be_==(false)
    }
    "containsZombieToken return false" in {
      gfc.containsZombieToken must be_==(false)
    }    
  }
  
  "An empty cell ADDED with a wall" should {
    val gfc = emptyGfc.addHere(wall)
    
    "isEmpty return false" in {
      gfc.isEmpty must be_==(false)
    }  
    "containsCoin return false" in {
      gfc.containsCoin must be_==(false)
    }
    "containsHumanToken return false" in {
      gfc.containsHumanToken must be_==(false)
    }
    "containsLivingZombieToken return false" in {
      gfc.containsLivingZombieToken must be_==(false)
    }
    "containsLivingHumanToken return false" in {
      gfc.containsLivingHumanToken must be_==(false)
    }
    "containsPowerup return false" in {
      gfc.containsPowerup must be_==(false)
    }
    "containsWall return true" in {
      gfc.containsWall must be_==(true)
    }
    "containsZombieToken return false" in {
      gfc.containsZombieToken must be_==(false)
    }   
  }
 
  "An empty cell ADDED with coin, living ZombieToken, dead HumanToken" should {
    val gfc = emptyGfc.addHere(coin)
    									.addHere(livingZombieToken)
    									.addHere(deadHumanToken)
    
    "isEmpty return false" in {
      gfc.isEmpty must be_==(false)
    }  
    "containsCoin return true" in {
      gfc.containsCoin must be_==(true)
    }
    "containsHumanToken return true" in {
      gfc.containsHumanToken must be_==(true)
    }
    "containsLivingZombieToken return true" in {
      gfc.containsLivingZombieToken must be_==(true)
    }
    "containsLivingHumanToken return false" in {
      gfc.containsLivingHumanToken must be_==(false)
    }
    "containsPowerup return false" in {
      gfc.containsPowerup must be_==(false)
    }
    "containsWall return false" in {
      gfc.containsWall must be_==(false)
    }
    "containsZombieToken return true" in {
      gfc.containsZombieToken must be_==(true)
    }   
  }
  
  "A living HumanToken MOVED to a cell with a coin" should {
    val gfc = emptyGfc.addHere(coin)
    									.moveHere(livingHumanToken)
    
    "isEmpty return false" in {
      gfc.isEmpty must be_==(false)
    }        
    "containsCoin return false" in {
      gfc.containsCoin must be_==(false)
    }
    "containsHumanToken return true" in {
      gfc.containsHumanToken must be_==(true)
    }
  } 
  
  "A living HumanToken MOVED to a cell with a powerup" should {
    val gfc = emptyGfc.addHere(powerUp)
    									.moveHere(livingHumanToken)
    
    "isEmpty return false" in {
      gfc.isEmpty must be_==(false)
    }        
    "containsPowerup return false" in {
      gfc.containsPowerup must be_==(false)
    }
    "containsHumanToken return true" in {
      gfc.containsHumanToken must be_==(true)
    }
    "containsLivingHumanToken return true" in {
      gfc.containsLivingHumanToken must be_==(true)
    }    
  }
    
  "A living HumanToken MOVED to a dead ZombieToken and Powerup" should {
    val gfc = emptyGfc.addHere(powerUp)
    									.addHere(deadZombieToken)
    									.moveHere(livingHumanToken)
    
    "containsPowerup return false" in {
      gfc.containsPowerup must be_==(false)
    }
    "containsHumanToken return true" in {
      gfc.containsHumanToken must be_==(true)
    }    
    "containsLivingHumanToken return true" in {
      gfc.containsLivingHumanToken must be_==(true)
    }
    "containsZombieToken return true" in {
      gfc.containsZombieToken must be_==(true)
    }
    "containsLivingZombieToken return false" in {
      gfc.containsLivingZombieToken must be_==(false)
    }
  }
  
  "A poweruped HumanToken moved to a living ZombieToken and Powerup" should {
    val gfc = emptyGfc.addHere(powerUp)
    									.moveHere(livingZombieToken)
    									.moveHere(poweredUpHumanToken)
         
    "containsPowerup return false" in {
      gfc.containsPowerup must be_==(false)
    }
    "containsHumanToken return true" in {
      gfc.containsHumanToken must be_==(true)
    }    
    "containsLivingHumanToken return true" in {
      gfc.containsLivingHumanToken must be_==(true)
    }
    "containsZombieToken return true" in {
      gfc.containsZombieToken must be_==(true)
    }
    "containsLivingZombieToken return false" in {
      gfc.containsLivingZombieToken must be_==(false)
    }        
  }  
  
  "A poweruped HumanToken moved to a living ZombieToken and Coin" should {
    val gfc = emptyGfc.addHere(coin)
    									.moveHere(livingZombieToken)
    									.moveHere(poweredUpHumanToken)
         
    "containsCoin return false" in {
      gfc.containsCoin must be_==(false)
    }
    "containsHumanToken return true" in {
      gfc.containsHumanToken must be_==(true)
    }    
    "containsLivingHumanToken return true" in {
      gfc.containsLivingHumanToken must be_==(true)
    }
    "containsZombieToken return true" in {
      gfc.containsZombieToken must be_==(true)
    }
    "containsLivingZombieToken return false" in {
      gfc.containsLivingZombieToken must be_==(false)
    }        
  }
  
  "Two living ZombieToken moved to and Coin. One ZombieToken leaves" should {
    val gfc = emptyGfc.addHere(coin)
    									.moveHere(livingZombieToken)
    									.moveHere(livingZombieToken2)
    									.remove(livingZombieToken2)
         
    "containsCoin return true" in {
      gfc.containsCoin must be_==(true)
    }
    "containsHumanToken return false" in {
      gfc.containsHumanToken must be_==(false)
    }    
    "containsLivingHumanToken return false" in {
      gfc.containsLivingHumanToken must be_==(false)
    }
    "containsZombieToken return true" in {
      gfc.containsZombieToken must be_==(true)
    }
    "containsLivingZombieToken return true" in {
      gfc.containsLivingZombieToken must be_==(true)
    }        
  }
  
    "A living HumanToken moved to a living ZombieToken and Powerup" should {
    val gfc = emptyGfc.addHere(powerUp)
    									.moveHere(livingZombieToken)
    									.moveHere(livingHumanToken)
         
    "containsPowerup return true" in {
      gfc.containsPowerup must be_==(true)
    }
    "containsHumanToken return true" in {
      gfc.containsHumanToken must be_==(true)
    }    
    "containsLivingHumanToken return false" in {
      gfc.containsLivingHumanToken must be_==(false)
    }
    "containsZombieToken return true" in {
      gfc.containsZombieToken must be_==(true)
    }
    "containsLivingZombieToken return true" in {
      gfc.containsLivingZombieToken must be_==(true)
    }        
  }
    
  "A living HumanToken gets updated" should {
    val gfc = emptyGfc.addHere(powerUp)
    									.moveHere(livingHumanToken)
    val gfc2 = gfc.updatedWithReplacement(livingHumanToken.updatedDecrementCounters)
    
    "isEmpty return false" in {
      gfc.isEmpty must be_==(false)
    }  
    "containsCoin return false" in {
      gfc.containsCoin must be_==(false)
    }
    "containsHumanToken return true" in {
      gfc.containsHumanToken must be_==(true)
    }
    "containsLivingZombieToken return false" in {
      gfc.containsLivingZombieToken must be_==(false)
    }
    "containsLivingHumanToken return true" in {
      gfc.containsLivingHumanToken must be_==(true)
    }
    "containsPowerup return false" in {
      gfc.containsPowerup must be_==(false)
    }
    "containsWall return false" in {
      gfc.containsWall must be_==(false)
    }
    "containsZombieToken return false" in {
      gfc.containsZombieToken must be_==(false)
    }   
  }
  
  "A human token and poweredUp Human move to one cell with a dead Human and dead Zombie" should {
    val gfc = emptyGfc.addHere(powerUp)
    									.addHere(deadHumanToken)
    									.addHere(deadZombieToken)
    									.moveHere(livingHumanToken)
    									.moveHere(poweredUpHumanToken)
    
    "isEmpty return false" in {
      gfc.isEmpty must be_==(false)
    }  
    "containsCoin return false" in {
      gfc.containsCoin must be_==(false)
    }
    "containsHumanToken return true" in {
      gfc.containsHumanToken must be_==(true)
    }
    "containsLivingZombieToken return false" in {
      gfc.containsLivingZombieToken must be_==(false)
    }
    "containsLivingHumanToken return true" in {
      gfc.containsLivingHumanToken must be_==(true)
    }
    "containsPowerup return false" in {
      gfc.containsPowerup must be_==(false)
    }
    "containsWall return false" in {
      gfc.containsWall must be_==(false)
    }
    "containsZombieToken return true" in {
      gfc.containsZombieToken must be_==(true)
    }   
  }  
}