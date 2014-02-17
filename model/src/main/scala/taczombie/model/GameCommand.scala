package taczombie.model

trait GameCommand

trait InGameCmd extends GameCommand
trait MoveCmd extends InGameCmd
case object MoveUp extends MoveCmd
case object MoveDown extends MoveCmd
case object MoveLeft extends MoveCmd
case object MoveRight extends MoveCmd

trait SwitchCmd extends InGameCmd
case object NextToken extends SwitchCmd
case object NextPlayer extends SwitchCmd

case object RespawnToken extends InGameCmd