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
  def moveHere (visitorPlayerToken : PlayerToken) 
  		: GameFieldCell = {
    logger.init(visitorPlayerToken + " arriving at " + this.coords, true)
    
    val updatedHostObjects = scala.collection.mutable.HashSet[GameObject]()
    var updatedVisitorPlayerToken : PlayerToken = visitorPlayerToken.updated(coords)
    
    
    // visitorPlayerToken fights the local living enemies first
    val hostLivingEnemies = gameObjects.filter(go => go match {
      case hostLivingEnemy : PlayerToken => 
        (!hostLivingEnemy.dead && 
            hostLivingEnemy.getClass() != visitorPlayerToken.getClass())
      case _=> false
    })
    
    // keep the rest in case the visitor dies
    val restHostObjects = gameObjects.--(hostLivingEnemies)
    
    for(hostLivingEnemy <- hostLivingEnemies) {
      hostLivingEnemy match {case hostLivingEnemy : VersatileGameObject =>
      hostLivingEnemy isVisitedBy updatedVisitorPlayerToken match {
        case (hostLivingEnemyResult : PlayerToken,
            		 updatedVisitorPlayerTokenResult : PlayerToken) => {
            	  
	        updatedHostObjects += hostLivingEnemyResult
	        updatedVisitorPlayerToken = updatedVisitorPlayerTokenResult
        } case _ => logger += ("Unexpected match")
      } case _ => logger += ("Unexpected match")}
    }
    
    // dead visitors can't pass to the rest
    if(!updatedVisitorPlayerToken.dead) {
      for(restHostObject <- restHostObjects) {
        restHostObject match { case restHostObject : VersatileGameObject =>
        restHostObject isVisitedBy updatedVisitorPlayerToken match {
          case (restHostObjectResult : VersatileGameObject,
              	updatedVisitorPlayerTokenResult : PlayerToken) => {

            updatedHostObjects += restHostObjectResult	
  	        updatedVisitorPlayerToken = updatedVisitorPlayerTokenResult
          } case _ => logger += ("Unexpected match")
        } case _ => logger += ("Unexpected match") }
      }
    } 
    else {
      restHostObjects.foreach(rHO => updatedHostObjects += rHO) 
    }
      					 

    return this.updated(updatedHostObjects.toSet + updatedVisitorPlayerToken)
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
    newGameFieldCell.logger merge this
    newGameFieldCell
  }
}