package taczombie.client.util

import scala.language.implicitConversions

object ViewHelper {

  implicit def charName2Wrapper(token: Char) =
    new charName2Helper(token)

  class charName2Helper(token: Char) {
    def toName: String = {
      token match {
        case 'H' => return "Human"
        case 'Z' => return "Zombie"
        case _ => return "Unknown"
      }
    }
  }
}