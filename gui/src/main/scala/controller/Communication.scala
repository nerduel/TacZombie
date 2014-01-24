package controller

import com.scalaloader.ws.Connected
import com.scalaloader.ws.Connecting
import com.scalaloader.ws.Disconnected
import com.scalaloader.ws.TextMessage
import com.scalaloader.ws.WebSocketClientFactory

class Communication {
  var connected = false

  val wsFactory = WebSocketClientFactory(2)
  var wsUri = new java.net.URI("ws://127.0.0.1:9000/broadcast")

  val wsClient = wsFactory.newClient(wsUri) {
    case Connecting =>
      println("Connecting")
    case Disconnected(_, reason) =>
      println("The websocket ist connected!")
      connected = false
    case TextMessage(_, data) =>
      handleData(data)
    case Connected(_) =>
      println("The websocket is connected!")
      connected = true
  }
  
  def send(msg:String) = {
    while(!connected)
    	Thread.sleep(100)
    	
    wsClient.send(msg)
  }

  def handleData(data: String) {
    //    data match {
    //      case 
    //    }
  }
}

object Communication {
  val wsConnection = new Communication
  wsConnection.wsClient.connect

  def moveUp = wsConnection.send("moveUp")
  def moveDown = wsConnection.send("moveDown")
  def moveLeft = wsConnection.send("moveLeft")
  def moveRight = wsConnection.send("moveRight")
  def newGame = wsConnection.send("newGame")
}