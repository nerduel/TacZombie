package view.gui

import scala.swing._
import util.RegexHelper
import scala.swing.event.KeyEvent
import scala.swing.event.KeyPressed
import scala.swing.event.Key
import javax.swing.Box
import util.Address

class ConnectDialog extends Dialog {
  var address: Option[Address] = None
  val ip = new TextField {
    listenTo(keys)
    reactions += {
      case KeyPressed(_, Key.Enter, _, _) =>
        handleEvent(this)
    }
  }
  preferredSize = new Dimension(200, 120)

  title = "Server IP Address"
  modal = true

  contents = new BorderPanel {
    add(new BoxPanel(Orientation.Vertical) {
      border = Swing.EmptyBorder(5, 5, 5, 5)

      contents += new Label("IP:")
      contents += ip
    }, BorderPanel.Position.North)

    add(new FlowPanel(FlowPanel.Alignment.Center)(
      new FlowPanel(FlowPanel.Alignment.Left)(Button("Connect") { handleEvent(this) }),
      new FlowPanel(FlowPanel.Alignment.Right)(Button("Quit") { sys.exit(0) })), BorderPanel.Position.South)
  }

  centerOnScreen()
  open()

  def handleEvent(elem: Component) {
    if (RegexHelper.checkAddress(ip.text)) {
      address = Some(Address(ip.text))
      close()
    } else {
      Dialog.showMessage(elem, "Invalid IP Adress!", "Login Error", Dialog.Message.Error)
    }
  }

}