package view.gui

import java.awt.Color

import scala.swing.Label

import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.EtchedBorder
import javax.swing.border.TitledBorder
import model.ViewModel
import util.Observer

class GameMessage(model: ViewModel) extends Label with Observer {
  focusable = false
  border = new CompoundBorder(new TitledBorder(new EtchedBorder, "Message"), new EmptyBorder(5, 5, 5, 10))
  text = "Welcome to TacZombie!"
  foreground = Color.red

  model.add(this)

  def update {
    text = model.gameMessage
  }
}