package taczombie.model.util

import scala.collection.immutable.TreeMap

import taczombie.model.Player

object GameHelper {
	def activePlayer(playerList : TreeMap[String,Player]) : Player = playerList.head._2
}

object GameObjectFactory {
  var counter : Int = 1
  def generateId : Int = {
    counter = counter + 1
    counter
  }
}