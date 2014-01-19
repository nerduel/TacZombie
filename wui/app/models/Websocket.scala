package models

import scala.collection.immutable.Map
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import akka.actor.Actor
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.pattern.ask
import akka.util.Timeout
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Concurrent
import play.api.libs.iteratee.Done
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Input
import play.api.libs.iteratee.Iteratee
import scala.collection.mutable.ListBuffer
import scala.util.Random

object Websocket {

  class Game {
    val id = 0
    def moveEast() = {}
    def moveNorth() = {}
    def moveSouth() = {}
    def moveWest() = {}
  }
  
  var outchannels = Map[Int, ListBuffer[play.api.libs.iteratee.Concurrent.Channel[String]]](0 -> new ListBuffer[play.api.libs.iteratee.Concurrent.Channel[String]])
  var games = Map[Int, Game]()
  var currentGameId = Map[play.api.libs.iteratee.Concurrent.Channel[String], Int]()

  def updateTest() {
    for (x <- 0 to 5) {
      outchannels.keys.filter(_ != 0).foreach(updateView(_))
      Thread.sleep(5000);
    }
  }

  def updateView(gameId: Int) = {
    outchannels(gameId).foreach(_ push ("View update available!"))
  }

  def join(out: Enumerator[String], channel: Concurrent.Channel[String]) = {
    // Add channel to default list.
    moveChannelToNewGroup(0, channel)

    val in = Iteratee.foreach[String] {
      msg => handleInput(msg, channel)
    }

    (in, out)
  }

  def handleInput(input: String, channel: Concurrent.Channel[String]) {
    input.split(" ") match {
      case Array("newGame") =>
        var gameId = Random.nextInt
        
        while(games.get(gameId) != None)
          gameId = Random.nextInt
                    
        moveChannelToNewGroup(gameId, channel)
        games += gameId -> new Game()
        channel.push("Created gameId: " + gameId)
        
        // Update View From Single Channel

      case Array("joinGame", gameId) =>
        moveChannelToNewGroup(gameId.toInt, channel)
        channel.push("Joined gameId: " + gameId)
        
        // Update View From Single Channel.

      case Array("quitGame") => {
        removeChannelFromCurrentGroup(channel)
        channel.eofAndEnd
      }

      case Array("move", gameId, direction) => {
        direction match {
          case "e" => games(gameId.toInt).moveEast()
          case "n" => games(gameId.toInt).moveNorth()
          case "s" => games(gameId.toInt).moveSouth()
          case "w" => games(gameId.toInt).moveWest()
        }
        
        // Update View From all game channels
      }
      case Array("msg", "id", gameId, msg) => outchannels(gameId.toInt).foreach(_ push (msg)); Thread.sleep(5000)

      case Array("msg", "all", msg) => outchannels.foreach(x => x._2.foreach(_.push(msg)))

      case _ => channel.push("Invalid Input!")
    }
  }

  def moveChannelToNewGroup(gameId: Int, channel: Concurrent.Channel[String]) {
    if (outchannels.get(gameId) == None) {
      outchannels += gameId -> new ListBuffer[play.api.libs.iteratee.Concurrent.Channel[String]]()
    }

    removeChannelFromCurrentGroup(channel)

    outchannels(gameId) += channel
    currentGameId += channel -> gameId
  }

  def removeChannelFromCurrentGroup(channel: Concurrent.Channel[String]) {
    val gameId = currentGameId.get(channel)
    if (gameId != None) {
      outchannels(gameId.get) -= channel
      if (outchannels(gameId.get).size == 0)
        games -= gameId.get
    }
  }
}