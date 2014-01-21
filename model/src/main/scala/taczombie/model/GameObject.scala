package taczombie.model

object GameObject {
  
  trait GameObject
  
  trait StaticGameObject extends GameObject
  trait VersatileGameObject extends GameObject
  
  trait NonHuman extends VersatileGameObject  
  trait Collectable extends NonHuman
  
  case class Wall extends StaticGameObject
  
  case class HumanToken extends VersatileGameObject
  case class ZombieToken(collectable : Collectable) extends NonHuman
  
  case class Coin extends Collectable
  case class Powerup extends Collectable
}
