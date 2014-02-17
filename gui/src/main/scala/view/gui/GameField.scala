package view.gui

import model.ViewModel
import taczombie.model.util.JsonHelper.Cell
import util.Observer

class GameField(model: ViewModel) extends swing.GridPanel(model.levelWidth, model.levelHeight) {
  focusable = false
  model.cells.foreach(cell => addCell(cell))

  def addCell(cell: ((Int, Int), (Char, Boolean))) {
    contents += new GameCell(model, cell)
  }
}