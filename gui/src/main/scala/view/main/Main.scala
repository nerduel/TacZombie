package view.main

import view.gui.Gui
import view.tui.Tui
import com.google.inject.AbstractModule
import com.google.inject.Guice

class UiModule(ui: Array[String]) extends AbstractModule {
  def configure {
    ui.toList match {
      case "tui" :: Nil => bind(classOf[IView]).to(classOf[Tui])
      case _ => bind(classOf[IView]).to(classOf[Gui])
    }
  }
}

object Main {
  def main(args: Array[String]) {
    var restart = true
    val viewInjector = Guice.createInjector(new UiModule(args))

    while (restart) {
      val view = viewInjector.getInstance(classOf[IView])
      view.open
      restart = view.runBlocking
    }
  }
}
