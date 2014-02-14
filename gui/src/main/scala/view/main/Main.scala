package view.main

import controller.Communication
import model.ViewModel
import view.tui.Tui
import view.gui.Gui

object MainGui {

  def main(args: Array[String]) {
    val model = new ViewModel
    val controller = new Communication(model)

    val gui = new Gui(model, controller)
    val tui = new Tui(model, controller)
    gui.visible = true
    tui.show
  }
}