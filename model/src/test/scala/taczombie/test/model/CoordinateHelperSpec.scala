package taczombie.test.model

import org.specs2.mutable._
import taczombie.test.model.TestObjects._
import taczombie.model.GameFactory

import taczombie.model.util.CoordinateHelper._
class CoordinateHelperSpec extends Specification {
  
  val testFile = getClass().getResource("/TestLevel_correct")
  val testGame = GameFactory.newGame(random = false, file = testFile.getFile())
  
  val testCoord1 = (10,10)
  val testCoord2 = (1,3)
  
  val -- = (a:Int,b:Int) => a - b
  val ++ = (a:Int,b:Int) => a + b
  
  private def upperLeftFor5StepsInBothDimensions(coord : (Int,Int))
  	(y : Int, fn1 : (Int,Int) => Int)
  	(x : Int, fn2 : (Int,Int) => Int) = {
	for {
    	  i <- 0 until 5
    	  j <- 0 until 5
    } yield coord.isUpperLeftOf(fn1(y,i),fn2(x,j))
  }.toList
  
  private def lowerRightFor5StepsInBothDimensions(coord : (Int,Int))
  	(y : Int, fn1 : (Int,Int) => Int)
  	(x : Int, fn2 : (Int,Int) => Int) = {
	for {
    	  i <- 0 until 5
    	  j <- 0 until 5
    } yield coord.isLowerRightOf(fn1(y,i),fn2(x,j))
  }.toList
  
  testCoord1.toString should {
    "have (10,9) left of it" in {
      testCoord1.leftOf must be_==(10,9)
    }
    
    "have (10,11) right of it" in {
      testCoord1.rightOf must be_==(10,11)
    }
    
    "have (9,10) above of it" in {
      testCoord1.aboveOf must be_==(9,10)
    }
    
    "have (11,10) below of it" in {
      testCoord1.belowOf must be_==(11,10)
    }
    
    "is upper left of (11+i,11+j) " in {
      upperLeftFor5StepsInBothDimensions(testCoord1)(11, ++)(11, ++)
      	.exists(_ == false) must be_!=(true)
    }
    
    "is not upper left of (10-j,11+i) " in {
      upperLeftFor5StepsInBothDimensions(testCoord1)(10, --)(11, ++)
      	.exists(_ == true) must be_!=(true)
    }
    
    "is not upper left of (11+i,10-j) " in {
      upperLeftFor5StepsInBothDimensions(testCoord1)(11, ++)(10, --)
      	.exists(_ == true) must be_!=(true)
    }
    
    "is lower right of (9-i,9-j) " in {
      lowerRightFor5StepsInBothDimensions(testCoord1)(9, --)(9, --)
      	.exists(_ == false) must be_!=(true)
    }
    
    "is not lower right of (10+i,9-j) " in {
      lowerRightFor5StepsInBothDimensions(testCoord1)(10, ++)(9, --)
      	.exists(_ == true) must be_!=(true)
    }
    
    "is not lower right of (9-i,10+j) " in {
      lowerRightFor5StepsInBothDimensions(testCoord1)(9, --)(10, ++)
      	.exists(_ == true) must be_!=(true)
    }
  }
  
  testCoord1.toString in {
    "minus " + testCoord2.toString should {
      "be (9,7)" in {
        testCoord1 - testCoord2 must be_==(9,7)
      }
    }
    
    "plus " + testCoord2.toString should {
      "be (11,13)" in {
        testCoord1 + testCoord2 must be_==(11,13)
      }
    }
    
    "times factor -1" should {
      "be the inverted value (-10,-10)" in {
        testCoord1 * -1 must be_==(-10,-10)
      }
    }
  }
  
  "In testGame 'Testlevel_correct' the field at coordinate (14,13)" should {
    "have the walkable neighbours (15,13) and (14,13)" in {
      val expectedNeighbours = (15,13) :: (13,13) :: Nil
      (14,13).getNeighbours(List(), testGame.gameField) must containAllOf(expectedNeighbours)
    }
  }
  
  "Method allowed move for the range 5" should {
    "return a correct List with walkable fields at startpoint (14,13)" in {
    	val expectedValue = (15,9)  :: (15,10) :: (15,11) :: (15,12) :: (15,13) :: (15,14) :: (15,15) :: 
    						(14,13) :: (14,15) :: 
    						(13,11) :: (13,12) :: (13,13) :: (13,14) :: (13,15) ::
    						(12,11) :: (12,15) :: 
    						(11,11) :: (11,15) :: Nil
							
		(14,13).calculateAllowedMoves(5, testGame) must containAllOf(expectedValue)
    }
  }
}