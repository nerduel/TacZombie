package taczombie.model

import scala.collection.immutable.TreeMap

private object defaults {
  val humanMoves = 20
  val lifes = 3
  val zombieMoves = 1
}

trait Player {
  
  val defaultHumanMoves = 10
  
  val name : String
  val playerTokens : PlayerTokens[PlayerToken]
  val movesRemaining : Int
  def currentToken : PlayerToken
  def coinsCollected : Int
  def score : Int
  def updatedMoved() : Player
  def updatedCycledTokens() : Player
  def updatedResetMovesRemaining() : Player
}

case class Human(val name : String,
    						 val playerTokens : PlayerTokens[HumanToken],
    						 val movesRemaining : Int = defaults.humanMoves,
    						 val lifes : Int = defaults.lifes)
    extends Player  {
  
  def currentToken : HumanToken = playerTokens.currentToken
  def coinsCollected = playerTokens.tokenList.foldLeft(0)((result,token) => 
    result + token.coins)
  def score = playerTokens.tokenList.foldLeft(0)({(result,token) => 
    result + token.score})
      

  def updated(playerTokens : PlayerTokens[HumanToken] = this.playerTokens,
    				 movesRemainingAdded : Int = 0,
    				 lifesAdded : Int = 0) : Human = {
    new Human(this.name, playerTokens, this.movesRemaining+movesRemainingAdded, 
        			this.lifes + lifesAdded)
  }
    
  def updatedToken(changedToken : HumanToken) : Human =
    updated(this.playerTokens.updatedWithExistingToken(changedToken))
    
  def updatedMoved() : Human = updated(movesRemainingAdded = -1)
  
  def updatedCycledTokens() : Human = {
    if(playerTokens.tokenList.size > 1)
    	updated(this.playerTokens.updatedNextToken)
    else this
  }
  
  def updatedResetMovesRemaining() : Human = {
    new Human(this.name, this.playerTokens, lifes = this.lifes)
  }
}

case class Zombie(val name : String,
    						  val playerTokens : PlayerTokens[ZombieToken],
									val movesRemaining : Int = defaults.zombieMoves)
    extends Player {
  def currentToken : ZombieToken = playerTokens.currentToken
  val coinsCollected = 0
  val score = 0
  
  def updated(playerTokens : PlayerTokens[ZombieToken] = this.playerTokens,
    				 movesRemainingAdded : Int = 0) : Zombie = {
    new Zombie(this.name, playerTokens, this.movesRemaining+movesRemainingAdded)
  }
  
  def updatedToken(changedToken : ZombieToken) : Zombie =
    updated(this.playerTokens.updatedWithExistingToken(changedToken))
  
  def updatedMoved() : Zombie = updated(movesRemainingAdded = -1)  
    
  def updatedCycledTokens() : Zombie = {
    if(playerTokens.tokenList.size > 1)
    	updated(playerTokens.updatedNextToken)
    else this
  }
  
  def updatedResetMovesRemaining() : Zombie = {
    new Zombie(this.name, this.playerTokens)
  }  
}
