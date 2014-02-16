package taczombie.model

import GameState.GameState
import util.CoordinateHelper.intIntTuple2Wrapper
import scala.collection.mutable.ListBuffer
import taczombie.model.util.Logger
import java.util.Calendar

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
  def won = "All coins collected! We have a winner"
  def gameOver = "Game Over!"
  def tokenDied = "You ran into death! Please switch players."
  def deadTokenSelected = "Your last selected token is dead. Please use \"respawn token\"" +
  												" or switch players." 
}

class Game(val id : Int,
    	   	 val gameField : GameField,
           val players : Players, // first player is current player!
           val gameState : GameState = GameState.InGame,
           val lastGameMessage : String = GameMessages.newGame,
           val mergeLog : Logger = null) extends Logger {
  
  var startTime : Long = 0
  
  if(mergeLog != null)
    logger.merge(mergeLog)
  
	def executeCommand(gameCommand : GameCommand) 
			: Game = {
    
    startTime = Calendar.getInstance().getTimeInMillis()
	  
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
        	logger += "Current token is frozen for " + currentToken.frozenTime +
        	    " rounds."
        	
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
        
        // decrease player's remaining moves
        updatedPlayer = currentPlayer.updatedMoved
        updatedPlayers = players.updatedExistingPlayer(updatedPlayer)

        // decrease current players powerup if it's a human
        // decrease current player's token's freezetimes
        updatedGameField = updatedGameField.updatedDecrementedCounters(updatedPlayer)
        
	      // check is won      
        if(gameIsWon(updatedPlayers, updatedGameField)) {
	        updatedGameMessage = GameMessages.won
	      	logger += updatedGameMessage
	      	return updated(updatedGameField, updatedPlayers, GameState.Win, updatedGameMessage)
	      }
        
        // check if the game is lost     
        if(gameIsLost(updatedPlayers, updatedGameField)) {
            updatedGameMessage = GameMessages.gameOver
            logger += updatedGameMessage
            return updated(updatedGameField, updatedPlayers, 
                					 GameState.GameOver, updatedGameMessage)
        }

        // check if current token is dead
        if(updatedPlayer.currentToken(updatedGameField).dead) {
          updatedGameMessage = GameMessages.tokenDied
          updatedGameState = GameState.NeedPlayerSwitch
                    
          return updated(newGameField = updatedGameField,
              					 newPlayers = updatedPlayers,
              					 newGameState = updatedGameState,
        	    					 newGameMessage = updatedGameMessage)
        }
        
              
	      // check for movesRemaining for player
        logger += "Player " + updatedPlayer.name + " can move "
              + updatedPlayer.movesRemaining + " more times."
              
        if (updatedPlayer.movesRemaining == 0) {
          updatedGameMessage = GameMessages.noRemainingMoves
          logger += updatedGameMessage
          return updated(newGameField = updatedGameField,
              					 newPlayers = updatedPlayers,
              					 newGameState = GameState.NeedPlayerSwitch,
              					 newGameMessage = updatedGameMessage)
        }
        
        // TODO check if all HumanTokens are dead
    	  
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
  	    
  	    updatedGameField = gameField.updatedDecrementedCounters(currentPlayer)
  	    updatedPlayers = players.updatedRotatedPlayers
  	    updatedGameState = GameState.InGame
      	updatedGameMessage = GameMessages.noMsg
  	   
      	updatedPlayer = updatedPlayers.currentPlayer
      	logger += "Next player is " + updatedPlayer + " with token " + 
      						updatedPlayer.currentToken(updatedGameField)
      	
  	    // check if next player's currentToken is dead
  	    if(updatedPlayer.currentToken(updatedGameField).dead) {
          // if it is a zombie. respawn it
          updatedPlayer match { 
            case zombie : Zombie => 
          		updatedPlayer.currentToken(updatedGameField)
            	updatedGameField = updatedGameField respawn 
            										 updatedPlayer.currentToken(updatedGameField).id
            	logger merge updatedGameField
            	
          	case human : Human =>
          	  updatedGameState = GameState.NeedTokenSwitch
          	  updatedGameMessage = GameMessages.deadTokenSelected
          	  logger += updatedGameMessage
          }
  	    }
  	    
        return updated(newGameField = updatedGameField,
            					 newPlayers = updatedPlayers,
            					 newGameState = updatedGameState,
            					 newGameMessage = updatedGameMessage)
  		}
  	      							
//  	  case (RespawnToken, GameState.NeedTokenSwitch) => {
//        // check if next player's currentToken is dead
//  	  	if(currentToken.dead) {
//  	  	  
//  	  	}
//  	  	
//  	  	this
//  	  }

  	  case (NextToken, GameState.NeedPlayerSwitch | 
  	      						 GameState.InGame |
  	      						 GameState.NeedTokenSwitch) => {
  		  this
  		  // TODO
  		}
  	  
  	  case (_, GameState.NeedPlayerSwitch |
  	      	   GameState.NeedTokenSwitch) =>
     	  return updated(newGameMessage = lastGameMessage)  	    
  	  
  	      						 
  	  case (gameCmd : GameCommand, _) => {
  	    updatedGameMessage = "Unsupported GameCommand " +
  	    		gameCmd	+ " for GameState " + gameState
      	logger += updatedGameMessage
    	  return updated(newGameMessage = updatedGameMessage)
      }
	  }
	  
	} // def executeCommand(gameCommand : GameCommand)
	
	private def updated(newGameField : GameField = this.gameField,
	    								newPlayers : Players = this.players,
	    								newGameState : GameState = this.gameState,
	    								newGameMessage : String = GameMessages.noMsg) : Game = {
	  logger += "Execution time:  " + 
    	  					 (Calendar.getInstance().getTimeInMillis() - startTime) +
    	  					" ms"
	  new Game(id, newGameField, 
        		 newPlayers, newGameState, newGameMessage, this)
	}
	
  private def gameIsWon(players : Players, gameField : GameField) : Boolean = {
    val coinsCollected = players.playerList.foldLeft(0){(sum, player) => 
      player match { 
        case human : Human => sum + human.coins(gameField)
        case _ => sum
      }
    }
    coinsCollected	== gameField.coinsPlaced  
  }	

  private def gameIsLost(players : Players, gameField : GameField) : Boolean = {
  	val humanLifes = players.playerList.foldLeft(0){(sum, player) => 
      player match { 
        case human : Human => sum + human.lifes
        case _ => sum
      }
    }
    humanLifes == 0 && 
    		gameField.findHumanTokens.filter(hT => hT.dead).size == 0
  } 

}