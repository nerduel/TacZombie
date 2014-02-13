package view

import scala.swing.Label

import model.ViewModel

class GameMessage(model: ViewModel) extends Label {
	text = ""
  listenTo(model)
  reactions += {
    case gameUpdated => update
  }
	
	def update {
	  text = "TODO: Implement Message in Class:GameMessage"
	}
}