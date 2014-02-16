package taczombie.model

object GameState extends Enumeration {
	type GameState = Value
	val Lose, Win, InGame, NeedPlayerSwitch, NeedTokenSwitch, GameOver = Value
}