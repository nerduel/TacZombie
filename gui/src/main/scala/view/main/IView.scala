package view.main

import util.Address

trait IView {
  val main: Main
  def open: Unit
  def dispose: Unit
  def askForAddress: Address
  
  def reconnect {
    dispose
    main.show
  }
}