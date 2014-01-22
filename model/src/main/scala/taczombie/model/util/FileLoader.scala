package taczombie.model.util

import taczombie.model._
import scala.io.Source

import scala.collection.mutable.Map

case class Configuration(map: Map[(Int,Int),GameObject] = Map[(Int,Int),GameObject](),
	        			 levelWidth : Int = 0,
	        			 levelHeight : Int = 0,
	        			 humanBase : (Int, Int) = (0,0),
	        			 zombieBase : (Int,Int) = (0,0),
	        			 coinAmount : Int = 0,
	        			 gameName : String = "")
	        			 
object FileLoader {
	def load(fileName : String) : Configuration = {
		val lvl = Source.fromFile(fileName).getLines.toArray
		
		lazy val affectedLineSizes = { 
			for (line <- lvl) yield line.length()
		}.toList.distinct
		
		def isGameFieldRectangular = affectedLineSizes.size == 1
		
		if (! isGameFieldRectangular) {
			throw new Exception("Level fiel is not Rectangular")
		}
		
		val width = affectedLineSizes apply 0
		val height = lvl.size
		
    var humanBase : (Int,Int) = (0,0)
    var zombieBase : (Int,Int) = (0,0)
    
    var coinAmount = 0
    
    val map = Map[(Int,Int),GameObject]()
	    
		for (row <- 0 until height) {
			for (col <- 0 until width) {
			  val tuple = (row,col)
				lvl(row)(col) match {
					case '#' => 
					    map.+= ((tuple, new Wall(0, tuple)))
					case '.' => 
					    map.+=((tuple, new Coin(GameObjectFactory.generateId, tuple)))
					    coinAmount += 1
					case ';' => 
					    map.+=((tuple, new Powerup(GameObjectFactory.generateId,tuple)))
					case 'H' => 
					    humanBase = (tuple)
				    	map.+=(((tuple), new HumanToken(GameObjectFactory.generateId,tuple)))
					case 'Z' => 
					    zombieBase = (tuple)
					    map.+=(((tuple), new ZombieToken(GameObjectFactory.generateId,tuple)))
				}
			} 
		}
	    
		Configuration(map,
		        	  levelWidth = width,
		        	  levelHeight = height,
		        	  humanBase = humanBase,
		        	  zombieBase = zombieBase,
		        	  coinAmount = coinAmount,
		        	  gameName = fileName.split("/").last
		)
	}
}