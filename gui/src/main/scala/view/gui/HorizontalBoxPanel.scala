package view.gui

import java.awt.Dimension

import scala.swing.BoxPanel
import scala.swing.Component
import scala.swing.Orientation

import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.EtchedBorder
import javax.swing.border.TitledBorder

class HorizontalBoxPanel(title: String, width: Int, height: Int) extends BoxPanel(Orientation.Vertical) {
  preferredSize = new Dimension(width, height)
  maximumSize = new Dimension(width, height)
  minimumSize = new Dimension(width, height)
  border = new CompoundBorder(new TitledBorder(new EtchedBorder, title), new EmptyBorder(5, 5, 5, 10))

  def add(c: Component) {
    contents += c
  }
}