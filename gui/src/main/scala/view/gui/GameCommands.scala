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
import scala.swing.GridBagPanel
import scala.swing.Component
import scala.swing.Separator

case object quit extends Event

class GameCommands(model: ViewModel, controller: Communication) extends BorderPanel {
  focusable = false
  val buttonWidth = 185

  val gridBagPanel = new GridBagPanel {
    border = new CompoundBorder(new TitledBorder(new EtchedBorder, "Move"), new EmptyBorder(5, 5, 5, 10))

    val constraint = new Constraints
    def addToGridBag(x: Int, y: Int, component: Component) {
      constraint.gridx = x
      constraint.fill = GridBagPanel.Fill.Both
      constraint.gridy = y
      add(component, constraint)
    }

    addToGridBag(1, 0, new Button("Up") {
      listenTo(mouse.clicks)
      reactions += {
        case me: MouseClicked => controller.moveUp
      }
    })

    addToGridBag(1, 1, new Button("Down") {
      listenTo(mouse.clicks)
      reactions += {
        case me: MouseClicked => controller.moveDown
      }
    })

    addToGridBag(0, 1, new Button("Left") {
      listenTo(mouse.clicks)
      reactions += {
        case me: MouseClicked => controller.moveLeft
      }
    })

    addToGridBag(2, 1, new Button("Right") {
      listenTo(mouse.clicks)
      reactions += {
        case me: MouseClicked => controller.moveRight
      }
    })

    addToGridBag(0, 3, new Separator)
    addToGridBag(1, 3, new Separator)
    addToGridBag(2, 3, new Separator)

    addToGridBag(0, 4, new Button("RT") {
      tooltip = "Respawn Token <f>"
      listenTo(mouse.clicks)
      reactions += {
        case me: MouseClicked => controller.respawnToken
      }
    })

    addToGridBag(1, 4, new Button("ST") {
      tooltip = "Switch Token <g>"
      listenTo(mouse.clicks)
      reactions += {
        case me: MouseClicked => controller.switchToken
      }
    })

    addToGridBag(2, 4, new Button("NP") {
      tooltip = "Next Player <n>"
      listenTo(mouse.clicks)
      reactions += {
        case me: MouseClicked => controller.nextPlayer
      }
    })
  }

  add(gridBagPanel, BorderPanel.Position.North)

  val buttonNextGame = new Button("Next Game <m>") {
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.nextGame
    }
  }

  val buttonRestartGame = new Button("Restart Game <r>") {
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.restartGame
    }
  }

  val buttonQuit = new Button("Quit <q>") {
    listenTo(mouse.clicks)
    reactions += {
      case me: MouseClicked => controller.disconnect; sys.exit(0)
    }
  }

  add(new BoxPanel(Orientation.Vertical) {
    border = new CompoundBorder(new TitledBorder(new EtchedBorder, "Round"), new EmptyBorder(5, 5, 5, 10))
    contents ++= Seq(buttonNextGame, buttonRestartGame, buttonQuit)
  }, BorderPanel.Position.Center)
}