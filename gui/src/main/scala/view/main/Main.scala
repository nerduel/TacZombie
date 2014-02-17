package view.main

import controller.Communication
import model.ViewModel
import util.Observer
import util.RegexHelper
import view.gui.Address
import view.gui.ConnectDialog
import view.gui.Gui
import view.tui.Tui

trait View {
  def show
}

object GUI extends View {

  var gui : Gui = null
  
  def main(args: Array[String]) {
  	show
  }

  def show {
    if(gui != null)
    	gui.dispose
    val address = new ConnectDialog().connect.getOrElse(throw new IllegalStateException("Please provide IP Address!"))
    val model = new ViewModel
    val controller = new Communication(model, address, this)
    gui = new Gui(model, controller)
    
    gui.visible = true
  }
}

object TUI extends View {
  var tui : Tui = null
  def main(args: Array[String]) {
  	show
  }
  
  def show {
    if(tui != null)
      tui.inputThread.stop()
    val address = askForAddress()
    val model = new ViewModel
    val controller = new Communication(model, address, this)
    tui = new Tui(model, controller)
    tui.show
  }
  
  def askForAddress() : Address =  {
    println("Please provide IP Address:")
    var input = readLine
    
    while(!RegexHelper.checkAddress(input)) {
    	println("Invalid IP Address! Please try again!")
    	input = readLine
    }
    
    return new Address(input)
  }
}
