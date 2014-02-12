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
    
    // TODO: why is that in this class???
    // How should that work without accessing the gameField?????
    def calculateAllowedMoves(range : Int, g : Game) = {
      var toVisit = scala.collection.mutable.Stack.empty[(Int,Int)]
      var pathList = ListBuffer(ListBuffer.empty[(Int, Int)])
      var element = tuple
      var col = 0
      var alreadyVisited = ListBuffer(element)

      do {
        if (pathList.apply(col).size <= range) {
          val neighbours = getNeighbours(element, alreadyVisited.toList, g.gameField)

          if (neighbours.size == 0) {
            if (pathList.last == pathList(col))
              pathList = pathList.+=(ListBuffer.empty[(Int, Int)])
            col += 1
            alreadyVisited = ListBuffer()
          }
          else {
            for (n <- neighbours) toVisit.push(n)

            // copy current List if more than one neighbour was found
            if (pathList.apply(col).size != 0)
              for (t <- 1 until neighbours.size)
                pathList.insert(col, pathList.apply(col))
          }
        }
        else {
          if (pathList.last == pathList(col)) {
            pathList = pathList.+=(ListBuffer.empty[(Int, Int)])
          } else {
            alreadyVisited = ListBuffer()
          }
          col += 1
        }

        element = toVisit.pop
        pathList(col) = pathList(col) ++ ListBuffer(element)
        alreadyVisited.+=(element)

      } while (!toVisit.isEmpty)
      
      println(pathList)
      pathList.toList.flatten.distinct
    }
    
    // Input must be (y,x). map needs coords in (y,x)
    // result is a List of valid coordinates for the array (y,x)
    // TODO: This function must check if the neighbour is not a
    // wall!!
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