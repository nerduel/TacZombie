package view.gui

import scala.swing.BoxPanel
import model.ViewModel
import util.Observer
import scala.swing.Swing
import java.awt.Color
import javax.swing.border._
import java.awt.Dimension
import scala.collection.mutable.Buffer
import scala.swing.Label
import scala.swing.Component

class GameStats(model: ViewModel) extends BoxPanel(swing.Orientation.Vertical) with Observer {
  focusable = false
  model.add(this)

  border = new CompoundBorder(new TitledBorder(new EtchedBorder, "GameStats"), new EmptyBorder(5, 5, 5, 10))
  preferredSize = new Dimension(300, 220)
  maximumSize = new Dimension(300, 220)
  minimumSize = new Dimension(300, 220)

  paintContent

  def update = {
    paintContent
  }

  def paintContent {
    contents.clear

    val buffer = Buffer.empty[Component]

    buffer += new LeftAlignedValueText("Current Player:", charToPlayer(model.currentPlayerTokenAsChar))
    buffer += new LeftAlignedValueText("Total Tokens:", model.totalTokens.toString)
    buffer += new LeftAlignedValueText("Dead Tokens:", model.deadTokens.toString)
    buffer += new LeftAlignedValueText("Moves remaining:", model.movesRemaining.toString)

    if (model.currentPlayerTokenAsChar == 'H') {
      buffer += new LeftAlignedValueText("Lifes:", model.lifes.toString)
      buffer += new LeftAlignedValueText("Coins Collected:", model.coins.toString)
      buffer += new LeftAlignedValueText("Score:", model.score.toString)
      buffer += new LeftAlignedValueText("PowerUp Time", model.powerUp.toString)
    }
    buffer += new LeftAlignedValueText("Frozen Time:", model.frozenTime.toString)

    contents ++= buffer

    revalidate
    repaint
  }

  def charToPlayer(char: Char): String = {
    char match {
      case 'H' => return "Human"
      case 'Z' => return "Zombie"
      case _ => return "Unknown"
    }
  }
}