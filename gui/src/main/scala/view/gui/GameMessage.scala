package view.gui

import scala.swing.Label
import model.ViewModel
import util.Observer
import javax.swing.border._
import java.awt.Dimension

class GameMessage(model: ViewModel) extends Label with Observer {
  focusable = false
  border = new CompoundBorder(new TitledBorder(new EtchedBorder, "Message"), new EmptyBorder(5, 5, 5, 10))
  text = "Welcome to TacZombie!"
    
  model.add(this)

  def update {
    text = model.gameMessage
  }
}