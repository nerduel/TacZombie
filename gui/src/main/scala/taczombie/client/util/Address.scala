package taczombie.client.util

case class Address(ip: String, port: String) {
  override def toString: String = {
    (ip + ":" + port)
  }
}