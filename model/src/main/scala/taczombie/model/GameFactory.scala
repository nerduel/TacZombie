package taczombie.model

import scala.Array.canBuildFrom
import scala.collection.immutable.HashSet
import scala.io.Source
import scala.collection.mutable.HashMap
import taczombie.model.util.LevelCreator
import scala.concurrent.Lock

object GameFactory {
  var counter : Int = 1
  
  val generateIdLock : Lock = new Lock()
  val readFileLock : Lock = new Lock()
  
  def generateId : Int = {
    generateIdLock.acquire
  	counter = counter + 1
  	val newId = counter
    generateIdLock.release
    newId
  }
  
  val defaultFile = getClass().getResource("/TestLevel_correct")
    
  def newGame(random : Boolean = false, file : String = defaultFile.toString(), 
      humans: Int = defaults.defaultHumans, zombies: Int= defaults.defaultZombies) : Game = {
       
    val (gameField, playerMap) = {
      if(random == false)
        	createGameFieldAndPlayerMap(humans, zombies, file)
      else {
        	val level = (new LevelCreator()).create(
        	    defaults.defaultHeight, defaults.defaultWidth, humans)
        	
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
        
    if(gameField == null || playerMap == null) 
      return null
    else     
    	new Game(generateId, gameField, playerMap, GameState.InGame)
  }
    
  private def createGameFieldAndPlayerMap(
      							humanTokenCount : Int, zombieTokenCount : Int,
                    file : String = null,
                    level : Array[String] = null,
                    name : String = null)
        
        : (GameField, Players)  = {
    
    readFileLock.acquire
    val (mapStringArray, gameName) = {
      if(file != null) {
        (Source.fromFile(file).getLines.toArray, file.split("/").last)
      }
      else
        (level, name)
    }
    readFileLock.release
    
    if((mapStringArray.foldLeft(Set[Int]()){(set, line) => 
      	set + line.length
      }.size) > 1) {
      println("level has different line lengths")
      return (null, null)
    }
      
    
    val levelWidth = (mapStringArray apply 0).size
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
              val humanToken = new HumanToken(this.generateId, (tuple))
              humanTokenIds.+=(humanToken.id)
              tmpGameFieldCell = tmpGameFieldCell.addHere(humanToken)
          case 'Z' =>
              zombieBase = (tuple)
              for(i <- 0 until zombieTokenCount) {
                val zombieToken =  new ZombieToken(this.generateId, (tuple))
                zombieTokenIds.+=(zombieToken.id)                
                tmpGameFieldCell = tmpGameFieldCell.addHere(zombieToken)
              }
          case c : Char => {
            println("unkown char: " + c.toByte)
            return (null, null)
          }
        }
        if(validCharacterRead) {
           gameFieldCells.+=((tuple,tmpGameFieldCell))
        }
      }
    }
    
    // check if we enough human bases for humans
    val missingHumans = humanTokenCount-humanTokenIds.size
    for(i <- (0 until missingHumans)) {
      val humanToken = new HumanToken(this.generateId, (humanBase))
      humanTokenIds.+=(humanToken.id)
      gameFieldCells.update(humanBase, gameFieldCells.apply(humanBase)
          																					 .addHere(humanToken))
    }
    
    // Create the player map with a human and a zombie player with tokens
    // TODO: make this scalable for more players
    var players : Players = new Players()
    
    val humanId = defaults.defaultHumanName + this.generateId
    players = players.updatedWithNewPlayer(new Human(humanId, humanTokenIds.toList))    
    val zombieId = defaults.defaultZombieName + this.generateId
    players = players.updatedWithNewPlayer(new Zombie(zombieId, zombieTokenIds.toList))
    
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