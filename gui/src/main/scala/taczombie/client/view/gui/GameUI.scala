package taczombie.client.view.gui

import scala.swing.BorderPanel
import scala.swing.FlowPanel
import scala.swing.event.Key
import scala.swing.event.KeyPressed
import taczombie.client.controller.ViewController
import taczombie.client.model.ViewModel
import taczombie.client.util.Observer
import scala.swing.Orientation
import scala.swing.BoxPanel

class GameUI(gui: Gui, model: ViewModel, controller: ViewController) extends BorderPanel with Observer {
  focusable = true
  model.add(this)

  listenTo(keys)
  reactions += {
    case KeyPressed(_, Key.Up, _, _) =>
      controller.moveUp
    case KeyPressed(_, Key.Down, _, _) =>
      controller.moveDown
    case KeyPressed(_, Key.Left, _, _) =>
      controller.moveLeft
    case KeyPressed(_, Key.Right, _, _) =>
      controller.moveRight
    case KeyPressed(_, Key.G, _, _) =>
      controller.switchToken
    case KeyPressed(_, Key.F, _, _) =>
      controller.respawnToken
    case KeyPressed(_, Key.N, _, _) =>
      controller.nextPlayer
    case KeyPressed(_, Key.M, _, _) =>
      controller.nextGame
    case KeyPressed(_, Key.R, _, _) =>
      controller.restartGame
    case KeyPressed(_, Key.Q, _, _) =>
      gui.closeOperation
  }

  // Make sure this gets focus after every update, 
  // otherwise key inputs wouldnt be recognized.
  def update {
    requestFocusInWindow
  }

  add(new GameMessage(model), BorderPanel.Position.North)

  add(new FlowPanel(new BoxPanel(Orientation.Vertical) {
    contents += new GameStats(model)
    contents += new GameCommands(gui, model, controller)
  }), BorderPanel.Position.West)

  add(new FlowPanel(new BoxPanel(Orientation.Vertical) {
    contents += new GameField(model)
    contents += new Log(model)
  }), BorderPanel.Position.East)

  requestFocusInWindow
}