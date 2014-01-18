package taczombie.model

trait PlayerState

case class killed(time : Int = 2) extends PlayerState
case class zombieKiller(time  : Int = 3) extends PlayerState
case class normal() extends PlayerState

trait Player {
	def coordinate() : (Int,Int)
	def state() : PlayerState
}

case class Human(coordinate : (Int,Int),
				 state : PlayerState = normal()) extends Player {
}
				 
case class Zombie(coordinate : (Int,Int),
				  state : PlayerState = normal()) extends Player