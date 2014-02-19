package view.main

import view.gui.Gui
import view.tui.Tui

class Main extends {
  var view: IView = null

  def setView(view: IView) = {
    this.view = view
  }

  def show {
    view.open
  }

  def reconnect {
    if (view != null) {
      view.dispose
    }
    show
  }
}

object Main {
  def main(args: Array[String]) {
    val main = new Main
    var ui = args.toList match {
      case "tui" :: Nil => new Tui(main)
      case _ => new Gui(main)
    }
    main.setView(ui)
    main.show
  }
}
