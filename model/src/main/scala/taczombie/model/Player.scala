package taczombie.model

import scala.collection.immutable.TreeMap

trait Player {
  val name : String
  val playerTokens : TreeMap[Int,PlayerToken]
  val movesRemaining : Int
  val tokenlistRotatesRemaining : Int = playerTokens.size
  def currentToken : PlayerToken
  def coinsCollected : Int
  def score : Int
  
  def updatedMoved() : Player
}

case class Human(val name : String,
    						 playerTokens : TreeMap[Int,HumanToken],
    						 val movesRemaining : Int = 5,
    						 val lifes : Int = 3)
    extends Player {
  def currentToken : HumanToken = playerTokens.head._2
  def coinsCollected = playerTokens.foldLeft(0)((result,token) => 
    result + token._2.coins)
  def score = playerTokens.foldLeft(0)({(result,token) => 
    result + token._2.score})
    
  def updatedMoved() : Human = update(movesRemainingAdded = -1)
  def update(playerTokens : TreeMap[Int,HumanToken] = this.playerTokens,
    				 movesRemainingAdded : Int = this.movesRemaining,
    				 lifesAdded : Int = this.lifes) : Human = {
    new Human(this.name, playerTokens, this.movesRemaining+1, 
        			this.lifes + lifesAdded)
  }
}

case class Zombie(val name : String,
    						  playerTokens : TreeMap[Int,ZombieToken],
									val movesRemaining : Int = 3)
    extends Player {
  def currentToken : ZombieToken = playerTokens.head._2
  val coinsCollected = 0
  val score = 0
  
  def updatedMoved() : Zombie = update(movesRemainingAdded = -1)
  def update(playerTokens : TreeMap[Int,ZombieToken] = this.playerTokens,
    				 movesRemainingAdded : Int = this.movesRemaining) : Zombie = {
    new Zombie(this.name, playerTokens, this.movesRemaining+1)
  }
}