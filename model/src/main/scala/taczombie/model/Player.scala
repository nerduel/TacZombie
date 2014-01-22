package taczombie.model

import taczombie.model._
import scala.collection.immutable.TreeMap

trait Player {
  val name : String
  val playerTokens : TreeMap[Int,PlayerToken]
  val movesRemaining : Int
  val tokenlistRotatesRemaining : Int = playerTokens.size
  def currentToken : PlayerToken
  def coinsCollected : Int
  def score : Int
}

case class Human(val name : String,
    						 playerTokens : TreeMap[Int,HumanToken],
    						 val movesRemaining : Int = 5,
    						 val lifes : Int = 3)
    extends Player {
  def currentToken : HumanToken = playerTokens.head._2
  def coinsCollected = playerTokens.foldLeft(0)((result,token) => 
    result + token._2.coins)
  def score = playerTokens.foldLeft(0)((result,token) => 
    result + token._2.score)    
}

case class Zombie(val name : String,
    						  playerTokens : TreeMap[Int,ZombieToken],
									val movesRemaining : Int = 3)
    extends Player {
  def currentToken : ZombieToken = playerTokens.head._2
  val coinsCollected = 0
  val score = 0
}