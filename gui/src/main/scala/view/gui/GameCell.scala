package view.gui

import java.awt.Color

import javax.swing.BorderFactory
import javax.swing.ImageIcon
import model.ViewModel
import util.Observer

class GameCell(model: ViewModel, cell: ((Int, Int), (Char, Boolean)))
  extends swing.Label with Observer {

  model.add(this)

  updateIcon(cell._2._1, cell._2._2)

  def update {
    if (model.cmd == "updated") {
      val currentCell = model.cells(cell._1._1, cell._1._2)
      updateIcon(currentCell._1, currentCell._2)
    }
  }

  def updateIcon(token: Char, isHighlighted: Boolean) {
    token match {
      case 'C' =>
        icon = if (isHighlighted)
          new ImageIcon(getClass.getResource("/images/hCoin.png"))
        else
          new ImageIcon(getClass.getResource("/images/coin.png"))
      case 'H' =>
        icon = if (isHighlighted)
          new ImageIcon(getClass.getResource("/images/hHuman.png"))
        else
          new ImageIcon(getClass.getResource("/images/human.png"))
      case 'Z' =>
        icon = if (isHighlighted)
          new ImageIcon(getClass.getResource("/images/hZombie.png"))
        else
          new ImageIcon(getClass.getResource("/images/zombie.png"))
      case 'P' =>
        icon = if (isHighlighted)
          new ImageIcon(getClass.getResource("/images/hPowerup.png"))
        else
          new ImageIcon(getClass.getResource("/images/powerup.png"))
      case 'N' =>
        icon = if (isHighlighted)
          new ImageIcon(getClass.getResource("/images/hEmpty.png"))
        else
          new ImageIcon(getClass.getResource("/images/empty.png"))
      case 'W' => icon = new ImageIcon(getClass.getResource("/images/wall.png"))
      case _ => icon = new ImageIcon(getClass.getResource("/images/wall.png"))
    }
  }

  border = BorderFactory.createLineBorder(Color.BLACK, 1)
}