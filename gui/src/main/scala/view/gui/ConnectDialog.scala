package view.gui

import scala.swing._
import util.RegexHelper
import scala.swing.event.KeyEvent
import scala.swing.event.KeyPressed
import scala.swing.event.Key

case class Address(ip: String) {
  override def toString: String = {
    return ip
  }
}

class ConnectDialog extends Dialog {
  var connect: Option[Address] = None
  val ip = new TextField {
    listenTo(keys)
    reactions += {
    case KeyPressed(_, Key.Enter, _, _) =>
      handleEvent(this)
    }
  }

  title = "Server IP Address"
  modal = true

  contents = new BorderPanel {
    add(new BoxPanel(Orientation.Vertical) {
      border = Swing.EmptyBorder(5, 5, 5, 5)

      contents += new Label("IP:")
      contents += ip
    }, BorderPanel.Position.North)

    add(new FlowPanel(FlowPanel.Alignment.Right)(
      Button("Connect") {
        handleEvent(this)
      }), BorderPanel.Position.South)
  }

  centerOnScreen()
  open()
  
  def handleEvent(elem : Component) {
    if (RegexHelper.checkAddress(ip.text)) {
          connect = Some(Address(ip.text))
          close()
        } else {
          Dialog.showMessage(elem, "Invalid IP Adress!", "Login Error", Dialog.Message.Error)
        }
  }
  
}