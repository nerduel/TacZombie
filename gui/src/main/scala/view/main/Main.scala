package view.main

import controller.Communication
import model.ViewModel
import util.RegexHelper
import view.gui.Address
import view.gui.ConnectDialog
import view.gui.Gui
import view.tui.Tui

trait View {

  def main(args: Array[String]) {
    show
  }

  def show
  def reconnect
}

object GUI extends View {
  var gui: Gui = null

  def show {
    val address = new ConnectDialog().address.getOrElse(throw new IllegalStateException("Please provide IP Address!"))
    val model = new ViewModel
    val controller = new Communication(model, address, this)
    gui = new Gui(model, controller)
    gui.visible = true
  }

  def reconnect {
    if (gui != null)
      gui.dispose
    show
  }
}

object TUI extends View {
  var tui: Tui = null
  def show {
    val address = askForAddress()
    val model = new ViewModel
    val controller = new Communication(model, address, this)
    tui = new Tui(model, controller)
    tui.show
  }

  def reconnect {
    if (tui != null)
      tui.inputThread.stop()
    show
  }

  def askForAddress(): Address = {
    println("Please provide IP Address:")
    var input = readLine

    while (!RegexHelper.checkAddress(input)) {
      if(input == "q") {
        sys.exit(0)
      }
      println("Invalid IP Address! Please try again!")
      input = readLine
    }

    return new Address(input)
  }

}
