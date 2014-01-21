package taczombie.model



trait Player {
	def coordinate() : (Int,Int)
	def state() : PlayerState
}

    


trait PlayerState
case class killed(time : Int = 2) extends PlayerState
case class zombieKiller(time  : Int = 3) extends PlayerState
case class normal() extends PlayerState