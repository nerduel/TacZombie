package taczombie.client.controller

import com.scalaloader.ws.Connected
import com.scalaloader.ws.Connecting
import com.scalaloader.ws.Disconnected
import com.scalaloader.ws.TextMessage
import com.scalaloader.ws.WebSocketClientFactory
import taczombie.client.model.ViewModel
import spray.json.pimpString
import java.util.concurrent.ExecutionException
import taczombie.client.view.main.IView

class ViewController(model: ViewModel, view: IView, address: String, port: String = "9000") {
  private var connected = false
  val wsFactory = WebSocketClientFactory(1)
  private val wsUri = new java.net.URI("ws://" + address + ":" + port + "/broadcast")

  private val wsClient = wsFactory.newClient(wsUri)({
    case Connecting =>
      println("Connecting")
    case Disconnected(_, reason) =>
      println("Disconnected with reason: " + reason)
      if (connected != false) {
        connected = false
        view.stop
      }
    case TextMessage(_, data) =>
      handleInput(data)
    case Connected(_) =>
      println("Connected")
      connected = true
    case _ =>

      println _
  })

  def moveUp = send("moveUp")
  def moveDown = send("moveDown")
  def moveLeft = send("moveLeft")
  def moveRight = send("moveRight")
  def nextGame = send("newGame")
  def switchToken = send("switchToken")
  def respawnToken = send("respawnToken")
  def nextPlayer = send("nextPlayer")
  def restartGame = send("restartGame")

  def connect: Boolean = {
    try {
      val ws = wsClient.connect.get
    } catch {
      case ex: ExecutionException =>
        println("Connection refused: " + address + ":" + port)
        return false
    }

    while (!connected) {
      Thread.sleep(200)
    }

    send("getGameData")

    (true)
  }
  def disconnect {
    connected = false
    wsClient.disconnect
    close
  }

  def close {
    wsFactory.shutdownAll
  }

  private def send(msg: String) = {
    if (connected)
      wsClient.send(msg)
  }

  private def handleInput(data: String) {
    if (data.contains("cmd"))
      model.toObject(data.asJson)
    else
      println("Received unknown message Type!")
  }
}