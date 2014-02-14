package view

import controller.Communication
import model.ViewModel

object Main {
  
  def main(args: Array[String]) {
    val model = new ViewModel
    val controller = new Communication(model)
  	val gui =  new Gui(model, controller)
    gui.visible = true
    
//    while(true) {
//      model.coins = model.coins + 1
//      model.updated
//      Thread.sleep(1000)
//    }
  }
}