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
      
      x match {
        case 0 => print("\t Current player:\t" + getTokenName(model.currentPlayerTokenAsChar) + '\n')
        case 1 => print("\t Current lifes:\t\t" + model.lifes + '\n')
        case 2 => print("\t Current coins:\t\t" + model.coins + '\n')
        case 3 => print("\t Score:\t\t\t" + model.score + '\n')
        case 4 => print("\t Zombiekiller:\t\t" + model.powerUp + '\n')
        case 5 => print("\t ------------------------\n")
        case 6 => print("\t Moves remaining:\t" + model.movesRemaining + '\n')
        case _ => print('\n')
      }
    }
  }
  
  def getTokenName(token: Char) : String = {
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