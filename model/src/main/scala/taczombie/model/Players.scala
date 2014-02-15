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
  
  def updatedRotatedPlayers() : Players = {
    var finalPlayers = this
    playerList.foreach(player => 
      finalPlayers = finalPlayers.updatedExistingPlayer(player.updatedResetMovesRemaining()))
    new Players(finalPlayers.playerList.tail ::: finalPlayers.playerList.head :: Nil)
  }
}