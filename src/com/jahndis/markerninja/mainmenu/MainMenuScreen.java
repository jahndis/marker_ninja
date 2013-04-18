package com.jahndis.markerninja.mainmenu;

import java.util.List;

import android.graphics.Color;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.framework.Input.TouchEvent;
import com.jahndis.whalebot.framework.Screen;
import com.jahndis.whalebot.gameobject.Button;

public class MainMenuScreen extends Screen {
  
  private Button startButton;
  private Button quitButton;
  
  public MainMenuScreen(Game game) {
    super(game);
    
    startButton = new StartButton(game, "Start");
    quitButton = new QuitButton(game, "Quit");
  }
  
  @Override
  public void update(float deltaTime) {
    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
    
    int len = touchEvents.size();
    for (int i = 0; i < len; i++) {
      TouchEvent event = touchEvents.get(i);
      
      startButton.respondToTouchEvent(event);
      quitButton.respondToTouchEvent(event);
    }
  }
  
  @Override
  public void paint(float deltaTime) {
    Graphics g = game.getGraphics();
    
    g.clearScreen(Color.BLACK);
    
    startButton.paint(g);
    quitButton.paint(g);
  }
  
  @Override
  public void pause() {
    
  }
  
  @Override
  public void resume() {
    
  }
  
  @Override
  public void dispose() {
    
  }
  
  @Override
  public void backButton() {
    game.exit();
  }

}
