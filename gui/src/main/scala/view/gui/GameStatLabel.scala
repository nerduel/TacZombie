package taczombie.client.view.gui

import scala.swing.Alignment
import scala.swing.BorderPanel
import scala.swing.Label

import taczombie.client.model.ViewModel
import taczombie.client.util.Observer

class GameStatLabel(model: ViewModel, text: String,
  property: String, visibleWhenZombie: Boolean = true)
  extends BorderPanel with Observer {

  model.add(GameStatLabel.this)

  private var labelText = new Label(text.padTo(18, ' ')) {
    horizontalTextPosition = Alignment.Left
  }

  private var labelValue = new Label(value.reverse.padTo(5, ' ').reverse) {
    horizontalTextPosition = Alignment.Right
  }

  add(labelText, BorderPanel.Position.West)
  add(labelValue, BorderPanel.Position.East)

  def update {
    labelValue.text = value.reverse.padTo(5, ' ').reverse

    if (model.currentPlayerToken == "Zombie" && !visibleWhenZombie) {
      labelText.visible = false
      labelValue.visible = false
    } else {
      labelText.visible = true
      labelValue.visible = true
    }
  }

  private def value: String = {
    val mthd = model.getClass.getMethods.find(_.getName.equals(property)).get
    val value = mthd.invoke(model).toString()
    return value
  }
}