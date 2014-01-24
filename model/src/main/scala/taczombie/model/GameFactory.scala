package taczombie.model

import scala.Array.canBuildFrom
import scala.collection.immutable.HashSet
import scala.collection.immutable.TreeMap
import scala.io.Source

object GameFactory {
  var counter : Int = 1
  def generateId : Int = {
    counter = counter + 1
    counter
  }
  
  val defaultFile =  
    "/home/junky/synchronized/TacZombie/model/src/test/scala/taczombie/test/model/TestLevel_correct"
    
  val defaultHumans = 1
  val defaultZombies = 1
    
  def newGame(random : Boolean = false, file : String = defaultFile, 
      humans: Int = defaultHumans, zombies: Int= defaultZombies) : Game = {
    
    val (gameField, playerMap) = {
//      if(random == false)
        	createGameFieldAndPlayerMap(humans, zombies, defaultFile)
//      else {
//        	var (level, name) = fromRandom()
//        	createGameFieldAndPlayerMap(humans, zombies, level, name)
//        	
    }
    
    new Game(generateId, gameField, playerMap, GameState.InGame)
  }


  val defaultHumanName = "Pacman"
  val defaultZombieName = "Zombie"
    
  def createGameFieldAndPlayerMap(humanTokenCount : Int, zombieTokenCount : Int,
                    file : String = null,
                    level : Array[String] = null,
                    name : String = null)
        
        : (GameField, Players)  = {
    
    val (mapStringArray, gameName) = {
      if(file != null)
        (Source.fromFile(file).getLines.toArray, file.split("/").last)
      else
        (level, name)
    }
        
    lazy val affectedLineSizes = {
      for (line <- mapStringArray) yield line.length()
    }.toList.distinct
    
    def isGameFieldRectangular = affectedLineSizes.size == 1
    
    if (! isGameFieldRectangular) {
      throw new Exception("Level field is not Rectangular")
    }
    
    val levelWidth = affectedLineSizes apply 0
    val levelHeight = mapStringArray.size
    
    var humanBase : (Int,Int) = (0,0)
    var zombieBase : (Int,Int) = (0,0)
    
    var coinsPlaced : Int = 0
    
    // collect tokens
    var humanTokens = TreeMap[Int,HumanToken]()
    var zombieTokens = TreeMap[Int,ZombieToken]()
    
    var gameFieldCells = Map[(Int,Int), GameFieldCell]()
    
    for (row <- 0 until levelHeight) yield {
      for (col <- 0 until levelWidth) yield {
        val tuple = (row,col)
        var tmpGameFieldCell = new GameFieldCell((tuple), HashSet[GameObject]())
        var validCharacterRead = true
        mapStringArray(row)(col) match {
          case '#' =>
              tmpGameFieldCell = tmpGameFieldCell.addHere(new Wall(0,(tuple)))
          case '.' =>
              tmpGameFieldCell = tmpGameFieldCell.addHere(new Coin(this.generateId,(tuple)))
              coinsPlaced += 1
          case ';' =>
              tmpGameFieldCell = tmpGameFieldCell.addHere(new Powerup(this.generateId, (tuple)))
          case 'H' =>
              humanBase = (tuple)
              for(i <- 0 until humanTokenCount) {
                val humanToken = new HumanToken(this.generateId, (tuple))
                humanTokens = humanTokens.updated(humanToken.id, humanToken)
                tmpGameFieldCell = tmpGameFieldCell.addHere(humanToken)
              }              
          case 'Z' =>
              zombieBase = (tuple)
              for(i <- 0 until zombieTokenCount) {
                val zombieToken =  new ZombieToken(this.generateId, (tuple))
                zombieTokens = zombieTokens.updated(zombieToken.id, zombieToken)                
                tmpGameFieldCell = tmpGameFieldCell.addHere(zombieToken)
              }
          case c : Char => {
            println("unkown char: " + c.toByte)
            validCharacterRead = false
          }
        }
        if(validCharacterRead) {
           gameFieldCells = gameFieldCells.+((tuple,tmpGameFieldCell))
        }
      }
    }
    
    // Create the player map with a human and a zombie player with tokens
    // TODO: make this scalable for more players
    var players : Players = new Players(List[Player]())
    val humanId = defaultHumanName + this.generateId
    players = players.updatedWithNewPlayer(new Human(humanId, humanTokens))
    val zombieId = defaultZombieName + this.generateId
    players = players.updatedWithNewPlayer(new Zombie(zombieId, zombieTokens))
    
    val gameField = new GameField(gameName,
                gameFieldCells,
                levelWidth,
                levelHeight,
                humanBase,
                zombieBase,
                coinsPlaced)
    
    (gameField, players)
  }
}