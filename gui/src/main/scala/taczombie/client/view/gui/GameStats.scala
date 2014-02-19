package taczombie.client.view.gui

import taczombie.client.model.ViewModel

class GameStats(model: ViewModel) extends HorizontalBoxPanel("GameStats", 300, 220) {
  add(new GameStatLabel(model, "Current Player:", "currentPlayerToken"))
  add(new GameStatLabel(model, "Total Tokens:", "totalTokens"))
  add(new GameStatLabel(model, "Dead Tokens:", "deadTokens"))
  add(new GameStatLabel(model, "Moves remaining:", "movesRemaining"))
  add(new GameStatLabel(model, "Lifes:", "lifes", false))
  add(new GameStatLabel(model, "Coins Collected:", "coins", false))
  add(new GameStatLabel(model, "Score:", "score", false))
  add(new GameStatLabel(model, "PowerUp Time", "powerUp", false))
  add(new GameStatLabel(model, "Frozen Time:", "frozenTime"))
}