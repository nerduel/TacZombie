package taczombie.client.view.gui

import java.util.concurrent.Callable
import java.util.concurrent.FutureTask

import taczombie.client.controller.ViewController
import taczombie.client.model.ViewModel
import taczombie.client.util.Address
import taczombie.client.util.Observer
import taczombie.client.view.main.IView

class Gui extends swing.Frame with Observer with IView {
  title = "TacZombie"
  iconImage = java.awt.Toolkit.getDefaultToolkit.getImage(getClass.getResource("/images/zombie.png"))

  var address: Address = askForAddress
  var model: ViewModel = new ViewModel
  model.add(this)
  var controller: ViewController = new ViewController(model, this, address.toString)

  val ret = controller.connect
  if (!ret.isEmpty) {
    contents = new ConnectError(this, ret)
  }

  override def closeOperation() {
    controller.disconnect
    continue = false
    dispose
    sys.exit(0)
  }

  def update {
    if (model.cmd == "all") {
      val gameUI = new GameUI(this, model, controller)
      contents = gameUI

      // Most stupid fix to get key inputs working.
      gameUI.update
    }
  }

  val future = new FutureTask[Boolean](new Callable[Boolean]() {
    def call(): Boolean = {
      while (continue) {
        Thread.sleep(1000)
      }
      return restart
    }
  })

  def runBlocking: Boolean = {
    pool.submit(future)

    // Wait for future to return.
    val ret = future.get
    controller.close
    pool.shutdownNow()
    return ret
  }

  def stop {
    continue = false
    restart = true
    dispose
  }

  def askForAddress = new ConnectDialog().address.getOrElse(new Address("localhost", "9000"))

}