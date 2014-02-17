package view.gui

import scala.swing.ListView

import model.ViewModel
import util.Observer

class LogListView(model: ViewModel) extends ListView[String] with Observer {
  focusable = false
  model.add(this)
  
  // Init.
  listData = model.log.toSeq
  
  def update {
    listData = model.log.toSeq
  }
}