package controller

import com.scalaloader.ws.Connected
import com.scalaloader.ws.Connecting
import com.scalaloader.ws.Disconnected
import com.scalaloader.ws.TextMessage
import com.scalaloader.ws.WebSocketClientFactory
import util._
import taczombie.model.GameField
import taczombie.model.Player
import spray.json._
import DefaultJsonProtocol._ // !!! IMPORTANT, else `convertTo` and `toJson` won't work correctly

class Communication {
    var connected = false
    
    val wsFactory = WebSocketClientFactory(2)          
    var wsUri = new java.net.URI("ws://127.0.0.1:9000/broadcast")
                                                    
  	val wsClient = wsFactory.newClient(wsUri) {
  								      case Connecting =>
  								        println ("Connecting")
  								      case Disconnected(_, reason) =>
  								        println ("The websocket ist connected!")
  								        connected = false
  								      case TextMessage(_, data) => 
  								        handleData(data)
  								      case Connected(_) =>
  								        println ("The websocket is connected!")
  								        connected = true
  }
 
  def handleData(data: String) {
//    data match {
//      case GameField() => println("NOT IMPLEMENTED YET!")
//    }
  }
}

object Communication {
  val wsConnection = new Communication
  wsConnection.wsClient.connect
 
  val list: List[Player] = List[Player]()

  
  def send(msg : userMsg) {
    while(!wsConnection.connected) {
      Thread.sleep(100)
    }   
    
    msg match {
      case MoveUp() => wsConnection.wsClient.send("moveUp") 
      case MoveDown() => wsConnection.wsClient.send("moveDown") 
      case MoveLeft() => wsConnection.wsClient.send("moveLeft") 
      case MoveRight() => wsConnection.wsClient.send("moveRight") 
      case NewGame() => wsConnection.wsClient.send("newGame") 
      case _ => 
    }    
  }
}

