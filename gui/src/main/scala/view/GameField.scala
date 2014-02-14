package view

import model.ViewModel
import taczombie.model.util.JsonHelper.Cell

class GameField(model: ViewModel) extends swing.GridPanel(model.levelWidth, model.levelHeight) {
  
  model.cells.foreach(cell => addCell(cell))
      
  def addCell(cell: ((Int,Int),(Char,Boolean))) {
    println(cell)
    contents += new GameCell(model, cell)
    }
}