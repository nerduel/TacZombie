package taczombie.test.model

import org.specs2.mutable._
import taczombie.test.model.TestObjects._
import taczombie.model.GameFactory
import taczombie.model.util.CoordinateHelper._
import scala.util.matching.Regex
import taczombie.model.GameState
import taczombie.model.Human
import taczombie.model.Zombie
import taczombie.model.GameFieldCell
import taczombie.model.Game
import taczombie.model.NextPlayer
import taczombie.model.MoveDown
import taczombie.model.MoveRight
import taczombie.model.MoveUp

class JsonHelperSpec extends Specification {

  private val testFile = getClass().getResource("/TestLevel_correct")

  private val cmdJAll = """"cmd":"all""""
  private val cmdJUpdated = """"cmd":"updated""""
  private val cmdJError = """"cmd":"error""""

  private val seperator = """:"""
  private val comma = ""","""
  private val quote = """""""
  private val anyString = """.*"""

  private val openCurlyBracket = """\{"""
  private val closingCurlyBracket = """\}"""
  private val openSquareBracket = """\["""
  private val closingSquareBracket = """\]"""

  private var prevAllowedMoves : List[(Int, Int)] = List.empty

  private def rJsonPair(a : String, b : String) = {
    quote + a + quote + seperator + quote + b + quote
  }

  private def rJsonPair(a : String, b : Int) = {
    quote + a + quote + seperator + b
  }

  private def rJsonProperty(name : String, prop : String) = {
    quote + name + quote + seperator + openCurlyBracket + prop + closingCurlyBracket
  }

  private def rJsonList(name : String, cellList : List[String]) = {
    var listString = """"""

    for (str <- cellList) {
      listString += str + comma
    }

    quote + name + quote + seperator + openSquareBracket + listString.dropRight(1) + closingSquareBracket
  }

  private def rJsonHumanToken(x : Int, y : Int, powerUp : Boolean) = {
    openCurlyBracket +
      quote + "x" + quote + seperator + x + comma +
      quote + "y" + quote + seperator + y + comma +
      quote + "powerUp" + quote + seperator + powerUp +
      closingCurlyBracket
  }

  private def getChar(gameFieldCell : GameFieldCell) : Char = {
    val char : Char = {
      if (gameFieldCell == null) 'N'
      else if (gameFieldCell.containsWall) 'W'
      else if (gameFieldCell.containsLivingZombieToken) 'Z'
      else if (gameFieldCell.containsLivingHumanToken) 'H'
      else if (gameFieldCell.containsCoin) 'C'
      else if (gameFieldCell.containsPowerup) 'P'
      else 'N'
    }
    (char)
  }

  private def getExpectedJsonData(testGame : Game, cmd : String) = {
    val currentPlayer = testGame.players.currentPlayer
    val gameField = testGame.gameField
    val movesRemaining = currentPlayer.movesRemaining
    var currentPlayerTokenAsChar = ' '
    var powerUp = 0
    var lifes = 0

    currentPlayer match {
      case h : Human =>
        currentPlayerTokenAsChar = 'H'
        powerUp = h.currentToken(testGame.gameField).powerupTime
        lifes = h.lifes
      case z : Zombie =>
        currentPlayerTokenAsChar = 'Z'
    }

    (
      (cmd match {
        case "All" => cmdJAll
        case "Updated" => cmdJUpdated
      }).r,

      rJsonPair("gameMessage", testGame.lastGameMessage.replaceAll("\\*", "\\\\*")).r,

      rJsonProperty("gameData", {
        rJsonPair("gameState", testGame.gameState.toString) + comma +
          rJsonPair("currentPlayer", currentPlayerTokenAsChar.toString) + comma +
          rJsonPair("lifes", lifes) + comma +
          rJsonPair("movesRemaining", currentPlayer.movesRemaining) + comma +
          rJsonPair("coins", currentPlayer.coins(gameField)) + comma +
          rJsonPair("score", currentPlayer.score(gameField)) + comma +
          rJsonPair("powerUp", currentPlayer.currentToken(gameField).powerupTime) + comma +
          rJsonPair("levelWidth", testGame.gameField.levelWidth) + comma +
          rJsonPair("levelHeight", testGame.gameField.levelHeight) + comma +
          rJsonPair("frozenTime", currentPlayer.currentToken(gameField).frozenTime) + comma +
          rJsonPair("deadTokens", currentPlayer.deadTokenCount(gameField)) + comma +
          rJsonPair("totalTokens", currentPlayer.totalTokens)
      }).r,

      rJsonList("cells", List(openCurlyBracket + anyString + closingCurlyBracket)).r,

      rJsonList("log", List(anyString)).r,

      rJsonList("humanTokens", {
        for (hToken <- gameField.findHumanTokens)
          yield rJsonHumanToken(hToken.coords._1, hToken.coords._2, hToken.powerupTime > 0)
      }).r
    )
  }

