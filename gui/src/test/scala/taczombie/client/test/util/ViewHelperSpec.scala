package taczombie.client.test.util

import org.specs2.mutable.Specification

import taczombie.client.util.ViewHelper.charName2Wrapper

class ViewHelperSpec extends Specification {
  import taczombie.client.util.ViewHelper._

  "toName" should {

    "return 'Human' when char is 'H' " in {
      val ret = 'H'.toName

      ret must be_==("Human")
    }

    "return 'Zombie' when char is 'Z'" in {
      val ret = 'Z'.toName

      ret must be_==("Zombie")
    }

    "return 'Unknown' when char is anything" in {
      val ret = 'X'.toName
      val ret1 = '!'.toName

      ret must be_==("Unknown")
      ret1 must be_==("Unknown")
    }
  }
}