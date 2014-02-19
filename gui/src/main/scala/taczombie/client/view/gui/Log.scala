package taczombie.client.view.gui

import java.awt.Dimension
import scala.swing.ScrollPane
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.EtchedBorder
import javax.swing.border.TitledBorder
import taczombie.client.model.ViewModel

class Log(model: ViewModel) extends ScrollPane {
  focusable = false
  border = new CompoundBorder(new TitledBorder(new EtchedBorder, "Log"), new EmptyBorder(5, 5, 5, 10))
  preferredSize = new Dimension(540, 136)
  contents = new LogListView(model)
}