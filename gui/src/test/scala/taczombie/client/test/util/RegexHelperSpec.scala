package taczombie.client.test.util

import org.specs2.mutable.Specification

import taczombie.client.util.RegexHelper.checkAddress

class RegexHelperSpec extends Specification {

  "checkAddress" should {

    "return true for 'localhost'" in {
      val isAddress = checkAddress("localhost")
      isAddress must be_==(true)
    }

    "return true for valid ip" in {
      val isAddress = checkAddress("192.168.0.1")
      isAddress must be_==(true)
    }

    "return false for invalid ip" in {
      val isAddress = checkAddress("1111.168.0.1")
      isAddress must be_==(false)
    }

    "return false for no valid ip or not localhost" in {
      val isAddress = checkAddress("loclhost")
      isAddress must be_==(false)
    }
  }
}