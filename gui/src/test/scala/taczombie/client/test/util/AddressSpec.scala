package taczombie.client.test.util

import org.specs2.mutable.Specification

import taczombie.client.util.Address

class AddressSpec extends Specification {

  "toString" should {

    "return string version of Address'" in {
      val addressAsString = "localhost:9000"
      val address = new Address("localhost", "9000")
      address.toString must be_==(addressAsString)
    }
  }
}