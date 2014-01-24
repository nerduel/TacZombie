package taczombie.model

trait GameCommand

trait Move extends GameCommand
case object MoveUp extends Move
case object MoveDown extends Move
case object MoveLeft extends Move
case object MoveRight extends Move

case object NextToken extends GameCommand
case object NextPlayer extends GameCommand
case object Restart extends GameCommand
case object Quit extends GameCommand