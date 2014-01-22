package taczombie.model.util

import scala.Array.canBuildFrom
import scala.collection.mutable.Map
import scala.io.Source

import taczombie.model.Coin
import taczombie.model.GameObject
import taczombie.model.HumanToken
import taczombie.model.Powerup
import taczombie.model.Wall
import taczombie.model.ZombieToken

case class Configuration(map: Map[(Int,Int),GameObject] = Map[(Int,Int),GameObject](),
	        			 levelWidth : Int,
	        			 levelHeight : Int,
	        			 humanBase : (Int, Int),
	        			 zombieBase : (Int,Int),
	        			 coinAmount : Int,
	        			 gameName : String)
	        			 
object FileLoader {
	def mapName(fileName : String) : Configuration = {
		val mapStringArray = Source.fromFile(fileName).getLines.toArray
		
		lazy val affectedLineSizes = { 
			for (line <- mapStringArray) yield line.length()
		}.toList.distinct
		
		def isGameFieldRectangular = affectedLineSizes.size == 1
		
		if (! isGameFieldRectangular) {
			throw new Exception("Level fiel is not Rectangular")
		}
		
		val width = affectedLineSizes apply 0
		val height = mapStringArray.size
		
    var humanBase : (Int,Int) = (0,0)
    var zombieBase : (Int,Int) = (0,0)
    
    var coinsPlaced = 0
    
    val map = Map[(Int,Int),GameObject]()
	    
		for (row <- 0 until height) yield {
		  for (col <- 0 until width) yield {
			  val tuple = (row,col)
				mapStringArray(row)(col) match {
					case '#' => 
					    map.+= ((tuple, new Wall(0, tuple)))
					case '.' => 
					    map.+=((tuple, new Coin(GameObjectFactory.generateId, tuple)))
					    coinsPlaced += 1
					case ';' => 
					    map.+=((tuple, new Powerup(GameObjectFactory.generateId, tuple)))
					case 'H' => 
					    humanBase = (tuple)
				    	map.+=((((tuple), new HumanToken(GameObjectFactory.generateId, tuple))))
					case 'Z' => 
					    zombieBase = (tuple)
					    map.+=((((tuple), new ZombieToken(GameObjectFactory.generateId, tuple))))
				}
			} 
		}
	    
		Configuration(map,
		        	  levelWidth = width,
		        	  levelHeight = height,
		        	  humanBase = humanBase,
		        	  zombieBase = zombieBase,
		        	  coinAmount = coinsPlaced,
		        	  gameName = fileName.split("/").last
		)
	}
}