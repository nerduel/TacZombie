package taczombie.test.model

import org.specs2.mutable._
import taczombie.model.util.LevelCreator
import scala.collection.mutable.ListBuffer

import taczombie.model.util.LevelHelper._
import taczombie.model.util.CoordinateHelper._

import scala.language.implicitConversions

object GameValidator {
  class myLevelHelper(field : Array[Array[String]]) {

    def getAllVisitableFields(startPoint : (Int, Int)) : List[(Int, Int)] = {
      val element = startPoint
      var pathList = ListBuffer(element)
      var toVisit = getNeighbours(element, pathList.toList)

      while (!toVisit.isEmpty) {
        val nextVisits = (
          for {
            pos <- toVisit
            neighbours <- getNeighbours(pos, pathList.toList)
          } yield neighbours
        ).distinct

        pathList ++= nextVisits
        toVisit = nextVisits
      }

      pathList.toList
    }

    private def getNeighbours(startPoint : (Int, Int), alreadyVisited : List[(Int, Int)]) = {
      val neighbours =
        ListBuffer(startPoint.rightOf) ++
          ListBuffer(startPoint.belowOf) ++
          ListBuffer(startPoint.leftOf) ++
          ListBuffer(startPoint.aboveOf)

      for (
        n <- neighbours.toList;
        if (!alreadyVisited.contains(n));
        if (!field(n).isWall)
      ) yield n
    }

    def getWalkableFields(height: Int, width : Int) : List[(Int, Int)] = {
      for {
        row <- 0 until height
        col <- 0 until width
        if (!field((row, col)).isWall)
      } yield (row, col)
    }.toList

  }

  implicit def myLevelWrapper(field : Array[Array[String]]) =
    new myLevelHelper(field)
}

import GameValidator._

class LevelCreatorSpec extends Specification {

  sequential
  
  private def test5GeneratedMaps(height : Int, width : Int) : List[Boolean] = {
    val levelCreator = new LevelCreator()
    for {
      i <- 0 until 10
      level = levelCreator.create(height, width, 1)
      walkable = level.getWalkableFields(height, width)
      reachedFields = level.getAllVisitableFields(walkable(0))
    } yield walkable.size == reachedFields.size
  }.toList

  "Every walkable field of a generated level (21x21)" should {
    "be visitable from each walkable position (5 times)" in {

      test5GeneratedMaps(21, 21).filter(_ == true).size must be_==(10)
    }
  }

  "Every walkable field of a generated level (21x19)" should {
    "be visitable from each walkable position (5 times)" in {

      test5GeneratedMaps(21, 19).filter(_ == true).size must be_==(10)
    }
  }

  "Every walkable field of a generated level (25x23)" should {
    "be visitable from each walkable position (5 times)" in {

      test5GeneratedMaps(25, 23).filter(_ == true).size must be_==(10)
    }
  }
}
