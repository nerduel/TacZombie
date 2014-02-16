package view.gui

import scala.swing.Dimension

import controller.Communication
import model.ViewModel
import util.Observer

class Gui(val model: ViewModel, val controller: Communication) extends swing.Frame with Observer {
  title = "TacZombie"
  iconImage = java.awt.Toolkit.getDefaultToolkit.getImage(getClass.getResource("/images/zombie.png"))
  preferredSize = new Dimension(1024, 768)
  
  model.add(this)

  override def closeOperation() {
    controller.disconnect
    sys.exit(0)
  }

  def update {
    if (model.cmd == "all") {
      contents.dropWhile(x => x.enabled)
    	contents = new GameUI(model, controller)
    }
  }


}