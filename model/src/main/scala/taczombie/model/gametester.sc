package taczombie.model

import scala.Array.canBuildFrom
import scala.collection.immutable.Map
import scala.collection.immutable.TreeMap
import scala.collection.immutable.HashSet
import scala.io.Source
import java.lang.Character

object gametester {
  
def printGameField(gameField : GameField) {
  for(x <- 0 to gameField.levelHeight) {
    for(y <- 0 to gameField.levelWidth) {
      val cell = {
        if (gameField.gameFieldCells.contains(x,y))
          gameField.gameFieldCells(x,y)
        else
          null
      }
      val char = {
        if (cell == null) ""
        else if (cell.containsWall) "█"
        else if (cell.containsZombieToken) "😈"
        else if (cell.containsHumanToken) "😃"
        else if (cell.containsCoin) "•"
        else if (cell.containsPowerup) "★"
        else ""
      }
      print(char)
    }
    print('\n')
  }
}                                                 //> printGameField: (gameField: taczombie.model.GameField)Unit

  var game = GameFactory.newGame(false)           //> game  : taczombie.model.Game = taczombie.model.Game@7561ab68
	printGameField(game.gameField)            //> █████████████████
                                                  //| █★•••••••••••••★█
                                                  //| █•████•████•███•█
                                                  //| █•█•█•••••••••█•█
                                                  //| █•█•••█•██•██•••█
                                                  //| █•█•█•█•••••••███
                                                  //| █•█•█•███████•••█
                                                  //| █•█•█•••••█████•█
                                                  //| █•█•███•█••████•█
                                                  //| █•█•••••██••███•█
                                                  //| █••██••████•••••█
                                                  //| ██••██•••█••███•█
                                                  //| ███••███•██•███•█
                                                  //| █•••😃••••██•••••█
                                                  //| █•███•███████•█•█
                                                  //| █★••••••😈••••••★█
                                                  //| █████████████████
                                                  //| 
	game = game.executeCommand(new MoveLeft())//> scala.MatchError: null
                                                  //| 	at taczombie.model.GameField$$anonfun$move$1.apply(GameField.scala:48)
                                                  //| 	at taczombie.model.GameField$$anonfun$move$1.apply(GameField.scala:47)
                                                  //| 	at scala.collection.immutable.HashSet$HashSet1.foreach(HashSet.scala:153
                                                  //| )
                                                  //| 	at scala.collection.immutable.HashSet$HashTrieSet.foreach(HashSet.scala:
                                                  //| 306)
                                                  //| 	at taczombie.model.GameField.move(GameField.scala:47)
                                                  //| 	at taczombie.model.Game.move(Game.scala:64)
                                                  //| 	at taczombie.model.Game.executeCommand(Game.scala:23)
                                                  //| 	at taczombie.model.gametester$$anonfun$main$1.apply$mcV$sp(taczombie.mod
                                                  //| el.gametester.scala:38)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$$anonfun$$exe
                                                  //| cute$1.apply$mcV$sp(WorksheetSupport.scala:76)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.redirected(W
                                                  //| orksheetSupport.scala:65)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.$execute(Wor
                                                  //| ksheetSupport.scala:75)
                                                  //| 	at taczombie.model.gametester
                                                  //| Output exceeds cutoff limit.
	
	printGameField(game.gameField)

}