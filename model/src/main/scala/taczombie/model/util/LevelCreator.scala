package taczombie.model.util

import scala.util.Random
import CoordinateHelper._
import scala.language.implicitConversions

private object LevelHelper {
  val path = " "
  val wall = "#"
  val zombieBase = "Z"
  val humanBase = "H"
  val innerWall = "+"
  val coin = "."
  val powerup = ";"
  val unset = "x"

  class StringHelper(value : String) {
    def isPath = value == path
    def isWall = value == wall
    def isUnset = value == unset
    def isInnerWall = value == innerWall
  }

  class Stringarray2DimHelper(array : Array[Array[String]]) {
    def apply(id : (Int, Int)) = array(id._1)(id._2)

    def apply(id : (Int, Int), value : String) = {
      array(id._1)(id._2) = value
    }

    def addCoin(id : (Int, Int)) = {
      array(id._1)(id._2) = coin
      (array, id)
    }

    def addWall(id : (Int, Int)) = {
      array(id._1)(id._2) = wall
      (array, id)
    }

    def addZombieBase(id : (Int, Int)) = {
      array(id._1)(id._2) = zombieBase
      (array, id)
    }
    
    def addHumanBase(id : (Int, Int)) = {
      array(id._1)(id._2) = humanBase
      (array, id)
    }

    def addInnerWall(id : (Int, Int)) = {
      array(id._1)(id._2) = innerWall
      (array, id)
    }

    def addPath(id : (Int, Int)) = {
      array(id._1)(id._2) = path
      (array, id)
    }

    def addPowerup(id : (Int, Int)) = {
      array(id._1)(id._2) = powerup
      (array, id)
    }
  }

  class StringarrayIntIntTuple2Helper(tuple : (Array[Array[String]], (Int, Int))) {
    val value = tuple._1(tuple._2._1)(tuple._2._2)
    val id = tuple._2
    val height = tuple._1.size
    val xAxis = height / 2
    val width = tuple._1.length
    val yAxis = width / 2

    def copyIntoEachQuadrant = {
      mirrorVertical
      mirrorHorizontal
      mirrorPointReflection
      (tuple._1, id)
    }

    def mirrorVertical = {
      val xDistance = (yAxis - id._2)
      tuple._1(id._1)(yAxis + xDistance) = value
      (tuple._1, id)
    }

    def mirrorHorizontal = {
      val yDistance = (xAxis - id._1)
      tuple._1(xAxis + yDistance)(id._2) = value
      (tuple._1, id)
    }

    def mirrorPointReflection = {
      val yDistance = (xAxis - id._1)
      val xDistance = (yAxis - id._2)
      tuple._1(xAxis + yDistance)(yAxis + xDistance) = value
      (tuple._1, id)
    }
  }

  class ListIntIntTuple2IntIntTuple2Helper(list : List[((Int, Int), (Int, Int))]) {
    def getRandomElement = {
      if (list.size > 0)
        list(Random.nextInt(list.size) % list.size)
      else
        null
    }
  }

  class IntIntTuple2IntIntTuple2Helper(tuple : ((Int, Int), (Int, Int))) {
    def visitor = tuple._1
    def prevVisitor = tuple._2
  }

  class ListIntIntTuple2Helper(list : List[(Int, Int)]) {
    def getRandomElement = {
      if (list.size > 0)
        list(Random.nextInt(list.size) % list.size)
      else
        null
    }
  }

  implicit def StringWrapper(tuple : String) =
    new StringHelper(tuple)

  implicit def Stringarray2DimWrapper(array : Array[Array[String]]) =
    new Stringarray2DimHelper(array)

  implicit def StringarrayIntIntTuple2Wrapper(tuple : (Array[Array[String]], (Int, Int))) =
    new StringarrayIntIntTuple2Helper(tuple)

  implicit def ListIntIntTuple2IntIntTuple2Wrapper(list : List[((Int, Int), (Int, Int))]) =
    new ListIntIntTuple2IntIntTuple2Helper(list)

  implicit def ListIntIntTuple2Wrapper(list : List[(Int, Int)]) =
    new ListIntIntTuple2Helper(list)

  implicit def IntIntTuple2IntIntTuple2Wrapper(tuple : ((Int, Int), (Int, Int))) =
    new IntIntTuple2IntIntTuple2Helper(tuple)
}

