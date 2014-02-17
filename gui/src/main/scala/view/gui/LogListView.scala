package view.gui

import scala.swing.ListView

import model.ViewModel
import util.Observer

class LogListView(model: ViewModel) extends ListView[String] with Observer {
  focusable = false
  model.add(this)

  def update {
    listData = model.log.toSeq
    ensureIndexIsVisible(listData.size - 1)
  }
}