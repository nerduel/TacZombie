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
import scala.swing.Label

case object quit extends Event

class GameCommands(model: ViewModel, controller: Communication) extends BoxPanel(Orientation.Vertical) {
  focusable = false

  contents += new BoxPanel(Orientation.Vertical) {
    preferredSize = new Dimension(300, 130)
    maximumSize = new Dimension(300, 130)
    minimumSize = new Dimension(300, 130)
    border = new CompoundBorder(new TitledBorder(new EtchedBorder, "Move"), new EmptyBorder(5, 5, 5, 10))
    contents += new GridBagPanel {
      val constraint = new Constraints
      def addToGridBag(x: Int, y: Int, component: Component) {
        constraint.gridx = x
        constraint.fill = GridBagPanel.Fill.Both
        constraint.gridy = y
        add(component, constraint)
      }

      addToGridBag(1, 0, new Button("↑") {
        listenTo(mouse.clicks)
        reactions += {
          case me: MouseClicked => controller.moveUp
        }
      })

      addToGridBag(1, 2, new Button("↓") {
        listenTo(mouse.clicks)
        reactions += {
          case me: MouseClicked => controller.moveDown
        }
      })

      addToGridBag(0, 1, new Button("←") {
        listenTo(mouse.clicks)
        reactions += {
          case me: MouseClicked => controller.moveLeft
        }
      })

      addToGridBag(2, 1, new Button("→") {
        listenTo(mouse.clicks)
        reactions += {
          case me: MouseClicked => controller.moveRight
        }
      })
    }
  }

  contents += new BoxPanel(Orientation.Vertical) {
    preferredSize = new Dimension(300, 130)
    maximumSize = new Dimension(300, 130)
    minimumSize = new Dimension(300, 130)
    border = new CompoundBorder(new TitledBorder(new EtchedBorder, "Token"), new EmptyBorder(5, 5, 5, 10))
    contents += new GridBagPanel {
      val constraint = new Constraints
      def addToGridBag(x: Int, y: Int, component: Component) {
        constraint.gridx = x
        constraint.fill = GridBagPanel.Fill.Both
        constraint.gridy = y
        add(component, constraint)
      }

      addToGridBag(0, 0, new Button("Respawn Token <f>") {
        listenTo(mouse.clicks)
        reactions += {
          case me: MouseClicked => controller.respawnToken
        }
      })

      addToGridBag(0, 1, new Button("Switch Token <g>") {
        listenTo(mouse.clicks)
        reactions += {
          case me: MouseClicked => controller.switchToken
        }
      })

      addToGridBag(0, 2, new Button("Next Player <n>") {
        listenTo(mouse.clicks)
        reactions += {
          case me: MouseClicked => controller.nextPlayer
        }
      })
    }
  }

  contents += new BoxPanel(Orientation.Vertical) {
    preferredSize = new Dimension(300, 130)
    maximumSize = new Dimension(300, 130)
    minimumSize = new Dimension(300, 130)
    border = new CompoundBorder(new TitledBorder(new EtchedBorder, "Round"), new EmptyBorder(5, 5, 5, 10))
    contents += new GridBagPanel {
      val constraint = new Constraints
      def addToGridBag(x: Int, y: Int, component: Component) {
        constraint.gridx = x
        constraint.fill = GridBagPanel.Fill.Both
        constraint.gridy = y
        add(component, constraint)
      }

      addToGridBag(0, 0, new Button("Next Game <m>") {
        listenTo(mouse.clicks)
        reactions += {
          case me: MouseClicked => controller.nextGame
        }
      })

      addToGridBag(0, 1, new Button("Restart Game <r>") {
        listenTo(mouse.clicks)
        reactions += {
          case me: MouseClicked => controller.restartGame
        }
      })

      addToGridBag(0, 2, new Button("Quit <q>") {
        listenTo(mouse.clicks)
        reactions += {
          case me: MouseClicked => controller.disconnect; sys.exit(0)
        }
      })
    }
  }
}