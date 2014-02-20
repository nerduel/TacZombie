package taczombie.client.view.gui

import scala.swing._
import taczombie.client.util.RegexHelper
import scala.swing.event.KeyPressed
import scala.swing.event.Key
import taczombie.client.util.Address
import taczombie.client.util.RegexHelper

class ConnectDialog extends Dialog {
  var address: Option[Address] = None
  val ip = new TextField {
    listenTo(keys)
    reactions += {
      case KeyPressed(_, Key.Enter, _, _) =>
        handleEvent(this)
    }
  }
  val port = new TextField {
    listenTo(keys)
    reactions += {
      case KeyPressed(_, Key.Enter, _, _) =>
        handleEvent(this)
    }
  }
  preferredSize = new Dimension(200, 150)

  title = "Server Address"
  modal = true

  contents = new BorderPanel {
    add(new BoxPanel(Orientation.Vertical) {
      border = Swing.EmptyBorder(5, 5, 5, 5)

      contents += new Label("IP:")
      contents += ip
      contents += new Label("Port:")
      contents += port
    }, BorderPanel.Position.North)

    add(new FlowPanel(FlowPanel.Alignment.Center)(
      new FlowPanel(FlowPanel.Alignment.Left)(Button("Connect") { handleEvent(this) }),
      new FlowPanel(FlowPanel.Alignment.Right)(Button("Quit") { sys.exit(0) })), BorderPanel.Position.South)
  }

  centerOnScreen()
  open()

  def handleEvent(elem: Component) {
    if (RegexHelper.checkPort(port.text)) {
      address = Some(Address(ip.text, port.text))
      close()
    } else {
      Dialog.showMessage(elem, "Invalid Adress!", "Login Error", Dialog.Message.Error)
    }
  }

}