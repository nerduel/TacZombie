package taczombie.model

import GameState.GameState
import util.CoordinateHelper.intIntTuple2Wrapper
import scala.collection.mutable.ListBuffer
import taczombie.model.util.Logger
import java.util.Calendar

object defaults {
  val defaultHumanLifes = 3
  
  val defaultHumanMoves = 7
  val defaultZombieMoves = 4
  
  val defaultHumanName = "Pacman"
  val defaultZombieName = "Zombie"
  
  val defaultPowerupTime = 5
  
  val defaultSpawnPowerupTime = 1
  val defaultSpawnFreeze = 1
  
  val defaultHumans = 2
  val defaultZombies = 4
  val defaultHeight = 21
  val defaultWidth = 21
    
  val defaultKillScore = 3
}

object GameMessages {
  def noMsg = "Chase them!"
  def newGame = "New Game is ready. Have fun!"
  def frozenToken(rounds : Int) = "This token is frozen for " + rounds + 
  	" rounds. If you have more Tokens switch to them, otherwise switch players!"
  def wall = "*OUCH* You ran against a wall..."
  def respawnedToken(coords : (Int, Int)) = 
    "Respawned a Token to " + coords  
  def noRemainingMoves = "You have no moves remaining. Please switch players."
  def won = "All coins collected! We have a winner"
  def gameOver = "Game Over!"
  def tokenDied = "You ran into death! Please switch players."
  def deadTokenSelected = "Your last selected token is dead. Please try to " + 
    											"respawn or switch tokens"
  def noOthersAlive = "You have no other living tokens."
  def noRoomForToken = "There is no room for your token to respawn right now!"
  def invalidCmd(cmd : Any, s : GameState) = cmd + " does not work for " + s
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

	    case (moveCmd : MoveCmd, GameState.InGame) => {
	        
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

        // check for dead tokens
        val deadTokens = gameField
        
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
        
    	  if(updatedGameState == null) updatedGameState = gameState
    	  if(updatedGameMessage == null) updatedGameMessage = GameMessages.noMsg
    	  
        return updated(updatedGameField, updatedPlayers, 
            					 updatedGameState, updatedGameMessage)
	  	}
	    
  	  case (NextPlayer, GameState.NeedPlayerSwitch | 
  	      							GameState.InGame |
  	      							GameState.NeedTokenSwitch) => {		  

  	    updatedGameState = GameState.InGame
      	updatedGameMessage = GameMessages.noMsg  	      							  
  	      							  
  	    updatedPlayers = players.updatedRotatedPlayers
      	updatedPlayer = updatedPlayers.currentPlayer
      	logger += "Next player is " + updatedPlayer + " with token " + 
      						updatedPlayer.currentToken(gameField)
      	
        // decrement counters - which is frozentime and poweruptime for now
  	    updatedGameField = gameField.updatedDecrementedCounters(updatedPlayer)      						
      						
      	// check for dead tokens
        val playersDeadTokenIds = updatedPlayer.deadTokenIds(gameField)
        if(playersDeadTokenIds.size > 0) {
          updatedPlayer match {
            case zombie : Zombie => {
              // respawn all dead zombie tokens
              updatedGameField = 
                updatedGameField respawn playersDeadTokenIds
              	logger merge updatedGameField  
            }
            case human : Human => {
              val curTokenDead = updatedPlayer.currentToken(updatedGameField)
              																.dead
            	if(curTokenDead) {
            	  updatedGameState = GameState.NeedTokenSwitch
            	  updatedGameMessage = GameMessages.deadTokenSelected
            	  logger += updatedGameMessage                      	    
          	  }
            }
          }
        }
  	    
  	    val curTokenFrozentime = updatedPlayer.currentToken(updatedGameField)
  	    																			.frozenTime
        if(curTokenFrozentime > 0) {
          logger += ("frozen token: " + updatedPlayer.currentToken(updatedGameField)) 
      	  updatedGameState = GameState.NeedTokenSwitch
      	  updatedGameMessage = GameMessages.frozenToken(curTokenFrozentime)
      	  logger += updatedGameMessage
    	  }
  	    
        return updated(newGameField = updatedGameField,
            					 newPlayers = updatedPlayers,
            					 newGameState = updatedGameState,
            					 newGameMessage = updatedGameMessage)
  		}
  	      							
