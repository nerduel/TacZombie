package view.gui

import java.awt.Color
import java.awt.Dimension

import scala.swing.ScrollPane
import scala.swing.Swing

import model.ViewModel

class Log(model: ViewModel) extends ScrollPane{
  border = Swing.LineBorder(Color.BLACK)
  preferredSize = new Dimension(model.levelWidth * 27, 155)
  horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
  
  contents = new LogListView(model)
}