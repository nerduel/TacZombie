package taczombie.client.view.main

import taczombie.client.util.Address
import java.util.concurrent.Executors

trait IView {
  val pool = Executors.newFixedThreadPool(1);
  var restart: Boolean = false
  var continue: Boolean = true

  // Opens GUI
  def open

  // Block main thread.
  def runBlocking: Boolean

  // Stop gui/tui thread.
  def stop: Unit

  def askForAddress: Address
}