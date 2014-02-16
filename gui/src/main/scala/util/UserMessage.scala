package util

trait userMsg

case class MoveUp() extends userMsg
case class MoveDown() extends userMsg
case class MoveLeft() extends userMsg
case class MoveRight() extends userMsg
case class RestartGame() extends userMsg
case class SwitchToken() extends userMsg
case class NextPlayer() extends userMsg
case class NewGame() extends userMsg