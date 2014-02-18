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
import scala.swing._
import javax.swing.border._
import scala.swing.GridBagPanel._

class GameCommands(gui: Gui, model: ViewModel, controller: Communication) extends BoxPanel(Orientation.Vertical) {
  focusable = false

  contents += new BoxPanelButtons("Move") {
    add(new GameButtonBagPanel {
      add(Button("↑") { controller.moveUp }, 1, 0)
      add(Button("↓") { controller.moveDown }, 1, 2)
      add(Button("←") { controller.moveLeft }, 0, 1)
      add(Button("→") { controller.moveRight }, 2, 1)
    })
  }

  contents += new BoxPanelButtons("Token") {
    add(new GameButtonBagPanel {
      add(Button("Respawn Token <f>") { controller.respawnToken }, 0, 0)
      add(Button("Switch Token <g>") { controller.switchToken }, 0, 1)
      add(Button("Next Player <n>") { controller.nextPlayer }, 0, 2)
    })
  }

  contents += new BoxPanelButtons("round") {
    add(new GameButtonBagPanel {
      add(Button("New Game <m>") { controller.nextGame }, 0, 0)
      add(Button("Restart Game <r>") { controller.restartGame }, 0, 1)
      add(Button("Quit <q>") { gui.closeOperation }, 0, 2)
    })
  }
}