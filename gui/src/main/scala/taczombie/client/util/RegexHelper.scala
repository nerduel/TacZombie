package taczombie.client.util

object RegexHelper {

  def checkPort(input: String): Boolean = {
    val portPatternNumber = """^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$""".r

    if (portPatternNumber.findFirstIn(input) != None) {
      return true
    } else {
      return false
    }
  }

  
}