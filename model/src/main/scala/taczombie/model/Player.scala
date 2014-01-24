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
  def updatedCycledTokens() : Player  
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
      

  def updated(playerTokens : TreeMap[Int,HumanToken] = this.playerTokens,
    				 movesRemainingAdded : Int = 0,
    				 lifesAdded : Int = 0) : Human = {
    new Human(this.name, playerTokens, this.movesRemaining+movesRemainingAdded, 
        			this.lifes + lifesAdded)
  }
    
  def updatedToken(changedToken : HumanToken) : Human =
    updated(this.playerTokens.updated(changedToken.id, changedToken))
    
  def updatedMoved() : Human = updated(movesRemainingAdded = -1)
  
  def updatedCycledTokens() : Human = {
    if(playerTokens.size > 1)
    	updated(playerTokens.tail.+(playerTokens.head))
    else this
  }
}

case class Zombie(val name : String,
    						  playerTokens : TreeMap[Int,ZombieToken],
									val movesRemaining : Int = 3)
    extends Player {
  def currentToken : ZombieToken = playerTokens.head._2
  val coinsCollected = 0
  val score = 0
  
  def updated(playerTokens : TreeMap[Int,ZombieToken] = this.playerTokens,
    				 movesRemainingAdded : Int = 0) : Zombie = {
    new Zombie(this.name, playerTokens, this.movesRemaining+movesRemainingAdded)
  }
  
  def updatedToken(changedToken : ZombieToken) : Zombie =
    updated(this.playerTokens.updated(changedToken.id, changedToken))
  
  def updatedMoved() : Zombie = updated(movesRemainingAdded = -1)  
    
  def updatedCycledTokens() : Zombie = {
    if(playerTokens.size > 1)
    	updated(playerTokens.tail.+(playerTokens.head))
    else this
  }
}
