package taczombie.model.util

import scala.collection.immutable.TreeMap

import taczombie.model.GameObject
import taczombie.model.Player
import taczombie.model.PlayerToken

object GameHelper {
	def activePlayer(playerList : TreeMap[Int,Player]) : Player = playerList.head._2
}

object GameObjectFactory {
  var counter : Int = 1
  def generateId : Int = {
    counter = counter + 1
    counter
  }
}