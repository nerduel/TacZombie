package view

import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.event.MouseClicked

import controller.Communication
import model.ViewModel

class GameCommands(model: ViewModel, controller: Communication) extends BorderPanel {
  
  val buttonUp = new Button("Up")
  val buttonDown = new Button("Down")
  val buttonLeft = new Button("Left")
  val buttonRight = new Button("Right")

  listenTo(buttonUp.mouse.clicks) 
  reactions += {
    case me: MouseClicked => controller.moveUp
  }  
  listenTo(buttonDown.mouse.clicks)
  reactions += {
    case me: MouseClicked => controller.moveDown
  }  
  listenTo(buttonLeft.mouse.clicks)
  reactions += {
    case me: MouseClicked => controller.moveLeft
  }  
  listenTo(buttonRight.mouse.clicks)
    reactions += {
    case me: MouseClicked => controller.moveRight
  }  
  
  add(buttonUp, BorderPanel.Position.North)
  add(buttonDown, BorderPanel.Position.South)
  add(buttonLeft, BorderPanel.Position.West)
  add(buttonRight, BorderPanel.Position.East)

}