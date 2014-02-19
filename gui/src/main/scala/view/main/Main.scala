package view.main

import controller.ViewController
import model.ViewModel
import util.RegexHelper
import view.gui.Address
import view.gui.ConnectDialog
import view.gui.Gui
import view.tui.Tui

trait View

trait Main {
  var view: View = null
  var address: Address = null
  var model: ViewModel = null
  var controller: ViewController = null

  def main(args: Array[String]) {
    show
  }

  def show
  def reconnect
}

object GUI extends Main {
  def show {
    address = new ConnectDialog().address.getOrElse(throw new IllegalStateException("Please provide IP Address!"))
    model = new ViewModel
    controller = new ViewController(model, this, address.toString)
    view = new Gui(model, controller)
    view.asInstanceOf[Gui].open
  }

  def reconnect {
    if (view != null) {
      view.asInstanceOf[Gui].dispose
    }
    show
  }
}

object TUI extends Main {
  def show {
    var restart = true

    while (restart) {
      address = askForAddress()
      model = new ViewModel
      controller = new ViewController(model, this, address.toString)
      view = new Tui(model, controller)
      restart = view.asInstanceOf[Tui].open
    }
  }

  def reconnect {
    if (view != null) {
      println(Console.RED_B + "[CONSOLEFIX] Need to press [ENTER]!" + Console.RESET)
      view.asInstanceOf[Tui].restart = true
      view.asInstanceOf[Tui].continue = false
    }
  }

  def askForAddress(): Address = {
    println("Please provide IP Address:")
    var input = readLine

    while (!RegexHelper.checkAddress(input)) {
      if (input == "q") {
        sys.exit(0)
      }
      println("Invalid IP Address! Please try again!")
      input = readLine
    }

    return new Address(input)
  }

}
