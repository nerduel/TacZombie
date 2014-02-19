package taczombie.client.view.gui

import taczombie.client.model.ViewModel

class GameField(model: ViewModel) extends swing.GridPanel(model.levelWidth, model.levelHeight) {
  focusable = false
  model.cells.foreach(cell => addCell(cell))

  def addCell(cell: ((Int, Int), (Char, Boolean))) {
    contents += new GameCell(model, cell)
  }
}