package com.jahndis.markerninja.pausemenu;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Input.TouchEvent;

public class RestartButton extends PauseMenuButton {

  public RestartButton(Game game, String label) {
    super(game, label, game.getGraphics().getWidth() / 2 - 150, 500);
  }

  @Override
  public void onRelease(TouchEvent event) {
//    game.getCurrentScreen().restart();
  }

}
