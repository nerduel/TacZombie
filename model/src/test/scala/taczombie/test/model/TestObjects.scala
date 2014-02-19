package taczombie.test.model

import taczombie.model.Coin
import taczombie.model.Wall
import taczombie.model.GameFieldCell
import taczombie.model.ZombieToken
import taczombie.model.GameObject
import taczombie.model.Powerup
import taczombie.model.HumanToken
import taczombie.model.GameFactory
import taczombie.model.Players
import taczombie.model.Human
import taczombie.model.Player
import taczombie.model.Zombie
import taczombie.model.GameField

object TestObjects {
  val coords = (0,0)
  
  val coordsHumans = (1,1)
  val coordsZombie = (1,2)
  
  val emptySetWithGameObjects = Set[GameObject]()
  
  val emptyGfc = new GameFieldCell(coords, emptySetWithGameObjects)
  
  val humanGfc = new GameFieldCell(coordsHumans, emptySetWithGameObjects)

  val wall = new Wall(GameFactory.generateId, coords)
  val coin = new Coin(GameFactory.generateId, coords)
  val powerUp = new Powerup(GameFactory.generateId, coords)
  
  val livingHumanToken = new HumanToken(GameFactory.generateId, coordsHumans, dead = false)
  val livingHumanToken2 = new HumanToken(GameFactory.generateId, coordsHumans, dead = false)
  val deadHumanToken = new HumanToken(GameFactory.generateId, coordsHumans, dead = true)
  val poweredUpHumanToken = new HumanToken(GameFactory.generateId, coordsHumans, powerupTime = 10)
  
  val humanGameFielcCellObjects = emptySetWithGameObjects + livingHumanToken + livingHumanToken2 + deadHumanToken
  val humanGameFieldCell = new GameFieldCell(coordsHumans, humanGameFielcCellObjects)
  
  val human = Human("Human "+GameFactory.generateId, List[Int](livingHumanToken.id, livingHumanToken2.id))
  val humanWithDeadToken = Human("Human "+GameFactory.generateId, List[Int](livingHumanToken.id, deadHumanToken.id))  
  
  val livingZombieToken = new ZombieToken(GameFactory.generateId, coordsZombie, dead = false)
  val livingZombieToken2 = new ZombieToken(GameFactory.generateId, coordsZombie, dead = false)
  val deadZombieToken = new ZombieToken(GameFactory.generateId, coordsZombie, dead = true)
  
  val zombieGameFielcCellObjects = emptySetWithGameObjects + livingZombieToken + livingZombieToken2 + deadZombieToken 
  val zombieGameFieldCell = new GameFieldCell(coordsZombie, zombieGameFielcCellObjects)
  
  val zombie = Zombie("Zombie "+GameFactory.generateId, List[Int](livingZombieToken.id, livingZombieToken2.id))
  val zombieWithDeadToken = Zombie("Zombie "+GameFactory.generateId, List[Int](livingZombieToken.id, deadZombieToken.id))
  
  val gameFieldCells = Map[(Int,Int),GameFieldCell]((coordsHumans, humanGameFieldCell),
      																							(coordsZombie, zombieGameFieldCell))
    
  
  val gameField = new GameField(""+GameFactory.generateId, gameFieldCells, 10, 10, coordsHumans, coordsZombie, 0)  
  
  val players = new Players(List[Player](human, zombie))
}