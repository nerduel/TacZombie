package view.main

import controller.Communication
import model.ViewModel
import view.gui.Address
import view.gui.ConnectDialog
import view.gui.Gui
import view.tui.Tui
import util.Observer
import util.RegexHelper

object GUI extends Observer {

  def main(args: Array[String]) {
    update
  }

  def update() {
    val address = new ConnectDialog().connect.getOrElse(throw new IllegalStateException("Please provide an ip address!"))
    val model = new ViewModel
    val controller = new Communication(model, address)
    val gui = new Gui(model, controller)
    gui.visible = true
  }
}

object TUI extends Observer {
  def main(args: Array[String]) {
  	update
  }
  
  def update {
    val address = askForAddress()
    val model = new ViewModel
    val controller = new Communication(model, address)
    val tui = new Tui(model, controller)
    tui.show
  }
  
  def askForAddress() : Address =  {
    println("Please provide ip address of the server, you wanna play on:")
    var input = readLine
    
    while(!RegexHelper.checkAddress(input)) {
    	println("Invalid Input! Try again!")
    	input = readLine
    }
    
    return new Address(input)
  }
}
