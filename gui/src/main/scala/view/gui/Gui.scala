package view.gui

import controller.ViewController
import model.ViewModel
import util.Address
import util.Observer
import view.main.IView
import view.main.Main

class Gui(val main: Main) extends swing.Frame with Observer with IView {
  title = "TacZombie"
  iconImage = java.awt.Toolkit.getDefaultToolkit.getImage(getClass.getResource("/images/zombie.png"))

  var address: Address = askForAddress
  var model: ViewModel = new ViewModel
  model.add(this)
  var controller: ViewController = new ViewController(model, main, address.toString)

  if (!controller.connect) {
    contents = new ConnectError(this)
  }

  override def closeOperation() {
    controller.disconnect
    dispose
    sys.exit(0)
  }

  def update {
    if (model.cmd == "all") {
      val gameUI = new GameUI(this, model, controller)
      contents = gameUI

      // Most stupid fix to get key inputs working.
      gameUI.update
      println("updated")
    }
  }

  def askForAddress = new ConnectDialog().address.get

}