import LevelHelper._

class LevelCreator {
  private object Border extends Enumeration {
    type Border = Value
    val UpperLeft, UpperRight, LowerRight, LowerLeft = Value
  }

  val alreadyVisited = (-1, -1)
  var width = 0
  var height = 0
  var field : Array[Array[String]] = Array()
  var cInterval : (Int, Int) = (0, 0)
  var rInterval : (Int, Int) = (0, 0)

  def create(levelHeight : Int, levelWidth : Int, amountHumans : Int) = {
    height = makeOdd(levelHeight)
    width = makeOdd(levelWidth)
    rInterval = (0, (height / 2) - 1)
    cInterval = (0, (width / 2) - 1)

    var done = false

    while (!done) {
      field = Array.fill[String](height, width)(LevelHelper.unset)

      buildFrame

      initializeMap

      buildMap(rInterval._2 - 1, cInterval._2 - 1)

      var count = 0
      for (c <- 1 until cInterval._2; r <- 1 until rInterval._2)
        if (field((c, r)).isUnset) count += 1

      if (count > (rInterval._2 * cInterval._2 * 7) / 16) {
        done = false
      }

      finalizeMap

      done = true
      for (c <- 1 until cInterval._2) {
        for (r <- 1 until rInterval._2) {
          if (getNeighbours(c, r).filter(id => field(id).isWall).size > 2) {
            done = false
          }
        }
      }
    }

    addPowerups
    
    // add human Base to map
    for (i <- 0  until amountHumans)
    	addHumanBase
    
    addCoins
    
    field
  }

  private def buildFrame {
    buildVerticalWall
    buildHorizontalWall
  }

  private def buildVerticalWall {
    for (row <- 0 to rInterval._2)
      field.addWall(row, 0).copyIntoEachQuadrant

    for (row <- 1 to rInterval._2)
      field.addInnerWall(row, cInterval._2).copyIntoEachQuadrant
  }

  private def buildHorizontalWall {
    for (col <- 0 to cInterval._2)
      field.addWall(0, col).copyIntoEachQuadrant

    for (col <- 1 to cInterval._2)
      field.addInnerWall(rInterval._2, col).copyIntoEachQuadrant

  }

  private def initializeMap {
    var next =
      field.addWall((rInterval._2, cInterval._2).belowOf).copyIntoEachQuadrant._2

    next = field.addWall(next.rightOf)._2
    next = field.addZombieBase(next.aboveOf)._2
    next = field.addPath(next.aboveOf).mirrorHorizontal._2
    next = field.addPath(next.leftOf).copyIntoEachQuadrant._2
    next = field.addWall(next.belowOf).copyIntoEachQuadrant._2
    field.addWall(next.rightOf.belowOf.belowOf)._2

    field.addPath(next.leftOf.aboveOf).copyIntoEachQuadrant
  }

