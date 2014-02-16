package view.gui

import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.event.MouseClicked
import controller.Communication
import model.ViewModel
import scala.swing.Action
import scala.swing.event.Event
import scala.swing.BoxPanel
import scala.swing.Orientation
import scala.swing.Swing
import java.awt.Dimension
import javax.swing.border._
import scala.swing.Alignment

case object quit extends Event

class GameCommands(model: ViewModel, controller: Communication) extends BorderPanel {
  border = new CompoundBorder(new TitledBorder(new EtchedBorder, "Commands"), new EmptyBorder(5, 5, 5, 10))

  val buttonUp = new Button("Up") {
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.moveUp
    }
  }

  val buttonDown = new Button("Down") {
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.moveDown
    }
  }

  val buttonLeft = new Button("Left") {
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.moveLeft
    }
  }

  val buttonRight = new Button("Right") {
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.moveRight
    }
  }

  val buttonSwitchToken = new Button("Switch Token") {
    minimumSize = new Dimension(140, 25)
    maximumSize = new Dimension(140, 25)
    preferredSize = new Dimension(140, 25)
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.switchToken
    }
  }

  val buttonNextPlayer = new Button("Next Player") {
    minimumSize = new Dimension(140, 25)
    maximumSize = new Dimension(140, 25)
    preferredSize = new Dimension(140, 25)
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.nextPlayer
    }
  }

  val buttonNextGame = new Button("Next Game") {
    minimumSize = new Dimension(140, 25)
    maximumSize = new Dimension(140, 25)
    preferredSize = new Dimension(140, 25)
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.nextGame
    }
  }

  val buttonRestartGame = new Button("Restart Game") {
    minimumSize = new Dimension(140, 25)
    maximumSize = new Dimension(140, 25)
    preferredSize = new Dimension(140, 25)
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.restartGame
    }
  }

  val buttonQuit = new Button("Quit") {
    minimumSize = new Dimension(140, 25)
    maximumSize = new Dimension(140, 25)
    preferredSize = new Dimension(140, 25)
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.disconnect; sys.exit(0)
    }
  }

  add(buttonUp, BorderPanel.Position.North)
  add(buttonDown, BorderPanel.Position.South)
  add(buttonLeft, BorderPanel.Position.West)
  add(buttonRight, BorderPanel.Position.East)

  add(new BoxPanel(Orientation.Vertical) {
    border = Swing.EmptyBorder(10, 10, 10, 10)
    contents ++= Seq(buttonSwitchToken, buttonNextPlayer, buttonNextGame, buttonRestartGame, buttonQuit)
  }, BorderPanel.Position.Center)
}