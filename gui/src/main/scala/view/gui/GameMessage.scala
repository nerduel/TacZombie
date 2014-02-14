package view.gui

import scala.swing.Label
import model.ViewModel
import util.Observer

class GameMessage(model: ViewModel) extends Label with Observer {

  model.add(this)

  def update {
    text = "TODO: Implement Message in Class:GameMessage"
  }
}