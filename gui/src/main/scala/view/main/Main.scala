package view.main

import controller.Communication
import model.ViewModel
import view.gui.Address
import view.gui.ConnectDialog
import view.gui.Gui
import view.tui.Tui

object GUI {

  def main(args: Array[String]) {
    show()
  }

  def show() {
    val address = new ConnectDialog().connect.getOrElse(throw new IllegalStateException("Please provide an ip address!"))
    val model = new ViewModel
    val controller = new Communication(model, address)
    val gui = new Gui(model, controller)
    gui.visible = true
  }
}

object TUI {
  def main(args: Array[String]) {
    val model = new ViewModel
    val controller = new Communication(model, new Address("localhost"))
    val tui = new Tui(model, controller)
    tui.show
  }
}
