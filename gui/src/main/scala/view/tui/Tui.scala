package view.tui

import controller.Communication
import model.ViewModel
import util.Observer

class Tui(model: ViewModel, controller: Communication) extends Observer {

  val arrowKeyLeft = '\033' :: '[' :: 'D' :: Nil
  val arrowKeyUp = '\033' :: '[' :: 'A' :: Nil
  val arrowKeyDown = '\033' :: '[' :: 'B' :: Nil
  val arrowKeyRight = '\033' :: '[' :: 'C' :: Nil

  model.add(this)
  val inputThread = new Thread(new Runnable {
    override def run() {
      try {
        readInput
      } catch {
        case ex: Exception => println(ex.getMessage())
      }
    }
  })

  def readInput {
    while (true) {
      val input = readLine
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
          controller.disconnect
          sys.exit(0)
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

  def show {
    inputThread.start

    println("\nWelcome to TacZombie!")
    update
  }

  def update {
    printGameField
    println
    println(Console.GREEN_B + model.gameMessage + Console.RESET)
    printHelp
  }

  def printHelp {
    println("---------------------------------|----------------------------------")
    print("                                ")
    println(" |  Moves remaining:\t" + model.movesRemaining)
    print("Move Player: <←>, <↑>, <→>, <↓> ")
    println(" |  -----------------------------")
    print("Switch Token: <g>               ")
    println(" |  Current player:\t" + getTokenName(model.currentPlayerTokenAsChar))
    print("Next Player: <h>                ")
    if (model.currentPlayerTokenAsChar == 'H') println(" |  Lifes:\t\t" + model.lifes)
    else if (model.currentPlayerTokenAsChar == 'Z') println(" |  Frozen Time:\t" + model.frozenTime)
    else println(" |  Coins collected:\t" + model.coins)
    print("Next Game: <n>                  ")
    if (model.currentPlayerTokenAsChar == 'H') println(" |  Score:\t\t" + model.score) else println(" |")
    print("Restart Game: <r>               ")
    if (model.currentPlayerTokenAsChar == 'H') println(" |  Powerup time:\t" + model.powerUp) else println(" |")
    print("Quit Game: <q>                  ")
    if (model.currentPlayerTokenAsChar == 'H') println(" |  Frozen Time:\t" + model.frozenTime) else println(" |")
    println("---------------------------------|----------------------------------")
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
          if (cell._2)
            print(Console.RED_B + getChar(cell._1) + Console.RESET)
          else
            print(Console.WHITE_B + getChar(cell._1) + Console.RESET)
        }
      }
      println
    }
  }

  def getTokenName(token: Char): String = {
    token match {
      case 'H' => return "Human"
      case 'Z' => return "Zombie"
      case _ => return "Unknown"
    }
  }

  def getChar(token: Char): String = {
    token match {
      case 'C' => return " • "
      case 'H' => return " H "
      case 'Z' => return " Z "
      case 'P' => return " ★ "
      case 'N' => return "   "
      case 'W' => return "███"
      case _ => return "███"
    }
  }

}