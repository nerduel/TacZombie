package taczombie.client.view.gui

import scala.swing.Component
import scala.swing.GridBagPanel

class GameBagPanel extends GridBagPanel {
  val constraint = new Constraints

  def add(c: Component, x: Int, y: Int) {
    constraint.gridx = x
    constraint.gridy = y
    constraint.fill = GridBagPanel.Fill.Both
    super.add(c, constraint)
  }
}