package taczombie.model

import scala.Array.canBuildFrom
import scala.collection.immutable.HashSet
import scala.io.Source
import scala.collection.mutable.HashMap
import taczombie.model.util.LevelCreator

object GameFactory {
  var counter : Int = 1
  def generateId : Int = {
    counter = counter + 1
    counter
  }
  
  val levelCreator = new LevelCreator()
  
  val defaultFile =  
    "src/test/scala/taczombie/test/model/TestLevel_correct"
    
  val defaultHumans = 2
  val defaultZombies = 4
  val defaultHeight = 21
  val defaultWidth = 21
    
  def newGame(random : Boolean = false, file : String = defaultFile, 
      humans: Int = defaultHumans, zombies: Int= defaultZombies) : Game = {
    
    val (gameField, playerMap) = {
      if(random == false)
        	createGameFieldAndPlayerMap(humans, zombies, file)
      else {
        	val level = levelCreator.create(defaultHeight, defaultWidth)
        	
        	val array = scala.collection.mutable.ArrayBuffer[String]()
        	
        	for(line <- level) {
        	  val lineConcatString = line.foldLeft("")((result, element) =>
        	    result concat element)
        	  array += lineConcatString
        	}
        	for(s <- array)
        	  println(s)
        	
        	createGameFieldAndPlayerMap(
        	    humans, zombies, 
        	    level = array.toArray[String], name = array.hashCode().toString)
      }
    }
    new Game(generateId, gameField, playerMap, GameState.InGame)
  }
    
  def createGameFieldAndPlayerMap(
      							humanTokenCount : Int, zombieTokenCount : Int,
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
    val humanTokenIds = scala.collection.mutable.ListBuffer[Int]()
    val zombieTokenIds = scala.collection.mutable.ListBuffer[Int]()
    
    val gameFieldCells = scala.collection.mutable.HashMap[(Int,Int), GameFieldCell]()
    
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
                humanTokenIds.+=(humanToken.id)
                tmpGameFieldCell = tmpGameFieldCell.addHere(humanToken)
              }              
          case 'Z' =>
              zombieBase = (tuple)
              for(i <- 0 until zombieTokenCount) {
                val zombieToken =  new ZombieToken(this.generateId, (tuple))
                zombieTokenIds.+=(zombieToken.id)                
                tmpGameFieldCell = tmpGameFieldCell.addHere(zombieToken)
              }
          case c : Char => {
            println("unkown char: " + c.toByte)
            validCharacterRead = false
          }
        }
        if(validCharacterRead) {
           gameFieldCells.+=((tuple,tmpGameFieldCell))
        }
      }
    }
    
    // Create the player map with a human and a zombie player with tokens
    // TODO: make this scalable for more players
    var players : Players = new Players(List[Player]())
    val zombieId = defaults.zombieName + this.generateId
    players = players.updatedWithNewPlayer(new Zombie(zombieId, zombieTokenIds.toList))
    val humanId = defaults.humanName + this.generateId
    players = players.updatedWithNewPlayer(new Human(humanId, humanTokenIds.toList))
    
    val gameField = new GameField(gameName,
                gameFieldCells.toMap,
                levelWidth,
                levelHeight,
                humanBase,
                zombieBase,
                coinsPlaced)
    
    (gameField, players)
  }
}