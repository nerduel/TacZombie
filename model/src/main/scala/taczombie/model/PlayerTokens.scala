package taczombie.model

class PlayerTokens[+TokenType](val tokenList : List[TokenType] = List[TokenType]()) {
   
	def currentToken : TokenType = {
	  	tokenList.head
	}
	
	def updatedWithNewToken[SpecialTokenType >: TokenType](playerToken : SpecialTokenType) : PlayerTokens[SpecialTokenType] = {
    if(!tokenList.contains(playerToken))
      new PlayerTokens[SpecialTokenType](tokenList.+:(playerToken))
    else this
  }
	
	def updatedWithExistingToken[SpecialTokenType >: TokenType]
			(playerToken : SpecialTokenType) : PlayerTokens[SpecialTokenType] = {
	  if(tokenList.contains(playerToken)) {
	    val index = tokenList.indexOf(playerToken)
	    new PlayerTokens[SpecialTokenType](tokenList.updated(index, playerToken))
	  }
	  else this
	}
	
	def updatedNextToken() : PlayerTokens[TokenType] = {
	  new PlayerTokens[TokenType](tokenList.tail ::: tokenList.head :: Nil)
	}
}