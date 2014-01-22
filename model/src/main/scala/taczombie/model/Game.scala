package taczombie.model

import scala.collection.immutable.TreeMap
import scala.collection.mutable.ListBuffer

import GameState.GameState
import taczombie.model.util.CoordinateHelper.intIntTuple2Wrapper
import taczombie.model.util.GameHelper.activePlayer

class Game(val id : Int,
    			 val gameField : GameField,
           val playerList : TreeMap[Int,Player], // first player is current player!
           val gameState : GameState = GameState.InGame) {
  
	def executeCommand(gameCommand : GameCommand) : Game = {
	  val player = activePlayer(playerList)
	  gameCommand match {
	    case MoveUp() => move(player.currentToken.coords.aboveOf, player)
	    case MoveDown() => move(player.currentToken.coords.belowOf, player)
	    case MoveLeft() => move(player.currentToken.coords.leftOf, player)
	    case MoveRight() => move(player.currentToken.coords.rightOf, player)
	    case _ => this // TODO: exception unknown command
	  }
	}  

	private def move(destinationCoords : (Int,Int), player : Player) : Game = {
	  
	  // TODO: exception cannot move
    if(gameState != GameState.InGame) null
    if(!walkOnAble(destinationCoords)) null
    
    var updatedGameState : GameState = null

    // process the actual move. dead tokens will have .dead = true
    val (updatedGameField, updatedGameFieldCells, updatedPlayerList) = 
      gameField.move(destinationCoords, playerList)
      
    // TODO check if Human won
    gameField.coinsPlaced
      
    new Game(id, updatedGameField, updatedPlayerList, updatedGameState)
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

    private def updateHumanMove(pos : (Int, Int)) : Game = { null
//
//        var updatedPlayerList = playerList
//        var updatedPlayer = currentPlayer
//        var updatedGameState = gState
//        var updatedLifes = lifes
//        var updatedCoinAmount = coinAmount
//        var updatedLevelScore = lvlScore
//
//        gameField.getObject(pos) match {
//            case GameObject.Coin =>
//                val state = updatedPlayerList(currentPlayer.id).state
//                updatedPlayerList = updatedPlayerList.updated(currentPlayer.id,
//                    new Human(pos, state))
//                updatedLevelScore += 1
//                updatedCoinAmount -= 1
//                if (updatedCoinAmount == 0)
//                    updatedGameState = GameState.Win
//
//            case GameObject.Powerup =>
//                updatedPlayerList = updatedPlayerList.updated(currentPlayer.id,
//                    new Human(pos, zombieKiller()))
//
//            case GameObject.None =>
//                val state = updatedPlayerList(currentPlayer.id).state
//                updatedPlayerList = updatedPlayerList.updated(currentPlayer.id,
//                    new Human(pos, state))
//        }
//
//        var updatedGameField = gameField.clearCell(pos)
//
//        val playerCollisions = {
//            for (player <- playerList.tail) yield player
//        }.filter(_.coordinate == pos).zipWithIndex
//
//        if (playerCollisions.size > 0) {
//            playerList(currentPlayer.id).state match {
//                case z : zombieKiller =>
//                    updatedLevelScore += 2
//                    for (pC <- playerCollisions) {
//                        updatedPlayerList =
//                            updatedPlayerList.updated(pC._2, new Zombie(zombieBase, killed()))
//                    }
//
//                case _ =>
//                    if (lifes == 0)
//                        updatedGameState = GameState.GameOver
//                    else {
//                        updatedGameState = GameState.Lose
//                        updatedLevelScore -= 15
//                        updatedLifes -= 1
//                    }
//            }
//        }
//
//        if (movesRemaining == 0) {
//            updatedPlayerList = updatePlayerState
//            updatedPlayer = getNextPlayerId
//        }
//
//        this.copy(gameField = updatedGameField, playerList = updatedPlayerList,
//            gState = updatedGameState, moves = moves - 1,
//            currentPlayer = updatedPlayer, coinAmount = updatedCoinAmount,
//            lvlScore = updatedLevelScore, lifes = updatedLifes)
    }
    
    private def updateZombieMove(pos : (Int, Int)) : Game = { null
//        var updatedLevelScore = lvlScore
//        var updatedGameState = gState
//        var updatedLifes = lifes
//        var updatedPlayer = currentPlayer
//        var updatedPlayerList = playerList
//
//        if (playerList(PlayerId.Human.id).coordinate == pos) {
//            playerList(PlayerId.Human.id).state match {
//                case z : zombieKiller =>
//                    updatedLevelScore += 1
//                    updatedPlayerList = playerList.updated(currentPlayer.id,
//                        new Zombie(zombieBase, killed()))
//
//                case _ =>
//                    if (lifes == 0)
//                        updatedGameState = GameState.GameOver
//                    else {
//                        updatedGameState = GameState.Lose
//                        updatedLevelScore -= 15
//                        updatedLifes -= 1
//                    }
//            }
//        }
//
//        var updatedGameField = gameField.clearCell(pos)
//        
//        if (movesRemaining == 0) {
//            updatedPlayerList = updatePlayerState
//            updatedPlayer = getNextPlayerId
//        }
//
//        this.copy(gameField = updatedGameField, playerList = updatedPlayerList,
//            currentPlayer = updatedPlayer, gState = updatedGameState,
//            moves = moves - 1, lvlScore = updatedLevelScore, lifes = updatedLifes)
    }

    private def updatePlayerState: List[Player] = { null
//        playerList(currentPlayer.id).state match {
//            case k : killed if k.time > 0 =>
//			  		playerList.updated(currentPlayer.id,
//			  		        new Zombie(playerList(currentPlayer.id).coordinate,
//			  		                   killed(k.time - 1)))
//			  		
//		  	case k : killed if k.time == 0 =>
//		  		playerList.updated(currentPlayer.id,
//			  		        new Zombie(playerList(currentPlayer.id).coordinate,
//			  		                   normal()))
//			  		                   
//		  	case z : zombieKiller if z.time > 0 => 
//		  	    playerList.updated(currentPlayer.id,
//			  		        new Human(playerList(currentPlayer.id).coordinate,
//			  		                   zombieKiller(z.time - 1)))
//		  	    
//		  	case z : zombieKiller if z.time == 0 =>
//		  	    playerList.updated(currentPlayer.id,
//			  		        new Human(playerList(currentPlayer.id).coordinate,
//			  		                   normal()))
//			  		
//	  		case _ => playerList
//        }
    }
    
//    def copy(gameField : GameField = gameField,
//                     playerList : List[Player] = playerList,
//                     currentPlayer : PlayerId.PlayerId = currentPlayer,
//                     gState : GameState = gState,
//                     moves : Int = moves,
//                     coinAmount : Int = coinAmount,
//                     lvlScore : Int = lvlScore,
//                     lifes : Int = lifes) : Game = new Game(gameField,
//        humanBase, zombieBase, playerList, currentPlayer,
//        gState, moves, coinAmount, lvlScore, lifes, id)

    private def getNextPlayerId = { null
//        var x : Boolean = false
//        var potentialPlayer = currentPlayer
//
//        def nextPlayerFound(player : PlayerId.PlayerId) = {
//            playerList(player.id).state match {
//                case k : killed => false
//                case _ => true
//            }
//        }
//
//        while (x == false) {
//            potentialPlayer = potentialPlayer match {
//                case PlayerId.Human =>
//                    x = nextPlayerFound(PlayerId.Zombie1)
//                    PlayerId.Zombie1
//
//                case PlayerId.Zombie1 =>
//                    x = nextPlayerFound(PlayerId.Zombie2)
//                    PlayerId.Zombie2
//
//                case PlayerId.Zombie2 =>
//                    x = nextPlayerFound(PlayerId.Zombie3)
//                    PlayerId.Zombie3
//
//                case PlayerId.Zombie3 =>
//                    x = nextPlayerFound(PlayerId.Human)
//                    PlayerId.Human
//            }
//        }
//
//        potentialPlayer
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
      !gameField.gameFieldCells(pos).gameObjects.contains(new Wall(0, pos)) 
    }
}