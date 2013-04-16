package com.jahndis.markerninja.mainmenu;

import java.util.List;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.framework.Input.TouchEvent;
import com.jahndis.whalebot.framework.Screen;

public class MainMenuScreen extends Screen {
  
  private StartButton startButton;
  private QuitButton quitButton;
  
  public MainMenuScreen(Game game) {
    super(game);
    
    startButton = new StartButton(game);
    quitButton = new QuitButton(game);
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
    //Display "Exit Game?" Box
  }

}
