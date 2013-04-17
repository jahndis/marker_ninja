package com.jahndis.markerninja.pausemenu;

import android.graphics.Color;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.gameobject.framework.Paintable;

public class PauseMenu implements Paintable {
  
  protected Game game;
  
  public ResumeButton resumeButton;
  public RestartButton restartButton;
  public ExitButton exitButton;
  
  public PauseMenu(Game game) {
    this.game = game;
    resumeButton = new ResumeButton(game);
    restartButton = new RestartButton(game);
    exitButton = new ExitButton(game);
  }

  @Override
  public void paint(Graphics g) {
    g.drawARGB(155, 0, 0, 0);
    g.drawRect(g.getWidth() / 2 - 200, 200, 400, 700, Color.RED);
    
    resumeButton.paint(g);
    restartButton.paint(g);
    exitButton.paint(g);
  }

}
