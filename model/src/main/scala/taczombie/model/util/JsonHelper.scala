package taczombie.model.util

import scala.language.implicitConversions

import spray.json.DefaultJsonProtocol
import spray.json.JsValue
import spray.json.pimpAny
import taczombie.model.Game
import taczombie.model.util.CoordinateHelper._
import taczombie.model.GameFieldCell
import taczombie.model.Human
import taczombie.model.Zombie

object JsonHelper {

  case class Data(cmd: String, gameData: JsValue, cells: JsValue)

  case class GameData(gameState: String, currentPlayer: Char,
    lifes: Int, movesLeft: Int, coins: Int,
    score: Int, powerUp: Int, levelWidth: Int,
    levelHeight: Int)

  case class Cell(x: Int, y: Int, token: Char, isHiglighted: Boolean)

  object GameDataJsonProtocol extends DefaultJsonProtocol {
    implicit val GameDataFormat = jsonFormat9(GameData)
    implicit val CellFormat = jsonFormat4(Cell)
    implicit val DataFormat = jsonFormat3(Data)
  }

  class Game2JsonHelper(g: Game) {

    // TODO: changedCells should be reacheable through the game class,
    def toJson(cmd: String, changedCells: Map[(Int, Int), GameFieldCell]): String = {
      val gameState = g.gameState.toString
      val currentPlayer = g.players.currentPlayer
      var currentPlayerTokenAsChar = ' '
      var lifes = 0
      val movesRemaining = currentPlayer.movesRemaining
      val coins = currentPlayer.coinsCollected
      val score = currentPlayer.score
      val width = g.gameField.levelWidth
      var powerUp = 0
      val height = g.gameField.levelHeight

      g.players.currentPlayer match {
        case h: Human =>
          currentPlayerTokenAsChar = 'H'
          powerUp = h.currentToken.powerupTime
          lifes = h.lifes
        case z: Zombie =>
          currentPlayerTokenAsChar = 'Z'
      }

      // Collect and simplify changed game cells
      var cells = List[Cell]()
      
      var allowedMoves = currentPlayer.currentToken.coords.calculateAllowedMoves(movesRemaining)
      
      for (gameFieldCell <- changedCells) {
        val char: Char = {
          if (gameFieldCell._2 == null) 'N'
          else if (gameFieldCell._2.containsWall) 'W'
          else if (gameFieldCell._2.containsZombieToken) 'Z'
          else if (gameFieldCell._2.containsHumanToken) 'H'
          else if (gameFieldCell._2.containsCoin) 'C'
          else if (gameFieldCell._2.containsPowerup) 'P'
          else 'N'
        }

        val canBeVisited = {
          if(allowedMoves.contains(gameFieldCell)) {
            allowedMoves = allowedMoves.filter(_ != gameFieldCell)
            true
          } else false
        }
          
        //        val canBeVisited = g.calculateAllowedMoves(currentPlayer).filter(cell =>
        //          cell._1 == gameFieldCell._1 &&
        //            cell._2 == gameFieldCell._2).size == 1

        cells ::: Cell(gameFieldCell._1._1, gameFieldCell._1._2, char, canBeVisited) :: Nil
      }
      
      

      import GameDataJsonProtocol._

      val gameData = GameData(gameState, currentPlayerTokenAsChar, lifes, movesRemaining, coins, score, powerUp, width, height)
      val gameDataJson = gameData.toJson
      val data = Data(cmd, gameDataJson, cells.toJson)

      data.toJson.toString
    }
  }

  implicit def game2JsonWrapper(game: Game) =
    new Game2JsonHelper(game)
}

		