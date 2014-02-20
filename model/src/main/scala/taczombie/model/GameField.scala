package taczombie.model

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer
import taczombie.model.util.Logger
import scala.util.Random

class GameField(val id : String,
    						val gameFieldCells : Map[(Int,Int),GameFieldCell],
                val levelWidth : Int,
                val levelHeight : Int,
    						val humanBase : (Int, Int),
    						val zombieBase : (Int, Int),                
                val coinsPlaced : Int,
                val lastUpdatedGameFieldCells : List[GameFieldCell] = null,
                val mergeLog : Logger = null)
                extends Logger {
  
  if(mergeLog != null)
    logger merge mergeLog

  /**
   * Get all PlayerTokens with the specified id
   */
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
  
  /**
   * Get the PlayerToken with the specified id
   */
  def findOnePlayerTokenById(id : Int) : PlayerToken = {
    val playerTokens = findPlayerTokensById(List[Int](id))
    if(playerTokens.isEmpty) {
      logger += ("Did not find any playerTokens!")
      null // FIXME exception
    } else playerTokens.head
  }

  /**
   * Find all HumanTokens on the gameField 
   */
  def findHumanTokens : List[HumanToken] = {
    gameFieldCells.foldLeft(List[HumanToken]())({(resultList, cell) =>
      resultList ++ cell._2.gameObjects.collect({case go : HumanToken => go})
    })
  }
  	
  /*
   * This method decreases the current players token's values
   * for frozenTime and powerupTime, according to Player subclass
   */
  def updatedDecrementedCounters(player : Player) : GameField = {
  	val updatedGameFieldCells = ListBuffer[GameFieldCell]() 
    for (gameFieldCellWithPlayerToken <- gameFieldCells.filter(gameFieldCell => {
                            gameFieldCell._2.containsLivingHumanToken ||
                            gameFieldCell._2.containsLivingZombieToken})) {
      var tmpGameFieldCell = gameFieldCellWithPlayerToken._2
      for (playersToken <- gameFieldCellWithPlayerToken._2.gameObjects.filter
          								(token => player.playerTokenIds.contains(token.id))) {
        playersToken match {
          case playerToken : PlayerToken =>
          	tmpGameFieldCell = tmpGameFieldCell.updatedWithReplacement(
          	    													playerToken.updatedDecrementCounters)
          case _ => // don't touch other tokens
        }
      }
      updatedGameFieldCells += tmpGameFieldCell
    }
  	this.updated(updatedGameFieldCells.toList)
  }
  		
  /**
   * This method moves a token to destination coordinates.
   */
  def move(movingPlayerToken : PlayerToken,
      		 destinationCoords : (Int, Int))
    		: GameField = {
    
    val updatedSourceCell =    
    		gameFieldCells.apply(movingPlayerToken.coords).remove(movingPlayerToken)
    		
    val updatedDestinationCell =   
   			gameFieldCells.apply(destinationCoords).moveHere(movingPlayerToken)
    val updatedGameFieldCells = List[GameFieldCell](updatedSourceCell, 
          																					updatedDestinationCell)
    
    logger.merge(updatedSourceCell)
    logger.merge(updatedDestinationCell)
    
    this.updated(updatedGameFieldCells)
  }
  
  /**
   * Respawn a dead token on a random empty spot
   */
  def respawn(tokenId : Int) : GameField = {
    
    
    
    val updatedGameFieldCells = ListBuffer[GameFieldCell]()
        
    val respawnToken = findOnePlayerTokenById(tokenId)

    val updatedSourceCell = gameFieldCells.apply(respawnToken.coords)
    																			.remove(respawnToken)
    																			
	  val randomSpawnCoords = getRandomSpawnCoords(respawnToken)
	  
	  if(randomSpawnCoords == null)
	    return this
	    
    val updatedRespawnToken = respawnToken.updated(
                                newCoords = getRandomSpawnCoords(respawnToken), 
                                newPowerupTime = defaults.defaultSpawnPowerupTime, 
                                newFrozenTime = defaults.defaultSpawnFreeze, 
                                newDead = false)
       
    logger += "Respawned " + updatedRespawnToken
                                
    updatedGameFieldCells += updatedSourceCell
    updatedGameFieldCells += gameFieldCells.apply(updatedRespawnToken.coords)
    																			 .addHere(updatedRespawnToken)
    																			 
    updated(updatedGameFieldCells.toList)
  }
  
  /**
   * Respawn several dead tokens on a random empty spot
   */
  def respawn(tokenIds : List[Int]) : GameField = {
    val updatedGameFieldCellsMap = 
      scala.collection.mutable.HashMap[(Int,Int),GameFieldCell]()
      
    var updatedGameField = this
    for(tokenId <- tokenIds) {
      updatedGameField = updatedGameField respawn tokenId
      updatedGameField.lastUpdatedGameFieldCells.foreach(ugfc => {
        updatedGameFieldCellsMap.update(ugfc.coords, ugfc)})
    }
    updatedGameField.updated(updatedGameFieldCellsMap.values.toList)  
  }
  
  /**
   * Find a random empty cell
   */
  private def getRandomSpawnCoords(playerToken : PlayerToken) : (Int, Int) = {
    var randomCoords : (Int, Int) = null    
    
    val candidateCells = gameFieldCells.values.filter({cell =>
      !cell.containsWall && 
      !cell.containsLivingZombieToken &&
      !cell.containsLivingHumanToken
    })
    
    if(candidateCells.size > 0) {
      val random = new Random(System.currentTimeMillis())
      randomCoords = 
        candidateCells.toList.apply(random.nextInt(candidateCells.size)).coords
    }
      
    if(randomCoords == null)
  	  logger += ("There is no random spot to respawn!")
  	randomCoords
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
  	    					updatedCells,
  	    					this)
  }
}