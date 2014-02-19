package util

object RegexHelper {

  def checkAddress(input: String): Boolean = {
    val ipPatternNumber = """^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}""".r

    if (input == "localhost" || ipPatternNumber.findFirstIn(input) != None) {
      return true
    } else {
      return false
    }
  }

}