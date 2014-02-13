package view

import java.awt.Color

import scala.swing.Label

import taczombie.model.util.JsonHelper.Cell

class GameCell(val c: Cell)
  extends swing.Component {

  val valueLabel = new Label("#")

  def update(highlighted : Boolean, text: String) {
    
    valueLabel.text = c.token.toString
    valueLabel.foreground = if(highlighted) Color.YELLOW else Color.BLACK
    
  }
}