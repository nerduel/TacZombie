package util

case class Address(ip: String) {
  override def toString: String = {
    return ip
  }
}