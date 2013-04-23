package com.jahndis.markerninja.pausemenu;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Input.TouchEvent;

public class ResumeButton extends PauseMenuButton {

  public ResumeButton(Game game, String label) {
    super(game, label, game.getGraphics().getWidth() / 2 - 150, 300);
  }

  @Override
  public void onRelease(TouchEvent event) {
    game.getCurrentScreen().resume();
  }

}
