package view.gui

import model.ViewModel

class GameStats(model: ViewModel) extends HorizontalBoxPanel("GameStats", 300, 220) {
  add(new LeftAlignedValueText(model, "Current Player:", "currentPlayerToken"))
  add(new LeftAlignedValueText(model, "Total Tokens:", "totalTokens"))
  add(new LeftAlignedValueText(model, "Dead Tokens:", "deadTokens"))
  add(new LeftAlignedValueText(model, "Moves remaining:", "movesRemaining"))
  add(new LeftAlignedValueText(model, "Lifes:", "lifes", false))
  add(new LeftAlignedValueText(model, "Coins Collected:", "coins", false))
  add(new LeftAlignedValueText(model, "Score:", "score", false))
  add(new LeftAlignedValueText(model, "PowerUp Time", "powerUp", false))
  add(new LeftAlignedValueText(model, "Frozen Time:", "frozenTime"))
}