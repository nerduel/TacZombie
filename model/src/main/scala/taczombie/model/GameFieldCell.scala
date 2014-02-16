package taczombie.model

import taczombie.model.util.Logger

class GameFieldCell(val coords : (Int, Int),
    								val gameObjects : Set[GameObject]) extends Logger {

  def isEmpty() : Boolean = {
    gameObjects.size == 0
  }  
  
  def containsZombieToken() : Boolean = {
    gameObjects.filter(gameObject =>
      gameObject.isInstanceOf[ZombieToken]).nonEmpty
  }
  
  def containsHumanToken() : Boolean = {
    gameObjects.filter(gameObject =>
      gameObject.isInstanceOf[HumanToken]).nonEmpty
  }   
  
  def containsLivingZombieToken() : Boolean = {
    gameObjects.filter(gameObject =>
      gameObject match {
        case zombieToken : ZombieToken => !zombieToken.dead
        case _ => false
      }).nonEmpty
  }
  
  def containsLivingHumanToken() : Boolean = {
    gameObjects.filter(gameObject =>
      gameObject match {
        case humanToken : HumanToken => !humanToken.dead
        case _ => false
      }).nonEmpty
  }  
  
  def containsWall() : Boolean = {
    gameObjects.filter(gameObject =>  
      gameObject.isInstanceOf[Wall]).nonEmpty
  }

  def containsCoin() : Boolean = {
    gameObjects.filter(gameObject =>  
      gameObject.isInstanceOf[Coin]).nonEmpty
  }
  
  def containsPowerup() : Boolean = {
    gameObjects.filter(gameObject =>  
      gameObject.isInstanceOf[Powerup]).nonEmpty
  }
      
  /**
   * Add a gameObject and return the new GameFieldCell
   */
  def addHere(gameObject : GameObject) : GameFieldCell = {
    this.updated(gameObjects.+(gameObject))
  }
  
  /**
   * Move a PlayerToken to the Cell
   */
  def moveHere (playerToken : PlayerToken) 
  		: GameFieldCell = {
    logger.init(playerToken + " arriving at " + this.coords)
    
    val zombieList = gameObjects.filter(
        hostObject =>	hostObject.isInstanceOf[ZombieToken])
    
    val hostList = if (zombieList.size > 0) zombieList 
        					 else gameObjects
        					 
    val finalHostObjects = scala.collection.mutable.HashSet[GameObject]()
    var finalPlayerToken : PlayerToken = playerToken.updated(coords)
    for (hostObject <- hostList) { 
      hostObject match {
        // PlayerToken acts on every versatile object in Cell
      	case versatileHostObject : VersatileGameObject => {
      		versatileHostObject isVisitedBy finalPlayerToken match {
      			case (hostObjectResult, playerTokenResult : PlayerToken) => {
      			  // Delete the old object from the cell
    			  	finalHostObjects.-=(versatileHostObject)
    			  	
    			  	// It would be null if it was a coin or powerup
    			  	if(hostObjectResult != null)
    			  	  finalHostObjects.+=(hostObjectResult)
    			  	  
    			  	finalPlayerToken = playerTokenResult
      			}
      		}
      	}
      }
    }
    // add final visitorObjectResult 
    finalHostObjects += finalPlayerToken
    
    this.updated(finalHostObjects.toSet)
  }
  
  /**
   * Remove an object from the Cell
   */
  def remove(leavingObject : GameObject) : GameFieldCell = {    
    val toRemove = this.gameObjects
    									 		.filter(go => go.id == leavingObject.id).head
    logger.init(toRemove + " leaving " + this.coords)
    
  	this.updated(this.gameObjects.-(toRemove))
  }
  
  def updatedWithReplacement(replacementObject : GameObject) : GameFieldCell = {
    val toRemove = this.gameObjects
									 		.filter(go => go.id == replacementObject.id).head
		this.updated(this.gameObjects.-(toRemove).+(replacementObject))
  }
  
  /**
   * Create an updated GameFieldCell from new GameObjects
   */
  def updated(gameObjects : Set[GameObject]) = { 
    val newGameFieldCell = new GameFieldCell(this.coords, gameObjects)
    newGameFieldCell.logger.merge(this)
    newGameFieldCell
  }
}