package taczombie.client.test.util

import org.specs2.mutable.Specification

import taczombie.client.util.RegexHelper._

class RegexHelperSpec extends Specification {

  "checkPort" should {

    "return true for valid port e.g '9000'" in {
      val isAddress = checkPort("9000")
      isAddress must be_==(true)
    }

    "return false for invalid port 'port'" in {
      val isAddress = checkPort("port")
      isAddress must be_==(false)
    }

    "return false for invalid port '66000'" in {
      val isAddress = checkPort("66000")
      isAddress must be_==(false)
    }

    "return false for invalid port '-1'" in {
      val isAddress = checkPort("-1")
      isAddress must be_==(false)
    }

  }
}