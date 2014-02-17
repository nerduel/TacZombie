package view.gui

import scala.swing._

class LeftAlignedValueText(text: String, value: String) extends BorderPanel {
  focusable = false
  private var labelText = new Label(text.padTo(18, ' ')) {
    horizontalTextPosition = Alignment.Left
  }

  private var labelValue = new Label(value.reverse.padTo(5, ' ').reverse) {
    horizontalTextPosition = Alignment.Right
  }

  def update(value: String) {
    labelValue.text = value.reverse.padTo(5, ' ').reverse
  }

  add(labelText, BorderPanel.Position.West)
  add(labelValue, BorderPanel.Position.East)
}