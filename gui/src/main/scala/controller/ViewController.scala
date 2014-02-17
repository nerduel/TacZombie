package controller

import com.scalaloader.ws.Connected
import com.scalaloader.ws.Connecting
import com.scalaloader.ws.Disconnected
import com.scalaloader.ws.TextMessage
import com.scalaloader.ws.WebSocketClientFactory

import model.ViewModel
import spray.json.pimpString
import view.gui.Address
import view.main.GUI

class Communication(model: ViewModel, address: Address) {
  private var connected = false
  private val wsFactory = WebSocketClientFactory(1)
  private val wsUri = new java.net.URI("ws://" + address.toString + ":9000/broadcast")

  private val wsClient = wsFactory.newClient(wsUri) {
    case Connecting =>
      println("Connecting")
    case Disconnected(_, reason) =>
      if (connected != false)
        GUI.show
      println("Disconnected")
    case TextMessage(_, data) =>
      handleInput(data)
    case Connected(_) =>
      println("Connected")
      connected = true
    case _ =>
      println _
  }

  wsClient.connect
  while (!connected) {
    Thread.sleep(200)
    send("getGameData")
  }

  def moveUp = send("moveUp")
  def moveDown = send("moveDown")
  def moveLeft = send("moveLeft")
  def moveRight = send("moveRight")
  def nextGame = send("nextGame")
  def switchToken = send("switchToken")
  def respawnToken = send("respawnToken")
  def nextPlayer = send("nextPlayer")
  def restartGame = send("restartGame")
  def disconnect = {
    connected = false
    wsFactory.shutdownAll
  }

  private def send(msg: String) = {
    if (connected)
      wsClient.send(msg)
  }

  private def handleInput(data: String) {
    if (data.contains("cmd")) {
      model.toObject(data.asJson)
    } else
      println("Received unknown message Type!")
  }
}