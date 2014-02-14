package model

import taczombie.model.util.JsonHelper._
import util.Observable
import spray.json.DefaultJsonProtocol
import spray.json._
import DefaultJsonProtocol._
import taczombie.model.GameState
import scala.swing.Publisher
import scala.swing.event.Event
import scala.collection.SortedMap

case object gameUpdated extends Event

class ViewModel extends Publisher {
  var receivedAll = false
  import taczombie.model.util.JsonHelper.GameDataJsonProtocol._
  var cmd = ""
  var gameState = " "
  var currentPlayerTokenAsChar = ' '
  var lifes = 0
  var movesRemaining = 0
  var coins = 0
  var score = 0
  var powerUp = 0
  var levelWidth = 0
  var levelHeight = 0
  var cells = SortedMap[(Int,Int),(Char,Boolean)]()

  def toObject(json: JsValue) {
    val data: Data = json.convertTo[Data]
    val gameData = data.gameData.convertTo[GameData]
    val updatedCells = data.cells.convertTo[List[Cell]]
    updatedCells.foreach{x =>
      cells += (x.x,x.y) -> (x.token,x.isHiglighted)
    }    
    cmd = data.cmd
    gameState = gameData.gameState.toString()
    currentPlayerTokenAsChar = gameData.currentPlayer
    lifes = gameData.lifes
    movesRemaining = gameData.movesRemaining
    score = gameData.score
    powerUp = gameData.powerUp
    levelWidth = gameData.levelWidth
    levelHeight = gameData.levelHeight
    if(cmd == "all")
      receivedAll = true
      
    publish(gameUpdated)
  }
}