package taczombie.model.util

import scala.language.implicitConversions
import spray.json.DefaultJsonProtocol
import spray.json.JsValue
import spray.json.pimpAny
import taczombie.model.Game
import taczombie.model.GameFieldCell
import taczombie.model.Human
import taczombie.model.Zombie

object JsonHelper {

  case class Data(cmd: String, gameData: JsValue, cells: JsValue)

  case class GameData(gameState: String, currentPlayer: Char,
    lifes: Int, movesLeft: Int, coins: Int,
    score: Int, powerUp: Int, width: Int,
    height: Int)

  case class Cell(x: Int, y: Int, token: String, isHiglighted: Boolean)

  object GameDataJsonProtocol extends DefaultJsonProtocol {
    implicit val GameDataFormat = jsonFormat9(GameData)
    implicit val CellFormat = jsonFormat4(Cell)
    implicit val DataFormat = jsonFormat3(Data)
  }

  class Game2JsonHelper(g: Game) {
    
    // TODO: changedCells should be reacheable through the game class,
    def toJson(cmd: String, changedCells: Map[(Int, Int), GameFieldCell]): String = {
      val gameState = g.gameState.toString
      val currentPlayer = g.playerList.head._2
      var currentPlayerTokenAsChar = ' '
      var lifes = 0
      val movesLeft = currentPlayer.movesRemaining
      val coins = currentPlayer.coinsCollected
      val score = currentPlayer.score
      val width = g.gameField.levelWidth
      var powerUp = 0
      val height = g.gameField.levelHeight

      g.playerList.head._2 match {
        case h: Human =>
          currentPlayerTokenAsChar = 'H'
          powerUp = h.currentToken.powerupTime
          lifes = h.lifes
        case z: Zombie =>
          currentPlayerTokenAsChar = 'Z'
      }

      // Collect and simplify changed game cells
      var cells = List[Cell]()
      for (gameFieldCell <- changedCells) {
        val char = {
          if (gameFieldCell._2 == null) ""
          else if (gameFieldCell._2.containsWall) "███"
          else if (gameFieldCell._2.containsZombieToken) " Z " //"😈"
          else if (gameFieldCell._2.containsHumanToken) " H " //"😃"
          else if (gameFieldCell._2.containsCoin) " • "
          else if (gameFieldCell._2.containsPowerup) " ★ "
          else ""
        }

        val canBeMovedTo = g.calculateAllowedMoves(currentPlayer).filter(cell =>
          cell._1 == gameFieldCell._1 &&
            cell._2 == gameFieldCell._2).size == 1

        cells :+ Cell(gameFieldCell._1._1, gameFieldCell._1._2, char, canBeMovedTo)
      }

      import GameDataJsonProtocol._

      val gameData = GameData(gameState, currentPlayerTokenAsChar, lifes, movesLeft, coins, score, powerUp, width, height)
      val gameDataJson = gameData.toJson
      val data = Data(cmd, gameDataJson, cells.toJson)

      data.toJson.toString
    }
  }

  implicit def game2JsonWrapper(game: Game) =
    new Game2JsonHelper(game)
}

		