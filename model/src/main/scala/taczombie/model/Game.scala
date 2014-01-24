package taczombie.model

import scala.collection.immutable.TreeMap
import scala.collection.mutable.ListBuffer

import GameState.GameState
import util.CoordinateHelper.intIntTuple2Wrapper

class Game(val id : Int,
    			 val gameField : GameField,
           val players : Players, // first player is current player!
           val gameState : GameState = GameState.InGame) {
  
	def executeCommand(gameCommand : GameCommand) : Game = {
	  val player = players.currentPlayer


	  var updatedGameState = gameState
	  var (updatedGameField, updatedGameFieldCells, updatedPlayers) = 
	    gameCommand match {
  	    case MoveUp() => move(player.currentToken.coords.aboveOf, player)
  	    case MoveDown() => move(player.currentToken.coords.belowOf, player)
  	    case MoveLeft() => move(player.currentToken.coords.leftOf, player)
  	    case MoveRight() => move(player.currentToken.coords.rightOf, player)
	  }
	      
    // check if the player has collected all the coins
    if(player.coinsCollected == gameField.coinsPlaced)
      updatedGameState = GameState.Win
      
    println("collectec coins for player " + player.name + player.coinsCollected)
 
	  // Respawn dead players randomly on map  
//    updatedPlayers.foreach({ updatedPlayer =>
//      if (updatedPlayer._2.currentToken.dead) {
//        // TODO: check for Dead tokens and re-spawn them
//      }
//    })	  
	      
    
    // TODO check for lifesRemaining for token
      
    // TODO check for movesRemaining for player
    if (player.movesRemaining == 0) {
      println("switching players")
      updatedPlayers = updatedPlayers.updatedRotatedPlayers
    } else {
      println("switching tokens")
    }
    
    // TODO check if all HumanTokens are dead
//    println(updatedPlayers)
//    players = players
//    println(updatedPlayerMap)
//    
//    // TODO cycle tokens
//    println(updatedPlayerMap)
//    updatedPlayerMap = cycleTokenMaps(updatedPlayerMap)
//    println(updatedPlayerMap)
    
    // TODO cycle player  
      
    new Game(id, updatedGameField, updatedPlayers, updatedGameState)
	}
	
	//private def cycleTokenMap(tokenMap : TreeMap[Int,PlayerToken]) : Player

	private def move(requestedDestinationCoords : (Int,Int), player : Player) 
			: (GameField, List[GameFieldCell], Players) = {

    if(gameState != GameState.InGame) null // TODO: invalid gamestate
    
    val destinationCoords = {
      if(!walkOnAble(requestedDestinationCoords) ||
      		player.currentToken.frozenTime > 0) 
      {
        player.currentToken.coords
      } else { 
        requestedDestinationCoords
      }
    }
    
    // process the actual move. dead tokens will have .dead = true
    gameField.move(destinationCoords, players)
  }
	  	
	private def calculateAllowedMoves(player : Player) = {
      var toVisit = scala.collection.mutable.Stack.empty[(Int,Int)]
      val position = player.currentToken.coords
      var pathList = ListBuffer(ListBuffer.empty[(Int, Int)])
      var element = position
      var col = 0
      var alreadyVisited = ListBuffer(element)

      do {
          if (pathList.apply(col).size < player.movesRemaining) {
              val neighbours = getNeighbours(element, alreadyVisited.toList)

              if (neighbours.size == 0) {
                  if (pathList.last == pathList(col))
                      pathList = pathList.+=(ListBuffer.empty[(Int, Int)])
                  col += 1
                  alreadyVisited = ListBuffer()
              }
              else {
                  for (n <- neighbours) toVisit.push(n)

                  // copy current List if more than one neighbour was found
                  if (pathList.apply(col).size != 0)
                      for (t <- 1 until neighbours.size)
                          pathList.insert(col, pathList.apply(col))
              }
          }
          else {
              if (pathList.last == pathList(col))
                  pathList = pathList.+=(ListBuffer.empty[(Int, Int)])

              alreadyVisited = ListBuffer()
              col += 1
          }

          element = toVisit.pop
          pathList(col) = pathList(col) ++ ListBuffer(element)
          alreadyVisited.+=(element)

      } while (!toVisit.isEmpty)

      pathList.toList.flatten.distinct
  }

    // Input must be (y,x). map needs coords in (y,x)
    // result is a List of valid coordinates for the array (y,x)
    private def getNeighbours(position : (Int, Int), alreadyVisited : List[(Int, Int)]) = {
        // von Neumann neighborhood, East, South, West, North
        val neighbours = 
          ListBuffer(position.rightOf) ++
          ListBuffer(position.belowOf) ++
          ListBuffer(position.leftOf) ++
          ListBuffer(position.aboveOf)

        for (
            n <- neighbours.toList;
            if (walkOnAble(position));
            if (!alreadyVisited.contains(n))
        ) yield n

    }
    
    private def walkOnAble(pos: (Int,Int)) = {
      !gameField.gameFieldCells(pos).containsWall
    }
}