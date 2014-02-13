package model

import taczombie.model.util.JsonHelper._
import util.Observable
import spray.json.DefaultJsonProtocol
import spray.json._
import DefaultJsonProtocol._
import taczombie.model.GameState
import scala.swing.Publisher
import scala.swing.event.Event

case object gameUpdated extends Event

class ViewModel extends Publisher {
  import taczombie.model.util.JsonHelper.GameDataJsonProtocol._
  var cmd = ""
  var gameState = " "
  var currentPlayerTokenAsChar = ' '
  var lifes = 0
  var movesRemaining = 0
  var coins = 0
  var score = 0
  var powerUp = 0
  var levelWidth = 10
  var levelHeight = 10
  var cells = List[Cell]()

  def toObject(json: JsValue) {
    val data: Data = json.convertTo[Data]
    val gameData = data.gameData.convertTo[GameData]
    cells = data.cells.convertTo[List[Cell]]
    cmd = data.cmd
    gameState = gameData.gameState.toString()
    currentPlayerTokenAsChar = gameData.currentPlayer
    lifes = gameData.lifes
    movesRemaining = gameData.movesLeft
    score = gameData.score
    powerUp = gameData.powerUp
    levelHeight = gameData.levelWidth
    levelHeight = gameData.levelHeight
    
    publish(gameUpdated)
  }
  
  def updated {
    publish(gameUpdated)
  }
}