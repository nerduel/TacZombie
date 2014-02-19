package view.gui

import scala.swing.Dialog
import scala.swing.Component

class ConnectError(gui: Gui) extends Component {
  
  	Dialog.showMessage(this, "Connection was refused!", "ConnectionTimeout", Dialog.Message.Error)
    gui.controller.close
    gui.closeOperation
}