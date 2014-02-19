package taczombie.client.view.gui

import scala.swing.Component
import scala.swing.Dialog

class ConnectError(gui: Gui) extends Component {

  Dialog.showMessage(this, "Connection was refused!", "ConnectionTimeout", Dialog.Message.Error)
  gui.controller.close
  gui.closeOperation
}