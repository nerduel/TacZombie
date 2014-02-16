package taczombie.model

import GameState.GameState
import util.CoordinateHelper.intIntTuple2Wrapper
import scala.collection.mutable.ListBuffer
import taczombie.model.util.Logger

object defaults {
  val humanMoves = 4
  val lifes = 3
  val zombieMoves = 2
  
  val humanName = "Pacman"
  val zombieName = "Zombie"
    
  val killScore = 3
}

class Game(val id : Int,
    	   	 val gameField : GameField,
           val players : Players, // first player is current player!
           val gameState : GameState = GameState.InGame) extends Logger {
  
	def executeCommand(gameCommand : GameCommand) 
			: Game = {
	  
	  logger.init("Executing command" + gameCommand)
	  var updatedGameField : GameField = null
	  var updatedPlayer : Player = null
	  var updatedPlayers : Players = null
	  var updatedGameState : GameState = null
	  
	  gameCommand match {
 	    // TODO nextGame
 	    // TODO nextToken
	    
	    case moveCmd : Move =>
	      
	      // only move if inGame
	      if(gameState != GameState.InGame) {
	        logger.+=("Illegal GameCommand + " + moveCmd +  " for GameState " + updatedGameState)
	        return this
	      }
	      
	      
	      val currentPlayer = players.currentPlayer
	      val currentToken = gameField.findOnePlayerTokenById(currentPlayer.currentTokenId)      
	      val currentTokenCoords = currentToken.coords
	      
	      // TODO check if tokens can be respawned
	      
	      var currentTokenIsMovable = true
	      
	      // check if token is frozen
        if(currentToken.frozenTime > 0) {
        	logger.+=("Current token is frozen for " + currentToken.frozenTime
        	    + " rounds.")
        	currentTokenIsMovable = false
        	
          // decrease current players powerup if it's a human
          // decrease current player's token's freezetimes
          updatedGameField = gameField.updatedDecrementedCounters(currentPlayer)
        }
	      
	      if(currentTokenIsMovable) {
  	      val destinationCoords = moveCmd match {
    	      case MoveUp => currentTokenCoords.aboveOf
      	    case MoveDown => currentTokenCoords.belowOf
      	    case MoveLeft => currentTokenCoords.leftOf
      	    case MoveRight => currentTokenCoords.rightOf
  	      }
  	      
  	      // ignore requests to walk into a wall    
          if(gameField.gameFieldCells.apply(destinationCoords).containsWall) {
            logger.+=("Illegal move to + " + destinationCoords + ". IT'S A WALL!")
          	return this
          }        
      
          // process the actual move. dead tokens will have .dead = true
          updatedGameField = 
            gameField.move(currentToken, destinationCoords)
          logger.merge(gameField)
                    	     
  	      // check if the player has collected all the coins
  	      if(currentPlayer.coins(updatedGameField) 
  	      		== gameField.coinsPlaced) {
  	      	logger.+=("All coins collected! We have a winner")
  	      	updatedGameState = GameState.Win
  	      }
          
          // decrease player's remaining moves
          updatedPlayer = currentPlayer.updatedMoved
          updatedPlayers = players.updatedExistingPlayer(updatedPlayer)

          // decrease current players powerup if it's a human
          // decrease current player's token's freezetimes
          updatedGameField = updatedGameField.updatedDecrementedCounters(updatedPlayer)
          
          
  	      // check for movesRemaining for player
          logger.+=("Player " + updatedPlayer.name + " can move "
                + updatedPlayer.movesRemaining + " more times.")
          if (updatedPlayer.movesRemaining == 0) {
            logger.+=("Switching players...")
            updatedPlayers = updatedPlayers.updatedRotatedPlayers()
          }

          // TODO check if all HumanTokens are dead

	      } // if(currentTokenIsMovable)
	  }
	  logger.print
	  
	  
	  if(updatedGameField == null) updatedGameField = gameField
	  if(updatedPlayers == null) updatedPlayers = players
	  if(updatedGameState == null) updatedGameState = gameState	  
	  
    updated(updatedGameField, updatedPlayers, updatedGameState)
	}
	
	private def updated(newUpdatedGameField : GameField,
	    								newPlayers : Players,
	    								newGameState : GameState) : Game = {
	  val updatedGame = new Game(id, newUpdatedGameField, 
        			newPlayers, newGameState)
	  updatedGame.logger.merge(this)
	  updatedGame
	}
	
	//private def cycleTokenMap(tokenMap : TreeMap[Int,PlayerToken]) : Player
}