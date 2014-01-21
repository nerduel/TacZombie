package taczombie.model.util

import scala.language.implicitConversions

object CoordinateHelper {
  
  class IntIntTuple2Helper(tuple : (Int,Int)) {
    
    def leftOf : (Int,Int) = (tuple._1, tuple._2 - 1)
    def rightOf : (Int,Int) = (tuple._1, tuple._2 + 1)
    def aboveOf : (Int,Int) = (tuple._1 - 1, tuple._2)
    def belowOf : (Int,Int) = (tuple._1 + 1, tuple._2)
    
  }
  
  implicit def intIntTuple2Wrapper(tuple: (Int,Int)) = 
    new IntIntTuple2Helper(tuple)
}