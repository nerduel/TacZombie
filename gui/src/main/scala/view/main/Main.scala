package view.main

import controller.Communication
import model.ViewModel
import view.gui.Gui
import view.tui.Tui

object GUI {

  def main(args: Array[String]) {
    val model = new ViewModel
    val controller = new Communication(model)
    val gui = new Gui(model, controller)
    gui.visible = true
  }
}

object TUI {

  def main(args: Array[String]) {
    val model = new ViewModel
    val controller = new Communication(model)
    val tui = new Tui(model, controller)
    tui.show
  }
}