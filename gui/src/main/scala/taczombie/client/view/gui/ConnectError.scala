package taczombie.client.view.gui

import scala.swing.Component
import scala.swing.Dialog

class ConnectError(gui: Gui, msg: String) extends Component {

  Dialog.showMessage(this, msg, "Connection Failed", Dialog.Message.Error)
  gui.controller.close
  gui.closeOperation
}