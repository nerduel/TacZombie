package view.gui

import scala.swing.BoxPanel
import scala.swing.Orientation
import java.awt.Dimension
import javax.swing.border._
import scala.swing.Component

class BoxPanelButtons(title: String) extends BoxPanel(Orientation.Vertical) {
  preferredSize = new Dimension(300, 130)
  maximumSize = new Dimension(300, 130)
  minimumSize = new Dimension(300, 130)
  border = new CompoundBorder(new TitledBorder(new EtchedBorder, title), new EmptyBorder(5, 5, 5, 10))

  def add(c: Component) {
    contents += c
  }

}