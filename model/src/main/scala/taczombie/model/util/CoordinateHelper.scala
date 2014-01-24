package taczombie.model.util

import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions

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
    
    def calculateAllowedMoves(range : Int) = {
      var toVisit = scala.collection.mutable.Stack.empty[(Int,Int)]
      var pathList = ListBuffer(ListBuffer.empty[(Int, Int)])
      var element = tuple
      var col = 0
      var alreadyVisited = ListBuffer(element)
    
      do {
          if (pathList.apply(col).size < range) {
              val neighbours = getNeighbours(alreadyVisited.toList)
    
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
              if (pathList.last == pathList(col))
                  pathList = pathList.+=(ListBuffer.empty[(Int, Int)])
    
              alreadyVisited = ListBuffer()
              col += 1
          }
    
          element = toVisit.pop
          pathList(col) = pathList(col) ++ ListBuffer(element)
          alreadyVisited.+=(element)
    
      } while (!toVisit.isEmpty)
    
      pathList.toList.flatten.distinct
    }
    
    // Input must be (y,x). map needs coords in (y,x)
    // result is a List of valid coordinates for the array (y,x)
    def getNeighbours(alreadyVisited : List[(Int, Int)]) = {
        // von Neumann neighborhood, East, South, West, North
        val neighbours = 
          ListBuffer(tuple.rightOf) ++
          ListBuffer(tuple.belowOf) ++
          ListBuffer(tuple.leftOf) ++
          ListBuffer(tuple.aboveOf)
  
        for (
            n <- neighbours.toList;
            if (!alreadyVisited.contains(n))
        ) yield n
    }      
	}
}