  	  case (RespawnToken, GameState.InGame |
  	      								GameState.NeedTokenSwitch) => {
        // check if player has dead tokens
  	    val deadTokens = currentPlayer.deadTokens(gameField)
  	  	if(deadTokens.nonEmpty) {
  	  	  if(currentPlayer.lifes > 0) {
    	  	  updatedGameField = gameField respawn deadTokens.head.id
    	  	  
    	  	     	  	  
    	  	  // only continue if it was respawned
    	  	  val tokenRespawned =
    	  	    !updatedGameField.findOnePlayerTokenById(deadTokens.head.id).dead
    	  	  if(tokenRespawned) {
    	  	  	updatedPlayers = players.updatedExistingPlayer(
      	  		    										 	currentPlayer.updatedDecreasedLifes)
      	  		logger merge updatedGameField
      	  		
      	  		var updatedCurrentPlayer = updatedPlayers.currentPlayer
      	  		    										 	
      	  		updatedGameState = this.gameState    										 	
      	  		updatedGameMessage =  GameMessages.respawnedToken(updatedGameField
      	  		    	.findOnePlayerTokenById(deadTokens.head.id).coords)    	  		    										 	
  
      	  		// check if this will be the only living token and select it   										 	
      	  		if(deadTokens.size == updatedCurrentPlayer.totalTokens) {
      	  		  while(deadTokens.head.id != updatedCurrentPlayer.currentTokenId) {
      	  		   updatedCurrentPlayer = updatedCurrentPlayer.updatedCycledTokens 
      	  		   updatedPlayers = 
      	  		     updatedPlayers.updatedExistingPlayer(updatedCurrentPlayer)			
      	  		  }
      	  		}
      	  	  
      	  	  // check if the current token is frozen
      	  	  val updatedCurrentToken = updatedCurrentPlayer.currentToken(updatedGameField)  			 	
      	  		val currentTokenFrozenTime = updatedCurrentToken.frozenTime
  					  if(currentTokenFrozenTime > 0) {		
      	  			updatedGameState = GameState.NeedTokenSwitch
      	  			updatedGameMessage += " - " + 
      	  					GameMessages.frozenToken(currentTokenFrozenTime)
      	  		}    	  	    
    	  	  } else { // if(tokenRespawned)  
    	  	  	updatedGameMessage = GameMessages.noRoomForToken
    	  	  	updatedGameState = this.gameState
    	  	  	updatedPlayers = this.players
    	  	  }
    	  	  
      	  	return updated(newGameField = updatedGameField,
      	  	    					 newPlayers = updatedPlayers,
      	  	    					 newGameState = updatedGameState,
      	  	    					 newGameMessage = updatedGameMessage)
  	  	  } // if(currentPlayer.lifes > 0)
  	  	  return updated(newGameMessage = "You don't have lifes left to respawn.")
  	  	} // if(deadTokens.nonEmpty)
  	  	else {
  	  	  return updated(newGameMessage = "There are no dead tokens to respawn")
  	  	}
  	  }

  	  case (NextToken, GameState.InGame |
  	      						 GameState.NeedTokenSwitch) => {
  	  	  	      						   
        // check if there are other alive tokens 	      						   
  	    val otherAliveTokens = gameField.findPlayerTokensById(
  	        currentPlayer.playerTokenIds.filter(id => 
  	          	  											 id != currentPlayer.currentTokenId))
  	          	  			 .filter(token => !token.dead)
  	    if(otherAliveTokens.isEmpty) {
  	      updatedGameMessage = GameMessages.noOthersAlive
  	      logger += (updatedGameMessage)
  	      if(currentToken.dead)
  	      	updatedGameState = GameState.NeedTokenSwitch
	      	else
	      	  updatedGameState = this.gameState
	      		
  	      return updated(newGameState = updatedGameState,
  	          					 newGameMessage = updatedGameMessage)
  	      
  	    } else {
    	    updatedPlayer = currentPlayer.updatedCycledTokens
    	    while(updatedPlayer.currentToken(gameField).dead) 
    	      updatedPlayer = updatedPlayer.updatedCycledTokens
    	    
    	    updatedPlayers = players.updatedExistingPlayer(updatedPlayer)  
    	      
  	      // check if token is frozen
    	    val updatedCurrentToken = updatedPlayer.currentToken(gameField)
          if(updatedCurrentToken.frozenTime > 0) {
          	logger += "Current token is frozen for " + 
          			updatedCurrentToken.frozenTime + " rounds."
          	
          	updatedGameMessage = GameMessages.frozenToken(updatedCurrentToken.frozenTime)    
            updatedGameState = GameState.NeedTokenSwitch
          } else {
            updatedGameState = GameState.InGame
            updatedGameMessage = GameMessages.noMsg
          }

  	      return updated(newGameState = updatedGameState,    	    
    	    							 newPlayers = updatedPlayers,
    	    							 newGameMessage = updatedGameMessage)
  	    }
  		}
  	  
  	  case (cmd, GameState.NeedPlayerSwitch |
  	      	   GameState.NeedTokenSwitch |
  	      		 GameState.GameOver | 
  						 GameState.Win) => {
  			val msg = GameMessages.invalidCmd(cmd, this.gameState)			   
  	    logger += msg 
  	    updated(newGameMessage = msg)
  	  }
	  }
	  
	} // def executeCommand(gameCommand : GameCommand)
	
	private def updated(newGameField : GameField = this.gameField,
	    								newPlayers : Players = this.players,
	    								newGameState : GameState = this.gameState,
	    								newGameMessage : String = GameMessages.noMsg) : Game = {
	  logger += ("Execution time:  " + 
    	  					 (Calendar.getInstance().getTimeInMillis() - startTime) +
    	  					" ms")
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
    		gameField.findHumanTokens.filter(hT => !hT.dead).size == 0
  }
}