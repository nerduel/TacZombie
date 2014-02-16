package taczombie.model

import scala.language.higherKinds

/**
 * obsolete!
 */
class PlayerTokens[+A <: PlayerToken](val tokenList : List[A]) {
  
	def currentToken : A = {
	  	tokenList.head
	}
  
 	def updatedWithNewToken[A <: PlayerToken](playerToken : A) : PlayerTokens[A] = {
			new PlayerTokens[A](tokenList.asInstanceOf[List[A]] ::: playerToken :: Nil)
 	  
	}
	
	def updatedWithExistingToken[A <: PlayerToken]
			(playerToken : A) : PlayerTokens[A] = {
	  
	  	val index = tokenList.indexOf( 
	  			tokenList.filter(t => t.id == playerToken.id).head)
			
			new PlayerTokens[A](tokenList.asInstanceOf[List[A]].updated(index, playerToken))
	}
	
	def updatedNextToken() : PlayerTokens[A] = {
	  new PlayerTokens[A](tokenList.tail ::: tokenList.head :: Nil)
	}
}