package view

import scala.swing.BoxPanel

import model.ViewModel

class GameStats(model: ViewModel) extends BoxPanel(swing.Orientation.Vertical) {

  listenTo(model)
  reactions += {
    case gameUpdated => update
  }
  
  var gameState = new LeftAlignedValueText("GameState:", "n/a")
  var currentPlayerToken = new LeftAlignedValueText("Current Player:","n/a")
  var lifes = new LeftAlignedValueText("Lifes:", "n/a")
  var movesRemaining = new LeftAlignedValueText("Moves remaining:", "n/a")
  var coins = new LeftAlignedValueText("Coins:", "0")
  var score = new LeftAlignedValueText("Score:", "n/a")
  var powerUp = new LeftAlignedValueText("PowerUp", "n/a")
  
  contents += gameState
  contents += currentPlayerToken
  contents += lifes
  contents += movesRemaining
  contents += coins
  contents += score
  contents += powerUp
  
  def update = {
    gameState.update(model.gameState.toString)
    currentPlayerToken.update(model.currentPlayerTokenAsChar.toString)
    lifes.update(model.lifes.toString)
    movesRemaining.update(model.movesRemaining.toString)
    coins.update(model.coins.toString)
    score.update(model.score.toString)
    powerUp.update(model.powerUp.toString)
  }

}