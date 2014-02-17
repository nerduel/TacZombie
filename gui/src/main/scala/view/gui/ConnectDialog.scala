package view.gui

import scala.swing._

case class Address(ip: String) {
  override def toString: String = {
    return ip
  }
}

class ConnectDialog extends Dialog {
  var connect: Option[Address] = None
  val ip = new TextField
  val port = new TextField

  title = "IP:PORT"
  modal = true

  contents = new BorderPanel {
    add(new BoxPanel(Orientation.Vertical) {
      border = Swing.EmptyBorder(5, 5, 5, 5)

      contents += new Label("IP:")
      contents += ip
    }, BorderPanel.Position.North)

    add(new FlowPanel(FlowPanel.Alignment.Right)(
      Button("Connect") {
        if (checkAddress()) {
          connect = Some(Address(ip.text))
          close()
        } else {
          Dialog.showMessage(this, "Invalid IP Adress!", "Login Error", Dialog.Message.Error)
        }
      }), BorderPanel.Position.South)
  }

  def checkAddress(): Boolean = {
    val ipPatternNumber = """^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}""".r

    if (ip.text == "localhost" || ipPatternNumber.findFirstIn(ip.text) != None) {
      return true
    } else {
      return false
    }
  }

  centerOnScreen()
  open()
}