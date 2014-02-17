package view.gui

import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import model.ViewModel
import util.Observer
import scala.swing.Swing
import scala.swing.Alignment

class GameCell(model: ViewModel, cell: ((Int, Int), (Char, Boolean)))
  extends swing.Label with Observer {
  focusable = false
  model.add(this)

  horizontalAlignment = Alignment.Center
  verticalAlignment = Alignment.Center

  updateIcon(cell._1._1, cell._1._2)

  def update {
    if (model.cmd == "updated") {
      model.cells(cell._1._1, cell._1._2)
      updateIcon(cell._1._1, cell._1._2)
    }
  }

  def updateIcon(x: Int, y: Int) {
    val cell = model.cells(x,y)
    val token = cell._1
    val isHighlighted = cell._2
    
    token match {
      case 'C' =>
        icon = if (isHighlighted)
          new ImageIcon(getClass.getResource("/images/hCoin.png"))
        else
          new ImageIcon(getClass.getResource("/images/coin.png"))
      case 'H' =>
        var powerUp = false
        if (model.humanTokens.contains(x,y)) {
          powerUp = model.humanTokens(x,y)
        }
        icon = if (isHighlighted) {
          if (powerUp) {
            new ImageIcon(getClass.getResource("/images/hHumanPowerup.png"))
          } else {
            new ImageIcon(getClass.getResource("/images/hHuman.png"))
          }
        } else {
          if (powerUp) {
            new ImageIcon(getClass.getResource("/images/humanPowerup.png"))
          } else {
            new ImageIcon(getClass.getResource("/images/human.png"))
          }
        }
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

  border = Swing.LineBorder(Color.BLACK)
}