package view

import scala.swing.GridPanel
import model.ViewModel
import javax.swing.border.Border
import javax.swing.BorderFactory
import java.awt.Color

class GameField(model : ViewModel) extends GridPanel(model.levelWidth, model.levelHeight){

	border = BorderFactory.createLineBorder(Color.YELLOW, 2)
  
  for(cell <- model.cells) {
	  contents += new GameCell(cell)
	} 
  
  listenTo(model)
  reactions += {
    case gameUpdated => update
  }
  
  def update {
    
  }
}