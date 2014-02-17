package view.gui

import scala.swing.BoxPanel
import model.ViewModel
import util.Observer
import scala.swing.Swing
import java.awt.Color
import javax.swing.border._

class GameStats(model: ViewModel) extends BoxPanel(swing.Orientation.Vertical) with Observer {
  focusable = false
  model.add(this)

  border = new CompoundBorder(new TitledBorder(new EtchedBorder, "GameStats"), new EmptyBorder(5, 5, 5, 10))

  // Init.  
  var currentPlayerToken = new LeftAlignedValueText("Current Player:", charToPlayer(model.currentPlayerTokenAsChar))
  var totalTokens = new LeftAlignedValueText("Total Tokens:", model.totalTokens.toString)
  var deadTokens = new LeftAlignedValueText("Dead Tokens:", model.deadTokens.toString)
  var frozenTime = new LeftAlignedValueText("Frozen Time:", model.frozenTime.toString)
  var lifes = new LeftAlignedValueText("Lifes:", model.lifes.toString)
  var movesRemaining = new LeftAlignedValueText("Moves remaining:", model.movesRemaining.toString)
  var coins = new LeftAlignedValueText("Coins:", model.coins.toString)
  var score = new LeftAlignedValueText("Score:", model.score.toString)
  var powerUp = new LeftAlignedValueText("PowerUp", model.powerUp.toString)

  contents += currentPlayerToken
  contents += totalTokens
  contents += deadTokens
  contents += frozenTime
  contents += movesRemaining
  contents += lifes
  contents += coins
  contents += score
  contents += powerUp
  
  def update = {
    currentPlayerToken.update(charToPlayer(model.currentPlayerTokenAsChar))
    totalTokens.update(model.totalTokens.toString)
    deadTokens.update(model.deadTokens.toString)
    lifes.update(model.lifes.toString)
    movesRemaining.update(model.movesRemaining.toString)
    coins.update(model.coins.toString)
    score.update(model.score.toString)
    powerUp.update(model.powerUp.toString)

    if (model.currentPlayerTokenAsChar == 'Z') {
      contents -= lifes
      contents -= coins
      contents -= score
      contents -= powerUp
    } else {
      contents += lifes
      contents += coins
      contents += score
      contents += powerUp
    }
  }

  def charToPlayer(char: Char): String = {
    char match {
      case 'H' => return "Human"
      case 'Z' => return "Zombie"
      case _ => return "Unknown"
    }
  }
}