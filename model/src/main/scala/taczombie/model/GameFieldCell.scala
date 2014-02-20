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
    gameObjects.collect({
      case zombieToken : ZombieToken if !zombieToken.dead => zombieToken
      }).nonEmpty
  }
  
  def containsLivingHumanToken() : Boolean = {
    gameObjects.collect({
        case humanToken : HumanToken if !humanToken.dead => humanToken
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
    logger.init(visitorPlayerToken + " arriving at " + this.coords, false)
    var updatedVisitorPlayerToken : PlayerToken = visitorPlayerToken.updated(coords)
    
    val updatedHostObjects = scala.collection.mutable.HashSet[GameObject]()
    val hostLivingPlayerTokens = scala.collection.mutable.HashSet[VersatileGameObject]()
    val collectableHostObjects = scala.collection.mutable.HashSet[VersatileGameObject]()
    val restHostObjects = scala.collection.mutable.HashSet[GameObject]()
    
    gameObjects.foreach{ go => go match {
        case pt : PlayerToken => 
          if(pt.getClass() != visitorPlayerToken.getClass() && !pt.dead)
            hostLivingPlayerTokens += pt
          else
            restHostObjects += pt
        case c : Collectable => collectableHostObjects += c
    	}
    }
    
    // visitor fights against living enemies
    
    val fightResultTuple = evaluateVisit(updatedVisitorPlayerToken,
        																 hostLivingPlayerTokens.toSet)
    updatedVisitorPlayerToken = fightResultTuple._1
    updatedHostObjects ++= fightResultTuple._2	
    
    // only living visitors can get the collectables
    if( ! updatedVisitorPlayerToken.dead) {
    	val collectableResultTuple = evaluateVisit(updatedVisitorPlayerToken,
    	    																			 collectableHostObjects.toSet)
    	updatedVisitorPlayerToken = collectableResultTuple._1
    	updatedHostObjects ++= collectableResultTuple._2
    } else {
      updatedHostObjects ++= collectableHostObjects 
    }

    return this.updated(updatedHostObjects.toSet ++ restHostObjects 
        								+ updatedVisitorPlayerToken)
  }
  
  private def evaluateVisit(visitor : PlayerToken, visitedObjects : Set[VersatileGameObject]) 
  	 : (PlayerToken,Set[VersatileGameObject]) =	{
    var updatedVisitor = visitor
    val updatedVisitedObjects = scala.collection.mutable.HashSet[VersatileGameObject]()
    for(visitedObject <- visitedObjects) {
      visitedObject isVisitedBy updatedVisitor match {
        case (null, updatedVisitorPlayerTokenResult : PlayerToken) => 
	        updatedVisitor = updatedVisitorPlayerTokenResult
	        
	      case (visitedObject, updatedVisitorPlayerTokenResult : PlayerToken) =>
	        updatedVisitor = updatedVisitorPlayerTokenResult
	        updatedVisitedObjects += visitedObject
      }
    }
    return (updatedVisitor, updatedVisitedObjects.toSet)
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