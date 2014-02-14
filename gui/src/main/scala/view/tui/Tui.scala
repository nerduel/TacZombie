package view.tui

import controller.Communication
import model.ViewModel
import util.Observer

class Tui(model: ViewModel, controller: Communication) extends Observer {

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
        case 'q' :: Nil =>
          controller.disconnect
          exit(0)
        case 'w' :: Nil =>
          controller.moveUp
        case 's' :: Nil =>
          controller.moveDown
        case 'a' :: Nil =>
          controller.moveLeft
        case 'd' :: Nil =>
          controller.moveRight
        case 'n' :: Nil =>
          controller.newGame
        case _ =>
          println("Invalid Input!")
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
  }

  def printHelp {
    println("------------------------------")
    println("Move Player: <a>, <w>, <s>, <d>")
    println("Finisch Move: <g>")
    println("New Game: <n>")
    println("Quit Game: <q>")
    println("------------------------------")
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
      print('\n')
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