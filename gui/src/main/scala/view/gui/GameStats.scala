package view.gui

import scala.swing.BoxPanel
import model.ViewModel
import util.Observer
import scala.swing.Swing
import java.awt.Color
import javax.swing.border._

class GameStats(model: ViewModel) extends BoxPanel(swing.Orientation.Vertical) with Observer {

  model.add(this)

  def update = {
    currentPlayerToken.update(model.currentPlayerTokenAsChar.toString)
    lifes.update(model.lifes.toString)
    movesRemaining.update(model.movesRemaining.toString)
    coins.update(model.coins.toString)
    score.update(model.score.toString)
    powerUp.update(model.powerUp.toString)
  }

  border = new CompoundBorder(new TitledBorder(new EtchedBorder, "GameStats"), new EmptyBorder(5, 5, 5, 10))

  var currentPlayerToken = new LeftAlignedValueText("Current Player:", model.currentPlayerTokenAsChar.toString)
  var lifes = new LeftAlignedValueText("Lifes:", model.lifes.toString)
  var movesRemaining = new LeftAlignedValueText("Moves remaining:", model.movesRemaining.toString)
  var coins = new LeftAlignedValueText("Coins:", model.coins.toString)
  var score = new LeftAlignedValueText("Score:", model.score.toString)
  var powerUp = new LeftAlignedValueText("PowerUp", model.powerUp.toString)

  contents += currentPlayerToken
  contents += lifes
  contents += movesRemaining
  contents += coins
  contents += score
  contents += powerUp
}