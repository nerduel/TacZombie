package taczombie.model

import scala.collection.immutable.HashSet
import scala.collection.immutable.TreeMap

import util.GameHelper._

class GameField(val id : String,
    						val gameFieldCells : Map[(Int,Int),GameFieldCell],
                val levelWidth : Int,
                val levelHeight : Int,
    						val humanBase : (Int, Int),
    						val zombieBase : (Int, Int),                
                val coinsPlaced : Int) {

  /**
   * This method moves a player's active token to destination coordinates.
   * @param spawning If a token is spawned it does not credit a real move 
   * @return Updated Game, List of changed Cells, Updated PlayerMap 
   */
  def move(destinationCoords : (Int, Int), 
        	 players : Players,
        	 spawning : Boolean = false,
        	 fakeMove : Boolean = false) 
    		: (GameField, List[GameFieldCell], Players) = {
  	
   
    val (movingPlayer, movingToken) = { 
      if(spawning) { 
      	(players.currentPlayer, players.currentPlayer.currentToken)
      } else {
        (players.currentPlayer.updatedMoved(), 
         players.currentPlayer.currentToken.updatedMoved())
      }
    }

    // TODO debug print 
    println("moving " + movingToken + " to " + destinationCoords)

    val updatedSourceCell = gameFieldCells.apply(movingToken.coords)
    																			.remove(movingToken)
    val (updatedDestinationCell, updatedMovingToken) =
    	gameFieldCells.apply(destinationCoords).moveHere(movingToken)
    val updatedGameFieldCells = List[GameFieldCell](updatedSourceCell, 
          																					updatedDestinationCell)

    var finalPlayers : Players = players.updatedExistingPlayer(movingPlayer)																			
    finalPlayers = finalPlayers.updatedFromUpdatedGameFieldCell(updatedDestinationCell)
    
    // TODO debug print 
    //finalPlayerMap.foreach(player => println(player)) 
    
    (this.updated(updatedGameFieldCells),
     updatedGameFieldCells,
     finalPlayers)
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
  	    					this.coinsPlaced)
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
  		: (GameFieldCell, PlayerToken) = {
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
      			  if(hostObjectResult != null)
      			  	finalHostObjects = finalHostObjects.-(hostObjectResult)
      			  																		 .+(hostObjectResult)
              finalPlayerToken = playerTokenResult
      			}
      		}
      	}
      }
    }
    
    // add final visitorObjectResult 
    finalHostObjects += finalPlayerToken
    
    (this.updated(finalHostObjects), finalPlayerToken)
  }
  
  /**
   * Remove an object from the Cell
   */
  def remove(leavingObject : GameObject) : GameFieldCell =
  	this.updated(gameObjects.-(leavingObject))
  
  /**
   * Create an updated GameFieldCell from new GameObjects
   */
  def updated(gameObjects : HashSet[GameObject]) = 
    new GameFieldCell(this.coords, gameObjects)
}

