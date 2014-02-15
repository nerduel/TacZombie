package taczombie.model

import util.GameHelper._

import scala.collection.immutable.HashSet
import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer

class GameField(val id : String,
    						val gameFieldCells : Map[(Int,Int),GameFieldCell],
                val levelWidth : Int,
                val levelHeight : Int,
    						val humanBase : (Int, Int),
    						val zombieBase : (Int, Int),                
                val coinsPlaced : Int,
                val lastUpdatedGameFieldCells : List[GameFieldCell] = null) {

  def findPlayerTokensById(tokenIds : List[Int]) : List[PlayerToken] = {
    val playerTokensMap = scala.collection.mutable.HashMap[Int, PlayerToken]();
    
    for(gameFieldCellWithPlayerToken <- gameFieldCells.filter(gameFieldCell => {
                            gameFieldCell._2.containsHumanToken ||
                            gameFieldCell._2.containsZombieToken})) {
      for(searchedPlayerToken <- 
          	gameFieldCellWithPlayerToken._2.gameObjects.filter(gameObject 
          			=> tokenIds.contains(gameObject.id))) {
        					playerTokensMap.+=((searchedPlayerToken.id, 
    				    								searchedPlayerToken.asInstanceOf[PlayerToken]))
      }
    }
    playerTokensMap.values.toList
  }
  
  def findOnePlayerTokenById(id : Int) : PlayerToken = 
  		findPlayerTokensById(List[Int](id)).head
  
  /**
   * This method moves a token to destination coordinates.
   */
  def move(movingPlayerToken : PlayerToken,
      		 destinationCoords : (Int, Int),
      		 commandLog : ListBuffer[String])
    		: GameField = {
  	

    val updatedSourceCell =    
    		gameFieldCells.apply(movingPlayerToken.coords).remove(movingPlayerToken)
    		
    val updatedDestinationCell =   
   			gameFieldCells.apply(destinationCoords).moveHere(movingPlayerToken)
    val updatedGameFieldCells = List[GameFieldCell](updatedSourceCell, 
          																					updatedDestinationCell)
    this.updated(updatedGameFieldCells)
  }
  
  // TODO: Check if obsolete
  //def getCopyOfData = for (line <- data.map(_.clone)) yield line

  private def isValid(pos : (Int, Int)) = {
      if ((pos._1 < levelHeight) && (pos._1 >= 0) &&
          (pos._2 < levelWidth) && (pos._2 >= 0))
          true
      else
          false
  }
  
  /**
   * Update a GameField's Cells
   */
  private def updated(updatedCells : List[GameFieldCell]) : GameField = {
    var newData = gameFieldCells
    
    for(cell <- updatedCells)
      newData.+=((cell.coords, cell))

  	new GameField(this.id,
  	    					newData,
  	    					this.levelWidth,
  	    					this.levelHeight,
  	    					this.humanBase,
  	    					this.zombieBase,                
  	    					this.coinsPlaced,
  	    					updatedCells)
  }
}

class GameFieldCell(val coords : (Int, Int),
    								val gameObjects : HashSet[GameObject]) {

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
    val zombieList = gameObjects.filter(
        hostObject =>	hostObject.isInstanceOf[ZombieToken])
    
    val hostList = if (zombieList.size > 0) zombieList 
        					 else gameObjects
    
    var finalHostObjects = HashSet[GameObject]()
    var finalPlayerToken : PlayerToken = playerToken.updated(coords)
    for (hostObject <- hostList) { 
      hostObject match {
      	case versatileHostObject : VersatileGameObject => {
      		versatileHostObject isVisitedBy finalPlayerToken match {
      			case (hostObjectResult, playerTokenResult : PlayerToken) => {
    			  	finalHostObjects = finalHostObjects.-(versatileHostObject)
    			  	if(hostObjectResult.isInstanceOf[PlayerToken] &&
    			  	    hostObjectResult.asInstanceOf[PlayerToken].dead == true) 
    			  	{} // don't add dead PlayerTokens to the gameFieldCell
    			  	else if(hostObjectResult != null) {
    			  	  finalHostObjects = finalHostObjects.+(hostObjectResult)
  			  	  }
    			  	finalPlayerToken = playerTokenResult
      			}
      		}
      	}
      }
    }
    
    // add final visitorObjectResult 
    finalHostObjects += finalPlayerToken
    
    this.updated(finalHostObjects)
  }
  
  /**
   * Remove an object from the Cell
   */
  def remove(leavingObject : GameObject) : GameFieldCell = {
    val toRemove = this.gameObjects
    									 		.filter(go => go.id == leavingObject.id).head
  	this.updated(this.gameObjects.-(toRemove))
  }
  
  /**
   * Create an updated GameFieldCell from new GameObjects
   */
  def updated(gameObjects : HashSet[GameObject]) = 
    new GameFieldCell(this.coords, gameObjects)
}

