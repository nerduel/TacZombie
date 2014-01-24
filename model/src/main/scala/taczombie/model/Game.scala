package taczombie.model

import GameState.GameState
import util.CoordinateHelper.intIntTuple2Wrapper

object defaults {
  val humanMoves = 20
  val lifes = 3
  val zombieMoves = 1
  
  val humanName = "Pacman"
  val zombieName = "Zombie"
}

class Game(val id : Int,
    			 val gameField : GameField,
           val players : Players, // first player is current player!
           val gameState : GameState = GameState.InGame,
           val lastUpdatedGameFieldCells : List[GameFieldCell] = List[GameFieldCell]()) {
  
	def executeCommand(gameCommand : GameCommand) 
			: Game = {
	  var player = players.currentPlayer

	  var updatedGameState = gameState
	  var (updatedGameField, updatedGameFieldCells, updatedPlayers) = 
	    gameCommand match {
  	    case MoveUp => move(player.currentToken.coords.aboveOf, player)
  	    case MoveDown => move(player.currentToken.coords.belowOf, player)
  	    case MoveLeft => move(player.currentToken.coords.leftOf, player)
  	    case MoveRight => move(player.currentToken.coords.rightOf, player)

  	    //TODO nextGame
  	    //TODO nextToken
	  }
	  
	  player = updatedPlayers.currentPlayer

    // check if the player has collected all the coins
    if(player.coinsCollected == gameField.coinsPlaced)
      updatedGameState = GameState.Win
      
    println("collected coins for player " + player.name + " : " + player.coinsCollected)
 
    // TODO check for lifesRemaining for token
      
    // TODO check for movesRemaining for player
	  
    if (player.movesRemaining == 0) {
      println("switching players")
      updatedPlayers = updatedPlayers.updatedRotatedPlayers
    } else {
      println("remaining moves " + player.movesRemaining)
//      println("remaining moves: " + player.movesRemaining + " -> switching tokens")
//      updatedPlayers = updatedPlayers.
//      								 		updatedExistingPlayer(player.updatedCycledTokens)
    }
    
	  // TODO check if all HumanTokens are dead
//    println(updatedPlayers)
//    players = players
//    println(updatedPlayerMap)
//    
    
    // TODO cycle player  
      
    new Game(id, updatedGameField, updatedPlayers, 
        updatedGameState,updatedGameFieldCells)
	}
	
	//private def cycleTokenMap(tokenMap : TreeMap[Int,PlayerToken]) : Player

	private def move(requestedDestinationCoords : (Int,Int), player : Player) 
			: (GameField, List[GameFieldCell], Players) = {

    if(gameState != GameState.InGame) null // TODO: invalid gamestate
    
    val destinationCoords = {
      if((gameField.gameFieldCells
          	.apply(requestedDestinationCoords).containsWall) ||
      		player.currentToken.frozenTime > 0) {
        player.currentToken.coords
      } else { 
        requestedDestinationCoords
      }
    }
    
    // process the actual move. dead tokens will have .dead = true
    gameField.move(destinationCoords, players)
  }
}