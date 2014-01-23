import scala.collection.mutable.ListBuffer
import taczombie.model.GameObject
import taczombie.model.GameObject._
import taczombie.model.util.CoordinateHelper._
import scala.util.Random

object playground {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
                                                  
/*	var t = Array(Array('#','#','#','#','#'),
                Array('#','.',' ',' ','#'),
                Array('#',' ','#','.','#'),
                Array('#',' ',' ','.','#'),
                Array('#',' ','#','.','#'),
                Array('#',' ',' ','.','#'),
                Array('#','#','#','#','#'))
                
                
  // Input must be (y,x). Array needs coords in (y,x)
  // result is a List of valid coordinates for the array (y,x)
	def getNeighbours(position : (Int,Int), alreadyVisited : List[(Int,Int)]) = {
	// von Neumann neighborhood, East, South, West, North
		val neighbours = ListBuffer((position._1, position._2 + 1)) ++
		               		 ListBuffer((position._1 + 1, position._2)) ++
		               		 ListBuffer((position._1, position._2 - 1)) ++
		               		 ListBuffer((position._1 - 1, position._2))
		                   
		for (n <- neighbours.toList;
		    if (n._1 != -1);
		    if (n._1 <= 5);
		    if (n._2 != -1);
		    if (n._2 <= 7);
		    if (t(n._1)(n._2) != '#');
		    if (! alreadyVisited.contains(n))
		) yield n
	}
  
  val position = (1,1)
	val validMoves = 4
	var toVisit = scala.collection.mutable.Stack(position)
	var pathList = ListBuffer(ListBuffer.empty[(Int,Int)])
	var element = position
	var col = 0
	var alreadyVisited = ListBuffer(element)
  
		do {
			if (pathList.apply(col).size < validMoves) {
				val neighbours = getNeighbours(element, alreadyVisited.toList)
           
				if (neighbours.size == 0) {
					if (pathList.last == pathList(col))
					pathList = pathList.+=(ListBuffer.empty[(Int,Int)])
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
					pathList = pathList.+=(ListBuffer.empty[(Int,Int)])
					
				alreadyVisited = ListBuffer()
				col += 1
			}
			
			element = toVisit.pop
			pathList(col) = pathList(col) ++ ListBuffer(element)
			alreadyVisited.+=(element)
        
		} while (! toVisit.isEmpty)
	
	
	
	for (x <- pathList.toList.flatten.distinct) {
			t(x._1)(x._2) = 'B'
	}
	
	for (line <- t){
			line.foreach(print(_))
			println
  }*/
}