package taczombie.model

import scala.collection.immutable.TreeMap

trait Player {
	val name : String
  val playerTokenIds : List[Int]  
  val movesRemaining : Int
	
	def currentTokenId = playerTokenIds.head
	
  def coins(gameField : GameField) : Int = 0
  def score(gameField : GameField) : Int = 0
  def lifes : Int
  
  def currentToken(gameField : GameField) : PlayerToken
  
  def deadTokens(gameField : GameField) : List[PlayerToken] = {
	  gameField.findPlayerTokensById(playerTokenIds)
	  				 .filter(token => token.dead)
	}
  
  def deadTokenCount(gameField : GameField) : Int = {
	  deadTokens(gameField).size
	}
	
	def totalTokens = playerTokenIds.size
  
  def updated(updatedPlayerTokenIds : List[Int] = this.playerTokenIds,
    				 updatedMovesRemaining : Int = this.movesRemaining,
    				 newLifes : Int = lifes) : Player				 
    				 
  def updatedMoved() : Player = updated(updatedMovesRemaining = this.movesRemaining-1)
  def updatedCycledTokens() : Player = {
    if(playerTokenIds.size > 1)
    	updated(playerTokenIds.tail ::: playerTokenIds.head :: Nil)
    else this
	}
	
	def updatedDecreasedLifes() : Player = updated(newLifes = lifes-1)
	
  def updatedResetMovesRemaining() : Player
}

case class Human(val name : String,
    						 val playerTokenIds : List[Int],
    						 val movesRemaining : Int = defaults.humanMoves,
    						 val lifes : Int = defaults.humanLifes)
    extends Player  {
  
  override def currentToken(gameField : GameField) : HumanToken = {
  	gameField.findOnePlayerTokenById(currentTokenId) match {
  	  case humanToken : HumanToken => humanToken
  	  case _ => null
  	}
  } 
  
  override def coins(gameField : GameField) = 
    gameField.findPlayerTokensById(playerTokenIds).foldLeft(0)((result,token) => 
    result + token.coins)
    
  override def score(gameField : GameField) = 
    gameField.findPlayerTokensById(playerTokenIds).foldLeft(0)({(result,token) => 
    result + token.score})
    
  override def updated(newPlayerTokenIds : List[Int],
    				 newMovesRemaining : Int,
    				 newLifes : Int) : Human = {
    new Human(this.name, newPlayerTokenIds, newMovesRemaining, 
        			newLifes)
  }
  
  def updated(newPlayerTokenIds : List[Int],
    				 newMovesRemaining : Int) : Human = {
  	updated(newPlayerTokenIds, newMovesRemaining,
  	    this.lifes)
  }
  
  def updatedResetMovesRemaining() : Human = {
    updated(this.playerTokenIds, defaults.humanMoves, this.lifes)
  }  
}

case class Zombie(val name : String,
    						  val playerTokenIds : List[Int],
									val movesRemaining : Int = defaults.zombieMoves)
    extends Player {
  
  val lifes = 0
  
  override def currentToken(gameField : GameField) : ZombieToken = {
  	gameField.findOnePlayerTokenById(currentTokenId) match {
  	  case zombieToken : ZombieToken => zombieToken
  	  case _ => null
  	}
  }  
  
  def updated(newPlayerTokenIds : List[Int] = this.playerTokenIds,
    				 newMovesRemaining : Int,
    				 newLifes : Int = lifes) : Zombie = {
    new Zombie(this.name, newPlayerTokenIds, newMovesRemaining)
  }
   
  def updatedResetMovesRemaining() : Zombie = {
    new Zombie(this.name, this.playerTokenIds)
  }  
}
