package taczombie.client.test.model

import scala.collection.SortedMap

import org.specs2.mutable.Specification

import spray.json.pimpAny
import taczombie.client.model.ViewModel
import taczombie.model.GameState
import taczombie.model.util.JsonHelper.Cell
import taczombie.model.util.JsonHelper.Data
import taczombie.model.util.JsonHelper.Error
import taczombie.model.util.JsonHelper.ErrorJsonProtocol.ErrorFormat
import taczombie.model.util.JsonHelper.GameData
import taczombie.model.util.JsonHelper.GameDataJsonProtocol.DataFormat
import taczombie.model.util.JsonHelper.GameDataJsonProtocol.GameDataFormat
import taczombie.model.util.JsonHelper.GameDataJsonProtocol.listFormat
import taczombie.model.util.JsonHelper.HumanTokens

class ModelSpec extends Specification {

  "toObject" should {

    "produce proper gameMessage when cmd is 'error'" in {
      val model = new ViewModel
      val errorMsg = "test"
      import taczombie.model.util.JsonHelper._
      import ErrorJsonProtocol._
      var error = Error(message = errorMsg).toJson
      model.toObject(error)

      model.gameMessage must be_==(errorMsg)
    }

    "produce proper model when cmd is 'all'" in {
      val model = new ViewModel
      val cmd = "all"
      val gameState = GameState.InGame.toString
      val currentPlayerToken = "Human"
      val lifes = 1
      val movesRemaining = 2
      val coins = 3
      val score = 4
      val powerUp = 5
      val levelWidth = 6
      val levelHeight = 7
      val frozenTime = 8
      val deadTokens = 9
      val totalTokens = 10
      val lastGameMessage = "lastGameMessage"
      val cell = Cell(1, 1, 'C', true)
      val cells = List[Cell](cell)
      var cellsModell = SortedMap[(Int, Int), (Char, Boolean)]()
      cellsModell += (cell.x, cell.y) -> (cell.token, cell.isHiglighted)
      val humanToken = HumanTokens(1, 1, true)
      val humanTokens = List[HumanTokens](humanToken)
      var humanTokensModell = SortedMap[(Int, Int), Boolean]()
      humanTokensModell += (humanToken.x, humanToken.y) -> (humanToken.powerUp)
      val logList = List[String]("logList")

      import taczombie.model.util.JsonHelper._
      import GameDataJsonProtocol._

      val gameData = GameData(gameState, 'H', lifes,
        movesRemaining, coins, score, powerUp, levelWidth, levelHeight, frozenTime, deadTokens, totalTokens)
      val gameDataJson = gameData.toJson
      val data = Data(cmd, lastGameMessage, gameDataJson, cells.toJson, logList, humanTokens.toJson)
      model.toObject(data.toJson)

      model.cmd must be_==(cmd)
      model.gameState must be_==(gameState)
      model.currentPlayerToken must be_==(currentPlayerToken)
      model.lifes must be_==(lifes)
      model.movesRemaining must be_==(movesRemaining)
      model.coins must be_==(coins)
      model.score must be_==(score)
      model.powerUp must be_==(powerUp)
      model.levelWidth must be_==(levelWidth)
      model.levelHeight must be_==(levelHeight)
      model.frozenTime must be_==(frozenTime)
      model.deadTokens must be_==(deadTokens)
      model.totalTokens must be_==(totalTokens)
      model.gameMessage must be_==(lastGameMessage)
      model.cells must be_==(cellsModell)
      model.log must be_==(logList)
      model.humanTokens must be_==(humanTokensModell)
    }

    "produce proper model when cmd is 'updated'" in {
      val model = new ViewModel
      val cmd = "updated"
      val gameState = GameState.InGame.toString
      val currentPlayerToken = "Human"
      val lifes = 1
      val movesRemaining = 2
      val coins = 3
      val score = 4
      val powerUp = 5
      val levelWidth = 6
      val levelHeight = 7
      val frozenTime = 8
      val deadTokens = 9
      val totalTokens = 10
      val lastGameMessage = "lastGameMessage"
      val cell = Cell(1, 1, 'C', true)
      val cells = List[Cell](cell)
      var cellsModell = SortedMap[(Int, Int), (Char, Boolean)]()
      cellsModell += (cell.x, cell.y) -> (cell.token, cell.isHiglighted)
      val humanToken = HumanTokens(1, 1, true)
      val humanTokens = List[HumanTokens](humanToken)
      var humanTokensModell = SortedMap[(Int, Int), Boolean]()
      humanTokensModell += (humanToken.x, humanToken.y) -> (humanToken.powerUp)
      val logList = List[String]("logList")

      import taczombie.model.util.JsonHelper._
      import GameDataJsonProtocol._

      val gameData = GameData(gameState, 'H', lifes,
        movesRemaining, coins, score, powerUp, levelWidth, levelHeight, frozenTime, deadTokens, totalTokens)
      val gameDataJson = gameData.toJson
      val data = Data(cmd, lastGameMessage, gameDataJson, cells.toJson, logList, humanTokens.toJson)
      model.toObject(data.toJson)

      model.cmd must be_==(cmd)
      model.gameState must be_==(gameState)
      model.currentPlayerToken must be_==(currentPlayerToken)
      model.lifes must be_==(lifes)
      model.movesRemaining must be_==(movesRemaining)
      model.coins must be_==(coins)
      model.score must be_==(score)
      model.powerUp must be_==(powerUp)
      model.levelWidth must be_==(levelWidth)
      model.levelHeight must be_==(levelHeight)
      model.frozenTime must be_==(frozenTime)
      model.deadTokens must be_==(deadTokens)
      model.totalTokens must be_==(totalTokens)
      model.gameMessage must be_==(lastGameMessage)
      model.cells must be_==(cellsModell)
      model.log must be_==(logList)
      model.humanTokens must be_==(humanTokensModell)
    }
  }
}