  private def buildMap(startPoint : (Int, Int)) {
    var prevVisitor = startPoint
    var nextStartPoints = List((startPoint.leftOf, prevVisitor), (startPoint.aboveOf, prevVisitor))

    var visitor = if (Random.nextInt(1) == 1) {
      startPoint.aboveOf
    }
    else {
      startPoint.leftOf
    }

    nextStartPoints = nextStartPoints.filterNot(elem => elem._1 == visitor)

    var done = false

    while (!done) {
      val possibleNeighbours = getNeighbours(visitor)
      if (isNotMergedWithPath(possibleNeighbours, prevVisitor)) {
        var nghbrs = possibleNeighbours.filterNot(_ == prevVisitor)
          .filterNot(id => field(id).isWall)
          .filterNot(id => id._1 >= cInterval._2)
          .filterNot(id => id._2 >= rInterval._2)

        if (nghbrs.size > 0) {
          field.addPath(visitor).copyIntoEachQuadrant

          for (n <- nghbrs.filter(id => field(id).isUnset)) {
            if (isWall(n)) {
              field.addWall(n).copyIntoEachQuadrant
              nghbrs = nghbrs.filterNot(_ == n)
              nextStartPoints = nextStartPoints.filterNot(_._1 == n)
            }
          }

          prevVisitor = visitor
          visitor = nghbrs.getRandomElement
          nghbrs = nghbrs.filterNot(_ == visitor)

          nextStartPoints = nextStartPoints ++ {
            for (n <- nghbrs) yield (n, prevVisitor)
          }

          if (visitor != null) {
            val step = visitor - prevVisitor
            if (field(visitor + step).isWall) {
              if (field(visitor + step.swap).isWall) {
                if (field(visitor + step.swap.*(-1)).isWall) {
                  field.addWall(visitor).copyIntoEachQuadrant
                }
              }
            }
            else {
              if (field(visitor + step).isInnerWall) {
                field.addPath(visitor).copyIntoEachQuadrant
                field.addPath(visitor + step).copyIntoEachQuadrant
                field.addPath(visitor + step + step).copyIntoEachQuadrant
                field.addWall(visitor + step + step.swap).copyIntoEachQuadrant
                field.addWall(visitor + step + step + step.swap).copyIntoEachQuadrant
                field.addWall(visitor + step + step.swap.*(-1)).copyIntoEachQuadrant
                field.addWall(visitor + step + step + step.swap.*(-1)).copyIntoEachQuadrant

                if (field(visitor + step.*(-1) + step.swap).isWall)
                  field.addWall(visitor + step.swap).copyIntoEachQuadrant

                if (field(visitor + step.*(-1) + step.swap.*(-1)).isWall)
                  field.addWall(visitor + step.swap.*(-1)).copyIntoEachQuadrant

                nghbrs = getNeighbours(visitor)
                  .filterNot(_ == prevVisitor)
                  .filterNot(x => field(x).isWall)

                nextStartPoints = nextStartPoints ++ {
                  for (n <- nghbrs) yield (n, prevVisitor)
                }

                val randElem = nextStartPoints.getRandomElement
                if (randElem != null) {
                  prevVisitor = randElem.prevVisitor
                  visitor = randElem.visitor
                  nextStartPoints = nextStartPoints.filterNot(_._1 == visitor)
                }
                else {
                  done = true
                }
              }
            }
          }
          else {
            val randElem = nextStartPoints.getRandomElement
            if (randElem != null) {
              prevVisitor = randElem.prevVisitor
              visitor = randElem.visitor
              nextStartPoints = nextStartPoints.filterNot(_._1 == visitor)
            }
            else {
              done = true
            }
          }
        }
        else {
          done = true
        }
      }
      else {
        field.addPath(visitor).copyIntoEachQuadrant

        for (
          n <- possibleNeighbours.filterNot(_ == prevVisitor)
            .filter(id => field(id).isUnset)
        ) {
          if (isWall(n)) {
            field.addWall(n).copyIntoEachQuadrant
            nextStartPoints = nextStartPoints.filterNot(_._1 == n)
          }
        }

        if (nextStartPoints.size == 0) {
          done = true
        }
        else {
          prevVisitor = nextStartPoints.getRandomElement.prevVisitor
          visitor = nextStartPoints.getRandomElement.visitor
          nextStartPoints = nextStartPoints.filterNot(_._1 == visitor)
        }
      }
    }
  }

