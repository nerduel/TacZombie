package taczombie.model

import GameObject._

class GameField(private val data : Array[Array[GameObject]],
                val levelWidth : Int,
                val levelHeight : Int) {
    
    def clearCell(pos : (Int, Int)) = {
        if (data(pos._1)(pos._2) != GameObject.Wall) {
            val newRow = data(pos._1).updated(pos._2, GameObject.None)
            val updatedData = data.updated(pos._1, newRow)
            new GameField(updatedData, levelWidth, levelHeight)
        }
        else {
            this
        }
    }

    def getObject(pos : (Int, Int)) = {
        if (isValid(pos)) {
            data(pos._1)(pos._2)
        }
        else
            GameObject.Wall
    }
    
    // TODO: Check if obsolete
    def getCopyOfData = for (line <- data.map(_.clone)) yield line

    private def isValid(pos : (Int, Int)) = {
        if ((pos._1 < levelHeight) && (pos._1 >= 0) &&
            (pos._2 < levelWidth) && (pos._2 >= 0))
            true
        else
            false
    }

}