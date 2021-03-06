package com.jahndis.markerninja.pausemenu;

import com.jahndis.markerninja.mainmenu.MainMenuScreen;
import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Input.TouchEvent;

public class ExitButton extends PauseMenuButton {

  public ExitButton(Game game, String label) {
    super(game, label, game.getGraphics().getWidth() / 2 - 150, 700);
  }

  @Override
  public void onRelease(TouchEvent event) {
    game.getCurrentScreen().dispose();
    game.setScreen(new MainMenuScreen(game));
  }

}
