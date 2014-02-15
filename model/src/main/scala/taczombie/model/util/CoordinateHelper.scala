package taczombie.model.util

import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions
import taczombie.model.Game
import taczombie.model.GameFieldCell
import taczombie.model.GameField

object CoordinateHelper {

  implicit def intIntTuple2Wrapper(tuple: (Int,Int)) = 
      new IntIntTuple2Helper(tuple)  
  
  class IntIntTuple2Helper(tuple : (Int,Int)) {
    
    def leftOf : (Int,Int) = (tuple._1, tuple._2 - 1)
    def rightOf : (Int,Int) = (tuple._1, tuple._2 + 1)
    def aboveOf : (Int,Int) = (tuple._1 - 1, tuple._2)
    def belowOf : (Int,Int) = (tuple._1 + 1, tuple._2)
    
    def -(rVal : (Int,Int)) : (Int, Int) = {
        (tuple._1 - rVal._1, tuple._2 - rVal._2)
    }
    
    def +(rVal : (Int,Int)) : (Int, Int) = {
        (tuple._1 + rVal._1, tuple._2 + rVal._2)
    }
    
    def *(factor : Int) : (Int,Int) = {
        (tuple._1 * factor, tuple._2 * factor)
    }
    
    def <(rVal : (Int,Int)) : Boolean = {
    	tuple._1 < rVal._1 &&
    	tuple._2 < rVal._2
    }
    
    def >(rVal : (Int,Int)) : Boolean = {
    	tuple._1 > rVal._1 &&
    	tuple._2 > rVal._2
    }

    def calculateAllowedMoves(range : Int, g : Game) = {
      val element = tuple 
      var toVisit = List(element) 
      var pathList = ListBuffer(element)

      for (step <- 0 until range) {
        val nextVisits = for {
          pos <- toVisit
          neighbours <- getNeighbours(pos, pathList.toList, g.gameField)
        } yield neighbours

        pathList ++= nextVisits
        toVisit = nextVisits
      }
      
      pathList.toList
    }
    
    def getNeighbours(startPoint : (Int,Int), alreadyVisited : List[(Int, Int)], gF : GameField) = {
        // von Neumann neighborhood, East, South, West, North
        val neighbours = 
          ListBuffer(startPoint.rightOf) ++
          ListBuffer(startPoint.belowOf) ++
          ListBuffer(startPoint.leftOf) ++
          ListBuffer(startPoint.aboveOf)
  
        for (
            n <- neighbours.toList;
            if (!alreadyVisited.contains(n));
            if (!gF.gameFieldCells(n).containsWall)
        ) yield n
    }      
	}
}