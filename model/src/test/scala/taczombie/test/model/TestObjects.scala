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
  
  
  
   val coordsWall1 = (4,4); 						val coordsWall2 = (5,4);
  val coordsHumans = (4,5); val coordsHumansWithPowerup = (5,5);  val coordsCoin = (6,5)
	val coordsZombie = (4,6); 				  val coordsPowerup = (5,6);
  
  
  val emptySetWithGameObjects = Set[GameObject]()
  
  val emptyGfc = new GameFieldCell(coords, emptySetWithGameObjects)
  
  val humanGfc = new GameFieldCell(coordsHumans, emptySetWithGameObjects)

  val wall1 = new Wall(GameFactory.generateId, coordsWall1)
  val wall1GameFieldCellObjects = emptySetWithGameObjects + wall1
  val wall1GieldFieldCell = new GameFieldCell(coordsWall1, wall1GameFieldCellObjects)
  
  val wall2 = new Wall(GameFactory.generateId, coordsWall1)
  val wall2GameFieldCellObjects = emptySetWithGameObjects + wall2
  val wall2GieldFieldCell = new GameFieldCell(coordsWall1, wall2GameFieldCellObjects)  
  
  val coin = new Coin(GameFactory.generateId, coords)
  val coinGameFieldCellObjects = emptySetWithGameObjects + coin
  val coinGameFieldCell = new GameFieldCell(coordsCoin, coinGameFieldCellObjects)
  
    
  val powerUp = new Powerup(GameFactory.generateId, coords)
  val powerUpGameFieldCellObjects = emptySetWithGameObjects + powerUp
  val powerUpGameFieldCell = new GameFieldCell(coordsPowerup, powerUpGameFieldCellObjects)
  
  val livingHumanToken = new HumanToken(GameFactory.generateId, coordsHumans, dead = false)
  val livingHumanToken2 = new HumanToken(GameFactory.generateId, coordsHumans, dead = false)
  val deadHumanToken = new HumanToken(GameFactory.generateId, coordsHumans, dead = true)  
  val poweredUpHumanToken = new HumanToken(GameFactory.generateId, coordsHumans, powerupTime = 10)
  
  val humanGameFielcCellObjects = emptySetWithGameObjects + livingHumanToken + livingHumanToken2 + deadHumanToken
  val humanGameFieldCell = new GameFieldCell(coordsHumans, humanGameFielcCellObjects)
  
  val humanWithPowerupGameFielcCellObjects = emptySetWithGameObjects + poweredUpHumanToken
  val humanWithPowerupGameFieldCell = new GameFieldCell(coordsHumansWithPowerup, humanWithPowerupGameFielcCellObjects)
  
  val human = Human("Human "+GameFactory.generateId, List[Int](poweredUpHumanToken.id, livingHumanToken.id, deadHumanToken.id,livingHumanToken2.id))  
  
  val livingZombieToken = new ZombieToken(GameFactory.generateId, coordsZombie, dead = false)
  val livingZombieToken2 = new ZombieToken(GameFactory.generateId, coordsZombie, dead = false)
  val deadZombieToken = new ZombieToken(GameFactory.generateId, coordsZombie, dead = true)
  
  val zombieGameFielcCellObjects = emptySetWithGameObjects + livingZombieToken + livingZombieToken2 + deadZombieToken 
  val zombieGameFieldCell = new GameFieldCell(coordsZombie, zombieGameFielcCellObjects)
  
  val zombie = Zombie("Zombie "+GameFactory.generateId, List[Int](livingZombieToken.id, livingZombieToken2.id, deadZombieToken.id))
  
  val gameFieldCells = Map[(Int,Int),GameFieldCell]((coordsWall1, wall1GieldFieldCell),
      																							(coordsWall2, wall2GieldFieldCell),
      																							(coordsPowerup, powerUpGameFieldCell),
      																							(coordsCoin, coinGameFieldCell),
      																							(coordsHumans, humanGameFieldCell),
      																							(coordsHumansWithPowerup, humanWithPowerupGameFieldCell),
      																							(coordsZombie, zombieGameFieldCell))
    
  
  val gameField = new GameField(""+GameFactory.generateId, gameFieldCells, 10, 10, coordsHumans, coordsZombie, 0)  
  
  val players = new Players(List[Player](human, zombie))
}