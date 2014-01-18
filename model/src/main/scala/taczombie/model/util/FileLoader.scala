package taczombie.model.util

import taczombie.model.GameObject._
import taczombie.model.GameObject
import scala.io.Source

case class Configuration(array : Array[Array[GameObject]] = Array.empty,
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
	    
	    val array : Array[Array[GameObject]] = Array.ofDim[GameObject](height,width)
	    
		for (row <- 0 until height)
		{
			for (col <- 0 until width) 
			{
				lvl(row)(col) match 
				{
					case '#' => 
					    array(row)(col) = GameObject.Wall
					case '.' => 
					    array(row)(col) = GameObject.Coin
					    coinAmount += 1
					case ';' => 
					    array(row)(col) = GameObject.Powerup
					case 'H' => 
					    humanBase = (row,col)
				    	array(row)(col) = GameObject.None
					case 'Z' => 
					    zombieBase = (row,col)
					    array(row)(col) = GameObject.None
				}
			} 
		}
	    
		Configuration(array = for (line <- array.map(_.clone)) yield line,
		        	  levelWidth = width,
		        	  levelHeight = height,
		        	  humanBase = humanBase,
		        	  zombieBase = zombieBase,
		        	  coinAmount = coinAmount,
		        	  gameName = fileName.split("/").last
		)
	}
}