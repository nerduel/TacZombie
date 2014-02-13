package view

import scala.swing.BorderPanel
import scala.swing.Dimension
import scala.swing.FlowPanel
import scala.swing.Label
import scala.swing.event.Key
import scala.swing.event.KeyPressed

import controller.Communication
import model.ViewModel

class Gui(val model: ViewModel, val controller: Communication) extends swing.Frame {
  title = "TacZombie"

  preferredSize = new Dimension(1024, 768)

  val gameStats = new GameStats(model)
  val gameMessages = new GameMessage(model)
  val gameCommands = new GameCommands(model, controller)
  val gameField = new GameField(model)
  
  contents = new BorderPanel {
    add(new FlowPanel(gameStats), BorderPanel.Position.East)
    add(new Label("north"), BorderPanel.Position.North)
    add(gameMessages, BorderPanel.Position.South)
    add(new FlowPanel(gameCommands), BorderPanel.Position.West)
    add(new FlowPanel(gameField), BorderPanel.Position.Center)
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
      case KeyPressed(_, Key.N, _, _) =>
        controller.newGame
      case KeyPressed(_, Key.Q, _, _) =>
        controller.disconnect
    }
    focusable = true
    requestFocus
  }
}