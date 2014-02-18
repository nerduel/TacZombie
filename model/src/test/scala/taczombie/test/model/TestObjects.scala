package taczombie.test.model

import taczombie.model.Coin
import taczombie.model.Wall
import taczombie.model.GameFieldCell
import taczombie.model.ZombieToken
import taczombie.model.GameObject
import taczombie.model.Powerup
import taczombie.model.HumanToken
import taczombie.model.GameFactory

object TestObjects {
  val coords = (0,0)
  val emptySetWithGameObjects = Set[GameObject]()
  
  val emptyGfc = new GameFieldCell(coords, emptySetWithGameObjects)

  val wall = new Wall(GameFactory.generateId, coords)
  val coin = new Coin(GameFactory.generateId, coords)
  val powerUp = new Powerup(GameFactory.generateId, coords)
  val livingHumanToken = new HumanToken(GameFactory.generateId, coords, dead = false)
  val deadHumanToken = new HumanToken(GameFactory.generateId, coords, dead = true)
  val poweredUpHumanToken = new HumanToken(GameFactory.generateId, coords, powerupTime = 10)
  
  val livingZombieToken = new ZombieToken(GameFactory.generateId, coords, dead = false)
  val livingZombieToken2 = new ZombieToken(GameFactory.generateId, coords, dead = false)
  val deadZombieToken = new ZombieToken(GameFactory.generateId, coords, dead = true)
}