package taczombie.model

import GameState.GameState
import util.CoordinateHelper.intIntTuple2Wrapper
import scala.collection.mutable.ListBuffer
import taczombie.model.util.Logger

object defaults {
  val humanLifes = 3
  
  val humanMoves = 4
  val zombieMoves = 2
  
  val humanName = "Pacman"
  val zombieName = "Zombie"
    
  val killScore = 3
}

object GameMessages {
  def noMsg = "Chase them!"
  def newGame = "New Game is ready. Have fun!"
  def frozenToken(rounds : Int) = "You're token is frozen for " + rounds + 
  	"rounds. If you have more Tokens switch to them, otherwise switch players!"
  def wall = "*OUCH* You ran against a wall..."
    
  def noRemainingMoves = "You have no moves remaining. Please switch players."
}

class Game(val id : Int,
    	   	 val gameField : GameField,
           val players : Players, // first player is current player!
           val gameState : GameState = GameState.InGame,
           val lastGameMessage : String = GameMessages.newGame) extends Logger {
  
	def executeCommand(gameCommand : GameCommand) 
			: Game = {
	  
	  logger.init("Executing command" + gameCommand)
	  var updatedGameField : GameField = null
	  var updatedPlayer : Player = null
	  var updatedPlayers : Players = null
	  var updatedGameState : GameState = null
	  var updatedGameMessage : String = null
	  
    val currentPlayer = players.currentPlayer
    val currentToken = gameField.findOnePlayerTokenById(currentPlayer.currentTokenId)      
    val currentTokenCoords = currentToken.coords	  
	  
	  (gameCommand, gameState) match {
 	    // TODO nextGame
 	    // TODO nextToken	   
	
	    case (moveCmd : MoveCmd, GameState.InGame) => {
	      
	      // check if token is frozen
        if(currentToken.frozenTime > 0) {
        	logger.+=("Current token is frozen for " + currentToken.frozenTime
        	    + " rounds.")
        	
        	updatedGameMessage = GameMessages.frozenToken(currentToken.frozenTime)    
          updatedGameState = GameState.NeedTokenSwitch
          return updated(newGameState = updatedGameState,
              					 newGameMessage = updatedGameMessage)
        }
	      
	      val destinationCoords = moveCmd match {
  	      case MoveUp => currentTokenCoords.aboveOf
    	    case MoveDown => currentTokenCoords.belowOf
    	    case MoveLeft => currentTokenCoords.leftOf
    	    case MoveRight => currentTokenCoords.rightOf
	      }
	      
	      // ignore requests to walk into a wall    
        if(gameField.gameFieldCells.apply(destinationCoords).containsWall) {
          logger.+=("Illegal move to + " + destinationCoords + ". IT'S A WALL!")
        	return updated(gameField, players, gameState, GameMessages.wall)
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
          updatedGameMessage = GameMessages.noRemainingMoves
          logger.+=(updatedGameMessage)
          updatedGameState = GameState.NeedPlayerSwitch
        }

        // TODO check if all HumanTokens are dead
    	  logger.print
    	  
    	  if(updatedGameField == null) updatedGameField = gameField
    	  if(updatedPlayers == null) updatedPlayers = players
    	  if(updatedGameState == null) updatedGameState = gameState
    	  if(updatedGameMessage == null) updatedGameMessage = GameMessages.noMsg
    	  
        return updated(updatedGameField, updatedPlayers, 
            					 updatedGameState, updatedGameMessage)
	  	}
	    
  	  case (NextPlayer, GameState.NeedPlayerSwitch | 
  	      							GameState.InGame |
  	      							GameState.NeedTokenSwitch) => {		  
        
  	    // TODO check if tokens can be respawned
  	      							  
        return updated(newPlayers = players.updatedRotatedPlayers(),
            					 newGameState = GameState.InGame)
  		}

  	  case (NextToken, GameState.NeedPlayerSwitch | 
  	      							GameState.InGame |
  	      							GameState.NeedTokenSwitch) => {
  		  this
  		  // TODO
  		}
  	    
 
    	    
  	  case (gameCmd : GameCommand, _) => {
  	    updatedGameMessage = "Unsupported GameCommand " +
  	    		gameCmd	+ " for GameState " + gameState
      	logger.+=(updatedGameMessage)
    	  return updated(newGameMessage = updatedGameMessage)
      }
	  }
	  
	} // def executeCommand(gameCommand : GameCommand)
	
	private def updated(newUpdatedGameField : GameField = this.gameField,
	    								newPlayers : Players = this.players,
	    								newGameState : GameState = this.gameState,
	    								newGameMessage : String = GameMessages.noMsg) : Game = {
	  val updatedGame = new Game(id, newUpdatedGameField, 
        			newPlayers, newGameState, newGameMessage)
	  updatedGame.logger.merge(this)
	  updatedGame
	}
	
	//private def cycleTokenMap(tokenMap : TreeMap[Int,PlayerToken]) : Player
}