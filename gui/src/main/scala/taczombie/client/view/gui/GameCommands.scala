package taczombie.client.view.gui

import scala.swing.BoxPanel
import scala.swing.Button
import scala.swing.Orientation

import taczombie.client.controller.ViewController
import taczombie.client.model.ViewModel

class GameCommands(gui: Gui, model: ViewModel, controller: ViewController) extends BoxPanel(Orientation.Vertical) {
  focusable = false

  contents += new HorizontalBoxPanel("Move", 300, 130) {
    add(new GameBagPanel {
      add(Button("↑") { controller.moveUp }, 1, 0)
      add(Button("↓") { controller.moveDown }, 1, 2)
      add(Button("←") { controller.moveLeft }, 0, 1)
      add(Button("→") { controller.moveRight }, 2, 1)
    })
  }

  contents += new HorizontalBoxPanel("Token", 300, 130) {
    add(new GameBagPanel {
      add(Button("Respawn Token <f>") { controller.respawnToken }, 0, 0)
      add(Button("Switch Token <g>") { controller.switchToken }, 0, 1)
      add(Button("Next Player <n>") { controller.nextPlayer }, 0, 2)
    })
  }

  contents += new HorizontalBoxPanel("round", 300, 130) {
    add(new GameBagPanel {
      add(Button("New Game <m>") { controller.nextGame }, 0, 0)
      add(Button("Restart Game <r>") { controller.restartGame }, 0, 1)
      add(Button("Quit <q>") { gui.closeOperation }, 0, 2)
    })
  }
}