package controller

import com.scalaloader.ws.Connected
import com.scalaloader.ws.Connecting
import com.scalaloader.ws.Disconnected
import com.scalaloader.ws.TextMessage
import com.scalaloader.ws.WebSocketClientFactory
import util.Observable
import model.ViewModel
import spray.json.DefaultJsonProtocol
import spray.json._

class Communication(model: ViewModel)  {
  private var connected = false
  private var message = ""

  private val wsFactory = WebSocketClientFactory(1)
  private var wsUri = new java.net.URI("ws://127.0.0.1:9000/broadcast")

  private val wsClient = wsFactory.newClient(wsUri) {
    case Connecting =>
      println("Connecting")
    case Disconnected(_, reason) =>
      connected = false
    case TextMessage(_, data) =>
      handleInput(data)
      message = data
    case Connected(_) =>
      println("Connected")
      connected = true
  }

  wsClient.connect

  def moveUp = send("moveUp")
  def moveDown = send("moveDown")
  def moveLeft = send("moveLeft")
  def moveRight = send("moveRight")
  def newGame = send("newGame")
  def disconnect = {
    wsClient.disconnect
    wsFactory.shutdownAll
  }

  private def send(msg: String) = {
    if (connected)
      wsClient.send(msg)
  }
  
  private def handleInput(data : String) {
    model.toObject(data.asJson)
  }
}