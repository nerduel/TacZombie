package taczombie.model

trait GameCommand

trait Move extends GameCommand
case class MoveUp() extends Move
case class MoveDown() extends Move
case class MoveLeft() extends Move
case class MoveRight() extends Move
case class YieldMove() extends Move

case class Restart() extends GameCommand
case class Quit() extends GameCommand