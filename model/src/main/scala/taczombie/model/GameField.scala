package taczombie.model

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer

import taczombie.model.util.Logger
import util.GameHelper._

class GameField(val id : String,
    						val gameFieldCells : Map[(Int,Int),GameFieldCell],
                val levelWidth : Int,
                val levelHeight : Int,
    						val humanBase : (Int, Int),
    						val zombieBase : (Int, Int),                
                val coinsPlaced : Int,
                val lastUpdatedGameFieldCells : List[GameFieldCell] = null)
                extends Logger {

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
  
  type A <: GameObject	
  	
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



