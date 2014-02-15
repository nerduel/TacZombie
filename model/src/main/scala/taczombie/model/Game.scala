package taczombie.model

import GameState.GameState
import util.CoordinateHelper.intIntTuple2Wrapper
import scala.collection.mutable.ListBuffer

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
           val gameState : GameState = GameState.InGame) {
  
	def executeCommand(gameCommand : GameCommand) 
			: (Game, List[String]) = {
	  
	  val commandLog = ListBuffer[String]("Executing command" + gameCommand)
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
	        commandLog.+=:("\n\tIllegal GameCommand + " + moveCmd +  " for GameState " + updatedGameState)
	        (this, commandLog)
	      }
	      
	      val currentPlayer = players.currentPlayer
	      val currentToken = gameField.findOnePlayerTokenById(currentPlayer.currentTokenId)      
	      val currentTokenCoords = currentToken.coords
	      
	      var currentTokenIsMovable = true
	      
	      // check if token is frozen
        if(currentToken.frozenTime > 0) {
        	commandLog.+=("\n\tCurrent token is frozen for " + currentToken.frozenTime
        	    + " rounds.")
        	currentTokenIsMovable = false
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
            commandLog.+=("\n\tIllegal move to + " + destinationCoords + ". IT'S A WALL!")
          	return (this, commandLog.toList)
          }        
      
          // process the actual move. dead tokens will have .dead = true
          commandLog.+=("\n\tMoving " + currentToken.id + " to " + destinationCoords)
          updatedGameField = 
            gameField.move(currentToken, destinationCoords, commandLog)
                    	     
  	      // check if the player has collected all the coins
  	      if(currentPlayer.coins(updatedGameField) 
  	      		== gameField.coinsPlaced) {
  	      	commandLog.+=("\n\tAll coins collected! We have a winner")
  	      	updatedGameState = GameState.Win
  	      }
          
          // decrease player's remaining moves
          updatedPlayer = currentPlayer.updatedMoved
          updatedPlayers = players.updatedExistingPlayer(updatedPlayer)
          
          // TODO decrease current player's token's freezetimes
          // TODO decrease current players powerup if it's a human
  	      // TODO check for lifesRemaining for player
          
  	      // check for movesRemaining for player
          if (updatedPlayer.movesRemaining == 0) {
            commandLog.+=("\n\tPlayer " + updatedPlayer.name 
                + " has reached moves. Switching..:")
            updatedPlayers = updatedPlayers.updatedRotatedPlayers()
          }

          // TODO check if all HumanTokens are dead

	      } // if(currentTokenIsMovable)
	  }
	  println(commandLog)
	  
	  
	  if(updatedGameField == null) updatedGameField = gameField
	  if(updatedPlayers == null) updatedPlayers = players
	  if(updatedGameState == null) updatedGameState = gameState	  
	  
    (new Game(id, updatedGameField, 
        			updatedPlayers, updatedGameState),
    commandLog.toList)
	}
	
	
	//private def cycleTokenMap(tokenMap : TreeMap[Int,PlayerToken]) : Player
}