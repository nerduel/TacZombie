package controller

import com.scalaloader.ws.Connected
import com.scalaloader.ws.Connecting
import com.scalaloader.ws.Disconnected
import com.scalaloader.ws.TextMessage
import com.scalaloader.ws.WebSocketClientFactory

import model.ViewModel
import spray.json.pimpString
import view.gui.Address
import view.main.Main

class Communication(model: ViewModel,main: Main, address: String, port: String = "9000") {
  private var connected = false
   val wsFactory = WebSocketClientFactory(1)
  private val wsUri = new java.net.URI("ws://" + address + ":" + port + "/broadcast")

  private val wsClient = wsFactory.newClient(wsUri) {
    case Connecting =>
      println("Connecting")
    case Disconnected(_, reason) =>
      println("Disconnected with reason: " + reason)
      if (connected != false) {
        connected = false
        main.reconnect
      }
    case TextMessage(_, data) =>
      handleInput(data)
    case Connected(_) =>
      println("Connected")
      connected = true
    case _ =>
      println _
  }

  connect

  def moveUp = send("moveUp")
  def moveDown = send("moveDown")
  def moveLeft = send("moveLeft")
  def moveRight = send("moveRight")
  def nextGame = send("newGame")
  def switchToken = send("switchToken")
  def respawnToken = send("respawnToken")
  def nextPlayer = send("nextPlayer")
  def restartGame = send("restartGame")
  def connect {
    wsClient.connect
    while (!connected) {
      Thread.sleep(200)
      send("getGameData")
    }
  }
  def disconnect {
    connected = false
    wsClient.disconnect
    wsFactory.shutdownAll
  }

  private def send(msg: String) = {
    if (connected)
      wsClient.send(msg)
  }

  private def handleInput(data: String) {
    if (data.contains("all") || data.contains("updated")) {
      model.toObject(data.asJson)
    } else
      println("Received unknown message Type!")
  }
}