package view.tui

import controller.ViewController
import model.ViewModel
import util.Observer
import view.gui.Address
import util.RegexHelper
import view.main.View
import java.util.concurrent.FutureTask
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Tui(model: ViewModel, controller: ViewController) extends Observer with View {
  model.add(this)
  var continue = true
  var restart = false
  val pool = Executors.newFixedThreadPool(1);
  
  val arrowKeyLeft = '\033' :: '[' :: 'D' :: Nil
  val arrowKeyUp = '\033' :: '[' :: 'A' :: Nil
  val arrowKeyDown = '\033' :: '[' :: 'B' :: Nil
  val arrowKeyRight = '\033' :: '[' :: 'C' :: Nil

  val future = new FutureTask[Boolean](new Callable[Boolean]() {
  	def call() : Boolean = {
  	  readInput
  	  return restart
    }
  })
  
  def readInput {
    while (continue) {
      val input = readLine
      if (continue) {
        input.toList match {
          case `arrowKeyUp` =>
            controller.moveUp
          case `arrowKeyDown` =>
            controller.moveDown
          case `arrowKeyLeft` =>
            controller.moveLeft
          case `arrowKeyRight` =>
            controller.moveRight
          case 'q' :: Nil =>
            continue = false
            controller.disconnect
          case 'm' :: Nil =>
            controller.nextGame
          case 'r' :: Nil =>
            controller.restartGame
          case 'g' :: Nil =>
            controller.switchToken
          case 'n' :: Nil =>
            controller.nextPlayer
          case _ =>
            printGameField
            println
            println(Console.RED_B + "Invalid Input!" + Console.RESET)
            printHelp
        }
      }
    }
  }
  
  def open : Boolean = {
  	if(controller.connect) {
      pool.submit(future)
      println("\nWelcome to TacZombie!")
      update
  
      // Wait for future to return.
      val ret = future.get
      controller.close
      pool.shutdownNow()
      return ret
  	} else {
  	  controller.close
  	  return false
  	}
  }

  def update {
    printGameField
    println
    println(Console.GREEN_B + model.gameMessage + Console.RESET)
    printHelp
  }

  def printHelp {
    var outputBuffer = Array.ofDim[String](10, 2)

    outputBuffer(0)(0) = "Move Player: <←>, <↑>, <→>, <↓>  "
    outputBuffer(1)(0) = "Respawn token <f>                "
    outputBuffer(2)(0) = "Switch token: <g>                "
    outputBuffer(3)(0) = "Next player: <n>                 "
    outputBuffer(4)(0) = "New game: <m>                    "
    outputBuffer(5)(0) = "Restart game: <r>                "
    outputBuffer(6)(0) = "                                 "
    outputBuffer(7)(0) = "                                 "
    outputBuffer(8)(0) = "                                 "
    outputBuffer(9)(0) = "                                 "

    outputBuffer(0)(1) = "Moves remaining:\t" + model.movesRemaining
    outputBuffer(1)(1) = "-------------------------------"
    import util.ViewHelper._
    outputBuffer(2)(1) = "Current player:\t" + model.currentPlayerToken
    outputBuffer(3)(1) = "Dead Tokens:\t" + model.deadTokens
    outputBuffer(4)(1) = "Total Tokens:\t" + model.totalTokens
    outputBuffer(5)(1) = if (model.currentPlayerToken == "Human") "Lifes:\t\t" + model.lifes
    else "Frozen Time:\t" + model.frozenTime
    outputBuffer(6)(1) = if (model.currentPlayerToken == "Human") "Coins collected:\t" + model.coins
    else ""
    outputBuffer(7)(1) = if (model.currentPlayerToken == "Human") "Score:\t\t" + model.score
    else ""
    outputBuffer(8)(1) = if (model.currentPlayerToken == "Human") "Powerup time:\t" + model.powerUp
    else ""
    outputBuffer(9)(1) = if (model.currentPlayerToken == "Human") "Frozen Time:\t" + model.frozenTime
    else ""

    println("----------------------------------|----------------------------------")
    for (i <- 0 until 10) {
      println(outputBuffer(i)(0) + " | " + outputBuffer(i)(1))
    }
    println("----------------------------------|----------------------------------")
  }

  def printGameField {
    for (x <- 0 until model.levelHeight) {
      for (y <- 0 until model.levelWidth) {
        val cell = {
          if (model.cells.contains(x, y))
            model.cells(x, y)
          else
            null
        }
        if (cell != null) {
          if (cell._2) {
            if (cell._1 == 'H' && model.humanTokens(x, y) == true)
              print(Console.YELLOW_B + " 😒 " + Console.RESET)
            else
              print(Console.YELLOW_B + getChar(cell._1) + Console.RESET)
          } else {
            if (cell._1 == 'H' && model.humanTokens(x, y) == true)
              print(Console.WHITE_B + " 😒 " + Console.RESET)
            else
              print(Console.WHITE_B + getChar(cell._1) + Console.RESET)
          }
        }
      }
      println
    }
  }

  def getChar(token: Char): String = {
    token match {
      case 'C' => return " • "
      case 'H' => return " 😓 "
      case 'Z' => return " 😈 "
      case 'P' => return " ★ "
      case 'N' => return "   "
      case 'W' => return "███"
      case _ => return "███"
    }
  }
}