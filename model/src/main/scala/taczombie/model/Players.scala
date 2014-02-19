package taczombie.model



class Players(val playerList : List[Player] = List[Player]()) {
  
  def currentPlayer() : Player = playerList.head
  
  def getPlayerByName(name : String) : Player = {
    val filteredPlayers = playerList.filter(player => player.name.equals(name))
    if(filteredPlayers.size > 0)
      filteredPlayers.head
    else null
  }
  
  def nextPlayer() : Player = { 
    if(playerList.size == 0) return null // TODO exception
    else if(playerList.size == 1) return playerList.head
    else playerList.tail.head
  }
  
  def updatedWithNewPlayer(player : Player) : Players = {
    if(!playerList.contains(player))
      new Players(playerList ::: player :: Nil)
    else this
  }
  
  def updatedExistingPlayer(player : Player) : Players = {
    if(playerList.nonEmpty) {
      val index = playerList.indexOf(getPlayerByName(player.name))
      new Players(playerList.updated(index, player))
    } 
    else this
  }
  
  def updatedRotatedPlayers() : Players = {
    var finalPlayers = this
    playerList.foreach(player => 
      finalPlayers = finalPlayers.updatedExistingPlayer(player.updatedResetMovesRemaining()))
    new Players(finalPlayers.playerList.tail ::: finalPlayers.playerList.head :: Nil)
  }
}