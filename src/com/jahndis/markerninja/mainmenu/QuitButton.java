package com.jahndis.markerninja.mainmenu;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Input.TouchEvent;

public class QuitButton extends MainMenuButton {
  
  public QuitButton(Game game, String label) {
    super(game, label);
    x = game.getGraphics().getWidth() / 2 - 150;
    y = 700;
  }
  
  @Override
  public void onRelease(TouchEvent event) {
    game.exit();
  }
  
}
