package view.gui

import scala.swing.ListView

import model.ViewModel
import util.Observer

class LogListView(model: ViewModel) extends ListView[String] with Observer {
  model.add(this)

  def update {
    listData = listData.reverse
    listData ++= model.log.toSeq
    listData = listData.reverse
  }
}