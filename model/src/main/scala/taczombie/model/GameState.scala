package taczombie.model

object GameState extends Enumeration {
	type GameState = Value
	val GameOver, Win, InGame, NeedPlayerSwitch, NeedTokenSwitch = Value
}