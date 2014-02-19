package taczombie.client.view.gui

import scala.swing.ListView

import taczombie.client.model.ViewModel
import taczombie.client.util.Observer

class LogListView(model: ViewModel) extends ListView[String] with Observer {
  focusable = false
  model.add(this)

  // Init.
  listData = model.log.toSeq

  def update {
    listData = model.log.toSeq
  }
}