  private def finalizeMap {
    var wall = 0

    val unsetFields = for {
      c <- 1 to cInterval._2
      r <- 1 to rInterval._2
      if (field((c, r)).isUnset)
    } yield (c, r)

    for (uId <- unsetFields) {
      if (isWall(uId)) {
        field.addWall(uId).copyIntoEachQuadrant
      }
      else {
        if (getNeighbours(uId).filter { id =>
          field(id).isPath ||
            field(id).isUnset
        }.size >= 2) {
          field.addPath(uId).copyIntoEachQuadrant
        }
      }
    }

    // set horizontal middle line as wall
    for (c <- 0 until cInterval._2) {
      val id = (rInterval._2, c)
      if (!field(id).isPath) {
        field.addWall(id).copyIntoEachQuadrant
        field.addWall(id.belowOf).mirrorVertical
      }
    }

    // set vertical middle line as wall
    for (r <- 0 until rInterval._2 - 1) {
      val id = (r, cInterval._2)
      if (!field(id).isPath) {
        field.addWall(id).copyIntoEachQuadrant
        field.addWall(id.rightOf).mirrorHorizontal
      }
    }

    for (c <- 1 to cInterval._2; r <- 1 to rInterval._2) {
      if (getNeighbours(c, r).filter { id =>
        field(id).isWall
      }.size > 2) {
        field.addWall(c, r).copyIntoEachQuadrant
      }
    }

    // build paths through the horizontal middle line
    for (c <- 1 until cInterval._2 - 1) {
      var toCheck = (rInterval._2 - 1, c)

      while (toCheck._1 > 1 && !(field(toCheck).isPath)) {
        toCheck = toCheck.aboveOf
      }

      // possible break through
      val surroundedWalls = getNeighbours(toCheck).filter { id =>
        field(id).isWall
      }

      if (surroundedWalls.size > 2) {
        for (r <- toCheck._1 to rInterval._2) {
          // we must break through
          field.addPath(r, c).copyIntoEachQuadrant
        }
        field.addPath((rInterval._2, c).belowOf).mirrorVertical
      }
    }

    val isNoHorizontalBreakthrough = {
      for (c <- 1 until cInterval._2) yield (rInterval._2 + 1, c)
    }.filter(id => field(id).isWall).size

    if (isNoHorizontalBreakthrough == cInterval._2 - 1) {
      var toCheck = (rInterval._2 - 1, 1)

      while (toCheck._1 > 1 && !(field(toCheck).isPath)) {
        toCheck = toCheck.aboveOf
      }

      for (r <- toCheck._1 to rInterval._2) {
        // we must break through
        field.addPath(r, 1).copyIntoEachQuadrant
      }
      field.addPath((rInterval._2, 1).belowOf).mirrorVertical
    }
  }

  private def addCoins {
    for (c <- 1 until width; r <- 1 until height) {
      if (field((r, c)).isPath) {
        field.addCoin((r, c))
      }
    }
  }

  private def addPowerups {
    var start = (0, 0)
    var list = List((1, 1))
    var alreadyVisited = List.empty[(Int, Int)]

    while (field(start).isWall) {
      start = list.head
      alreadyVisited = alreadyVisited :+ start
      list = (list ++ getNeighbours(start).filter { coord =>
        coord > (0, 0)
      }.filter { coord =>
        coord < (rInterval._2, cInterval._2)
      }.filterNot { elem =>
        alreadyVisited.contains(elem)
      })
      list = list.tail
    }
    field.addPowerup(start).copyIntoEachQuadrant
  }

  private def getNeighbours(element : (Int, Int)) = {
    element.leftOf ::
      element.rightOf ::
      element.aboveOf ::
      element.belowOf :: List()
  }

  private def isNotMergedWithPath(list : List[(Int, Int)], prev : (Int, Int)) : Boolean = {
    list.filterNot(_ == prev).filter(x => field(x).isPath).size == 0
  }

  import Border._
  private def isWall(elem : (Int, Int)) = {
    for {
      b <- Border.values
      if (getEdgeOf(elem, b).filter(x => (field(x).isPath)).size == 3)
    } yield elem
  }.toList.distinct.isEmpty == false

  private def getEdgeOf(element : (Int, Int), index : Border) = {
    index match {
      case Border.UpperLeft =>
        element.leftOf ::
          element.leftOf.aboveOf ::
          element.leftOf.aboveOf.rightOf :: List()

      case Border.UpperRight =>
        element.aboveOf ::
          element.aboveOf.rightOf ::
          element.aboveOf.rightOf.belowOf :: List()

      case Border.LowerRight =>
        element.rightOf ::
          element.rightOf.belowOf ::
          element.rightOf.belowOf.leftOf :: List()

      case Border.LowerLeft =>
        element.belowOf ::
          element.belowOf.leftOf ::
          element.belowOf.leftOf.aboveOf :: List()
    }
  }

  private def makeOdd(number : Int) : Int = {
    if (number % 2 != 0) number else number + 1
  }
  
  private def addHumanBase = {
    val cols = width
    val rowsLowerBound = rInterval._2 + 1;
    
    var randomCoords : (Int, Int) = null
    val rand = new Random(System.currentTimeMillis());
    
    do 
      randomCoords = (rand.nextInt(height - rowsLowerBound) + rowsLowerBound, rand.nextInt(width))
    while 
      (!field(randomCoords).isPath)
    
    field.addHumanBase(randomCoords)
  }
}
