package taczombie.model

class Players(val playerList : List[Player]) {
    
  def currentPlayer() : Player = playerList.head
  
  def getPlayerByName(name : String) : Player = 
    playerList.filter(player => player.name.equals(name)).head
  
  def nextPlayer() : Player = { 
    if(playerList.size == 0) null // TODO exception
    if(playerList.size == 1) playerList.head
    else playerList.apply(2)
  }
  
  def updatedWithNewPlayer(player : Player) : Players = {
    if(!playerList.contains(player))
      new Players(playerList.+:(player))
    else this
  }
  
  def updatedExistingPlayer(player : Player) : Players = {
    if(playerList.nonEmpty) {
      val index = playerList.indexOf(getPlayerByName(player.name))
      new Players(playerList.updated(index, player))
    } 
    else null // TODO exception empty list
  }
  
  def updatedFromUpdatedGameFieldCell(gameFieldCell : GameFieldCell) 
  	: Players = {
    var updatedPlayers : Players = this
    playerList.foreach(player => {
      player.playerTokens.foreach(playerToken => {
        gameFieldCell.gameObjects.foreach(gameObject =>{
      	  (player, playerToken._2, gameObject) match {
      	    case(human : Human, playerHumanToken : HumanToken, 
      	        	cellHumanToken : HumanToken) =>
      	      if(playerHumanToken.id == cellHumanToken.id) {
      	        println("found same token")
      	        updatedPlayers = updatedPlayers.updatedExistingPlayer(
      	            human.updatedToken(cellHumanToken))
      	      }      	        
      	        
      	    case(zombie : Zombie, playerZombieToken : ZombieToken, 
      	        	cellZombieToken : ZombieToken) =>
      	      if(playerZombieToken.id == cellZombieToken.id) {
      	        println("found same token")
      	        updatedPlayers = updatedPlayers.updatedExistingPlayer(
      	            zombie.updatedToken(cellZombieToken))
      	      }
      	    case _ =>
      	  	} 
      	})
      })
    })
    updatedPlayers
  }
  
  def updatedRotatedPlayers() : Players = {
    new Players(playerList.tail ::: playerList.head :: Nil)
  }
  
//  def udpatedRotatedTokens(player : Player) {
//    val newPlayer = player.playerTokens
//  }
}