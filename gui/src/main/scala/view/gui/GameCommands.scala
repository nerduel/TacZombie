package view.gui

import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.event.MouseClicked
import controller.Communication
import model.ViewModel
import taczombie.model.Quit
import javax.swing.Action
import scala.swing.event.Event

case object quit extends Event

class GameCommands(model: ViewModel, controller: Communication) extends BorderPanel {

  val buttonUp = new Button("Up") {
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.moveUp; println("up")
    }
  }
  val buttonDown = new Button("Down") {
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.moveDown; println("down")
    }
  }
  val buttonLeft = new Button("Left") {
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.moveLeft; println("left")
    }
  }
  val buttonRight = new Button("Right") {
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.moveRight; println("right")
    }
  }

  val buttonQuit = new Button("Quit") {

    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.disconnect; println("disconnect"); exit(0)
    }
  }

  add(buttonUp, BorderPanel.Position.North)
  add(buttonDown, BorderPanel.Position.South)
  add(buttonLeft, BorderPanel.Position.West)
  add(buttonRight, BorderPanel.Position.East)
  add(buttonQuit, BorderPanel.Position.Center)

}