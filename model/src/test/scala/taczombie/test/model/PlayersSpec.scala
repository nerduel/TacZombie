package taczombie.test.model

import org.specs2.mutable.Specification
import taczombie.model.Human
import taczombie.model.Players
import taczombie.model.Player

class PlayersSpec extends Specification {
  
  val players = TestObjects.players
  val newPlayer = new Human("qwerqaelrjblkjh", null)
  
  "Searching a non existent player" should {
    "return null" in {
      players.getPlayerByName("i'm not there") must be_==(null)
    }
  }
  
  "Adding a new Player" should {
    
    "add him at all" in {
      var morePlayers = players.updatedWithNewPlayer(newPlayer)
      
      morePlayers.getPlayerByName(newPlayer.name).name must be_==(newPlayer.name)
    }
    
    "put him in the last position" in {
      var morePlayers = players.updatedWithNewPlayer(newPlayer)
      
      for(i <- (0 until morePlayers.playerList.size-2))
        morePlayers = morePlayers.updatedRotatedPlayers
      
      morePlayers.nextPlayer.name must be_==(newPlayer.name)        
    }
  }	  
  
	"Updating an existing player" should {
    val ups = players.updatedExistingPlayer(players.currentPlayer.updatedMoved)
    
	  "not change his position" in {
    	ups.currentPlayer.name must be_==(players.currentPlayer.name)
	  }
	}
  
	"Rotating players" should {
	  sequential	  
	  var ups = players
    "cycle players evenly" in {
      var ups = players
      for(i <- (0 until ups.playerList.size))
        ups = ups.updatedRotatedPlayers
      ups.currentPlayer.name must be_==(players.currentPlayer.name)        
    } 
	}
	
	"Try adding an existing player as new" should {
	  "not change anything" in {
	    val ups = players.updatedWithNewPlayer(players.currentPlayer)
	    ups must be_==(players)
	  }	  
	}
	
	"Players object without any players" should {
	  val emptyPlayers = new Players()
	  
	  "should return null for nextPlayer" in {
	    emptyPlayers.nextPlayer must be_==(null)
	  }
	  
	  "should not change anything on trying to update a player" in {
	    val ups = emptyPlayers.updatedExistingPlayer(players.currentPlayer)
	    ups.equals(emptyPlayers)
	  }
	
  	"added with one player" should {
  	  "return same player for nextPlayer" in {
  	    val onePlayers = emptyPlayers.updatedWithNewPlayer(TestObjects.human)
  	    onePlayers.currentPlayer must be_==(onePlayers.nextPlayer)
  	  }
  	}
	}
}