  import taczombie.model.util.JsonHelper._
  "Method toJson" should {
    "return a proper Json String with parameter 'All'" in {

      "when a new Game was created" in {
        var testGame = GameFactory.newGame(random = false, file = testFile.getFile(), humans = 1)

        val expectedJsonString = getExpectedJsonData(testGame, "All")
        val jsonString = testGame.toJson(All)
        jsonString must find(expectedJsonString._1)
        jsonString must find(expectedJsonString._2)
        jsonString must find(expectedJsonString._3)
        jsonString must find(expectedJsonString._4)
        jsonString must find(expectedJsonString._5)
      }

      "when game was already played" in {
        var testGame = GameFactory.newGame(random = false, file = testFile.getFile(), humans = 1)
        testGame = testGame.executeCommand(MoveRight)

        val expectedJsonString = getExpectedJsonData(testGame, "All")
        val jsonString = testGame.toJson(All)
        jsonString must find(expectedJsonString._1)
        jsonString must find(expectedJsonString._2)
        jsonString must find(expectedJsonString._3)
        jsonString must find(expectedJsonString._4)
        jsonString must find(expectedJsonString._5)
      }
    }

    "return a proper Json String with parameter 'Updated'" in {

      "when changing the player" in {
        var testGame = GameFactory.newGame(random = false, file = testFile.getFile(), humans = 1)
        testGame = testGame.executeCommand(NextPlayer)
        val expectedJsonString = getExpectedJsonData(testGame, "Updated")
        val jsonString = testGame.toJson(Updated)
        jsonString must find(expectedJsonString._1)
        jsonString must find(expectedJsonString._2)
        jsonString must find(expectedJsonString._3)
        jsonString must find(expectedJsonString._4)
        jsonString must find(expectedJsonString._5)
      }

      "when moving humanToken to another walkable field" in {
        var testGame = GameFactory.newGame(random = false, file = testFile.getFile(), humans = 1)
        testGame = testGame.executeCommand(MoveRight)

        val expectedJsonString = getExpectedJsonData(testGame, "Updated")
        val jsonString = testGame.toJson(Updated)
        jsonString must find(expectedJsonString._1)
        jsonString must find(expectedJsonString._2)
        jsonString must find(expectedJsonString._3)
        jsonString must find(expectedJsonString._4)
        jsonString must find(expectedJsonString._5)
      }

      "when moving humanToken against a Wall" in {
        var testGame = GameFactory.newGame(random = false, file = testFile.getFile(), humans = 1)
        testGame = testGame.executeCommand(MoveUp)

        val expectedJsonString = getExpectedJsonData(testGame, "Updated")
        val jsonString = testGame.toJson(Updated)
        jsonString must find(expectedJsonString._1)
        jsonString must find(expectedJsonString._2)
        jsonString must find(expectedJsonString._3)
        jsonString must find(expectedJsonString._4)
        jsonString must find(expectedJsonString._5)
      }
    }
  }
  
  "Method toErrorJson" should {
    "generate a proper error Json on a random String with its content as message" in {
      
      val testErrorMsg = "This is an erro Message"
      val expectedJsonError = openCurlyBracket + 
      							cmdJError + comma +
      							rJsonPair("message", testErrorMsg) +
      						  closingCurlyBracket
      
      val generatedErrorJson = testErrorMsg.toErrorJson
      generatedErrorJson must beMatching(expectedJsonError)
    }
  }
}