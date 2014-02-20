package controllers

import play.api.libs.json.JsValue
import taczombie.model.Game
import taczombie.model.GameFactory
import taczombie.model.GameState
import taczombie.model.MoveDown
import taczombie.model.MoveLeft
import taczombie.model.MoveRight
import taczombie.model.MoveUp
import taczombie.model.NextPlayer
import taczombie.model.NextToken
import taczombie.model.util.LevelCreator
import taczombie.model.util.JsonHelper
import scala.language.implicitConversions
import taczombie.model.RespawnToken

object GameController {

  class GameHelper(g: Game) {
    def isSet = g != null
  }

  var lastGeneratedGame: Game = null
  var myGame: Game = null

  implicit def gameWrapper(g: Game) =
    new GameHelper(g)

  import taczombie.model.util.JsonHelper._

  def evaluateCommand(cmd: String): String = {
    cmd match {
      case "moveLeft" =>
        if (myGame.isSet) {
          myGame = myGame.executeCommand(MoveLeft)
          myGame.toJson(Updated)
        } else {
         "Can't handle command 'moveLeft'! No Game was loaded before!".toErrorJson
        }

      case "moveRight" =>
        if (myGame.isSet) {
          myGame = myGame.executeCommand(MoveRight)
          myGame.toJson(Updated)
        } else {
          "Can't handle command 'moveRight'! No Game was loaded befor!".toErrorJson
        }

      case "moveUp" =>
        if (myGame.isSet) {
          myGame = myGame.executeCommand(MoveUp)
          myGame.toJson(Updated)
        } else {
          "Can't handle command 'moveUp'! No Game was loaded before!".toErrorJson
        }

      case "moveDown" =>
        if (myGame.isSet) {
          myGame = myGame.executeCommand(MoveDown)
          myGame.toJson(Updated)
        } else {
          "Can't handle command 'moveDown'! No Game was loaded before!".toErrorJson
        }

      case "nextPlayer" =>
        if (myGame.isSet) {
          myGame = myGame.executeCommand(NextPlayer)
          myGame.toJson(Updated)
        } else {
          "Can't handle command 'nextPlayer'! No Game was loaded before!".toErrorJson
        }

      case "switchToken" =>
        if (myGame.isSet) {
          myGame = myGame.executeCommand(NextToken)
          myGame.toJson(Updated)
        } else {
          ":\"Can't handle command 'switchToken'! No Game was loaded before!".toErrorJson
        }

      case "restartGame" =>
        if (myGame.isSet) {
          myGame = lastGeneratedGame
          myGame.toJson(All)
        } else {
          "Can't handle command 'restartGame'! No Game was loaded before!".toErrorJson
        }

      case "respawnToken" =>
        if (myGame.isSet) {
          myGame = myGame.executeCommand(RespawnToken)
          myGame.toJson(Updated)
        } else {
          "Can't handle command 'respawnToken'! No Game was loaded before!".toErrorJson
        }

      case "newGame" =>
        lastGeneratedGame = GameFactory.newGame(random = true)
        
        myGame = lastGeneratedGame
        myGame.toJson(All)

      case _ =>
        "Unknown command was received!".toErrorJson

    }
  }

  def getCurrentGame: String = {
    myGame.toJson(All)
  }

}