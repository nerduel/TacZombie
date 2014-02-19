package taczombie.test.model

import org.specs2.mutable.Specification
import taczombie.model.GameFactory
import taczombie.model.GameState


class GameFactorySpec extends Specification {

  val testfile = getClass().getResource("/TestLevel_correct")
  
  "GameFactory" should {
    "GameState must be InGame at begin" in {
      val game = GameFactory.newGame(false, file = testfile.getFile(), humans = 2, zombies = 4)
      game.gameState must be_==(GameState.InGame)     
    }
  }
}