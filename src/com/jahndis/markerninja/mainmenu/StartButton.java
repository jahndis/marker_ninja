package com.jahndis.markerninja.mainmenu;

import com.jahndis.markerninja.game.GameScreen;
import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Input.TouchEvent;

public class StartButton extends MainMenuButton {
  
  public StartButton(Game game, String label) {
    super(game, label, game.getGraphics().getWidth() / 2 - 150, 500);
  }
  
  @Override
  public void onRelease(TouchEvent event) {
    game.setScreen(new GameScreen(game));
  }

}
