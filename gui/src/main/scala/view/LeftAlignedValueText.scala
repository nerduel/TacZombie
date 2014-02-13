package view

import scala.swing.Alignment
import scala.swing.FlowPanel
import scala.swing.Label

class LeftAlignedValueText(text: String, value : String) extends FlowPanel {  
    
    private var labelText = new Label(text) {
      horizontalAlignment = Alignment.Left
    }
    
    private var labelValue = new Label(value) {
      horizontalAlignment = Alignment.Left
    }
    
    def update(value: String) {
      labelValue.text = value
    }
    
    contents += labelText
    contents += labelValue
  }