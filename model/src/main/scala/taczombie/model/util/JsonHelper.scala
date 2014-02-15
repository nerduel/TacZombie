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
    lifes: Int, movesRemaining: Int, coins: Int,
    score: Int, powerUp: Int, levelWidth: Int,
    levelHeight: Int)

  case class Cell(x: Int, y: Int, token: Char, isHiglighted: Boolean)

  object GameDataJsonProtocol extends DefaultJsonProtocol {
    implicit val GameDataFormat = jsonFormat9(GameData)
    implicit val CellFormat = jsonFormat4(Cell)
    implicit val DataFormat = jsonFormat3(Data)
  }
  
  trait Type
  case object All extends Type
  case object Updated extends Type
  
  var prevAllowedMoves : List[(Int,Int)] = List.empty


  class Game2JsonHelper(g: Game) {   
    def toJson(command: Type): String = {
      
      var lastUpdatedGameFieldCells: List[GameFieldCell] = List.empty
      
      var cmd = ""
      command match {
        case All => 
          cmd = "all"
          lastUpdatedGameFieldCells = {for {cell <- g.gameField.gameFieldCells} yield cell._2 }.toList
        case Updated => 
          cmd = "updated"
          lastUpdatedGameFieldCells = g.gameField.lastUpdatedGameFieldCells
      }
      
      val gameState = g.gameState.toString
      val currentPlayer = g.players.currentPlayer
      var currentPlayerTokenAsChar = ' '
      var lifes = 0
      val movesRemaining = currentPlayer.movesRemaining
      val coins = currentPlayer.coins(g.gameField)
      val score = currentPlayer.score(g.gameField)
      val width = g.gameField.levelWidth
      var powerUp = 0
      val height = g.gameField.levelHeight

      g.players.currentPlayer match {
        case h: Human =>
          currentPlayerTokenAsChar = 'H'
          powerUp = h.currentToken(g.gameField).powerupTime
          lifes = h.lifes
        case z: Zombie =>
          currentPlayerTokenAsChar = 'Z'
      }
      
      // Collect and simplify changed game cells
      
      import taczombie.model.util.CoordinateHelper._
      val allowedMoves = currentPlayer.currentToken(g.gameField).coords.calculateAllowedMoves(movesRemaining,g)
      val gameFieldCellsFromAllowedMoves = g.gameField.gameFieldCells.filter(x => allowedMoves.contains(x._1._1, x._1._2))
      
      // Get current gameFieldCells which were highlighted, but now arent highlighted anymore.
      val gameFieldCellsFromPrevAllowedMoves = g.gameField.gameFieldCells.filter(x => prevAllowedMoves.filter(
              y => !allowedMoves.contains(y)).contains(x._1._1, x._1._2))
      // Updated lastAllowedMoves
      prevAllowedMoves = allowedMoves
              
      // Add highlighted Cells.
      var highlightedCells = {for {
          	gameFieldCell <- gameFieldCellsFromAllowedMoves
        	cell = Cell(gameFieldCell._1._1, gameFieldCell._1._2, getChar(gameFieldCell._2), true)
        } yield cell
      }.toList
      
      // Add updated Cells.
      var updatedCells = highlightedCells ::: { 
        for { 
        	gameFieldCell <- lastUpdatedGameFieldCells.filter(x => !gameFieldCellsFromAllowedMoves.contains(x.coords))
        	cell = Cell(gameFieldCell.coords._1,gameFieldCell.coords._2, getChar(gameFieldCell), false)
        } yield cell
      }
               
      // Add unhighlighted Cells. (because they arent counted as updated cells).
      var cells = updatedCells ::: { 
        for { 
        	gameFieldCell <- gameFieldCellsFromPrevAllowedMoves
        	cell = Cell(gameFieldCell._1._1,gameFieldCell._1._2, getChar(gameFieldCell._2), false)
        } yield cell
      }.toList
                  
      import GameDataJsonProtocol._

      val gameData = GameData(gameState, currentPlayerTokenAsChar, lifes, movesRemaining, coins, score, powerUp, width, height)
      val gameDataJson = gameData.toJson
      val data = Data(cmd.toString(), gameDataJson, cells.toJson)

      data.toJson.toString
    }
    
    private def getChar(gameFieldCell : GameFieldCell) : Char = {
      val char: Char = {
          if (gameFieldCell == null) 'N'
          else if (gameFieldCell.containsWall) 'W'
          else if (gameFieldCell.containsZombieToken) 'Z'
          else if (gameFieldCell.containsHumanToken) 'H'
          else if (gameFieldCell.containsCoin) 'C'
          else if (gameFieldCell.containsPowerup) 'P'
          else 'N'
        }
      (char)
    }
  }

  implicit def game2JsonWrapper(game: Game) =
    new Game2JsonHelper(game)
}

		