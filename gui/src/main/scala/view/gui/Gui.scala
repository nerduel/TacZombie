package view.gui

import controller.Communication
import model.ViewModel
import util.Observer
import view.main.View

class Gui(val model: ViewModel, val controller: Communication) extends swing.Frame with Observer with View {
  title = "TacZombie"
  iconImage = java.awt.Toolkit.getDefaultToolkit.getImage(getClass.getResource("/images/zombie.png"))
  model.add(this)

  override def closeOperation() {
    controller.disconnect
    sys.exit(0)
  }

  def update {
    if (model.cmd == "all") {
      val gameUI = new GameUI(model, controller)
      contents = gameUI

      // Most stupid fix to get key inputs working.
      gameUI.update
    }

  }

}