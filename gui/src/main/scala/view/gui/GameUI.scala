package view.gui

import scala.swing.BorderPanel
import scala.swing.FlowPanel
import scala.swing.Label
import scala.swing.event.Key
import scala.swing.event.KeyPressed
import controller.Communication
import model.ViewModel
import util.Observer
import scala.swing.ScrollPane
import scala.swing.Separator
import scala.swing.Orientation
import scala.swing.BoxPanel
import javax.swing.border.CompoundBorder
import javax.swing.border.TitledBorder
import javax.swing.border.EtchedBorder
import javax.swing.border.EmptyBorder

class GameUI(model: ViewModel, controller: Communication) extends BorderPanel with Observer {
  focusable = true
  model.add(this)

  // Make sure this gets focus after every update, 
  // otherwise key inputs wouldnt be recognized.
  def update {
    requestFocus
  }

  add(new GameMessage(model), BorderPanel.Position.South)
  add(new FlowPanel(new BoxPanel(Orientation.Vertical) {
    contents += new GameCommands(model, controller)
    contents += new GameStats(model)
  }), BorderPanel.Position.West)
  add(new FlowPanel(new BoxPanel(Orientation.Vertical) {
    contents += new GameField(model)
    contents += new Log(model)
  }), BorderPanel.Position.Center)

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
    case KeyPressed(_, Key.H, _, _) =>
      controller.nextPlayer
    case KeyPressed(_, Key.N, _, _) =>
      controller.nextGame
    case KeyPressed(_, Key.R, _, _) =>
      controller.restartGame
    case KeyPressed(_, Key.Q, _, _) =>
      controller.disconnect
      sys.exit(0)
  }
  requestFocus
}