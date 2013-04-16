package com.jahndis.markerninja;

import java.util.List;

import com.jahndis.markerninja.mainmenu.MainMenuScreen;
import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.framework.Input.TouchEvent;
import com.jahndis.whalebot.framework.Input.TouchEvent.TouchEventType;
import com.jahndis.whalebot.framework.Screen;

import android.graphics.Color;
import android.graphics.Paint;

public class GameScreen extends Screen {
  
  enum GameState {
    Ready, Running, Paused, GameOver
  }
  
  GameState state = GameState.Ready;
  
  // Variable Setup
  // Create game objects here
  
  int livesLeft = 1;
  Paint paint;
  
  public GameScreen(Game game) {
    super(game);
    
    // Initialize game objects here
    
    // Defining a paint object
    paint = new Paint();
    paint.setTextSize(30);
    paint.setTextAlign(Paint.Align.CENTER);
    paint.setAntiAlias(true);
    paint.setColor(Color.WHITE);
  }
  
  @Override
  public void update(float deltaTime) {
    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
    
    switch (state) {
    case Ready:
      updateReady(touchEvents);
      break;
    case Running:
      updateRunning(touchEvents, deltaTime);
      break;
    case Paused:
      updatePaused(touchEvents);
      break;
    case GameOver:
      updateGameOver(touchEvents);
      break;
    }
  }
  
  private void updateReady(List<TouchEvent> touchEvents) {
    if (touchEvents.size() > 0) {
      state = GameState.Running;
    }
  }
  
  private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
    // 1. Handle touch input
    int len = touchEvents.size();
    for (int i = 0; i < len; i++ ) {
      TouchEvent event = touchEvents.get(i);
      
      if (event.type == TouchEventType.TOUCH_DOWN) {
        if (event.x < 640) {
          // Move left
        } else if (event.x > 640) {
          // Move right
        }
      }
      
      if (event.type == TouchEventType.TOUCH_UP) {
        if (event.x < 640) {
          // Stop moving left
        } else if (event.x > 640) {
          // Stop moving right
        }
      }
    }
    
    // 2. Check miscellaneous events like death
    if (livesLeft == 0) {
      state = GameState.GameOver;
    }
    
    // 3. Call individual update method here
  }
  
  private void updatePaused(List<TouchEvent> touchEvents) {
    int len = touchEvents.size();
    for (int i = 0; i < len; i++) {
      TouchEvent event = touchEvents.get(i);
      if (event.type == TouchEventType.TOUCH_UP) {
        
      }
    }
  }
  
  private void updateGameOver(List<TouchEvent> touchEvents) {
    int len = touchEvents.size();
    for(int i = 0; i < len; i++) {
      TouchEvent event = touchEvents.get(i);
      if (event.type == TouchEventType.TOUCH_UP) {
        if (event.x > 300 && event.x < 980 && event.y > 100 && event.y < 500) {
          nullify();
          game.setScreen(new MainMenuScreen(game));
          return;
        }
      }
    }
  }
  
  private void nullify() {
    paint = null;
    System.gc();
  }
  
  @Override
  public void paint(float deltaTime) {
//    Graphics g = game.getGraphics();
    
    // 1. Draw game elements
    
    // 2. Draw UI elements above the game
    switch (state) {
    case Ready:
      drawReadyUI();
      break;
    case Running:
      drawRunningUI();
      break;
    case Paused:
      drawPausedUI();
      break;
    case GameOver:
      drawGameOverUI();
      break;
    }
  }
  
  private void drawReadyUI() {
    Graphics g = game.getGraphics();
    
    g.drawARGB(155, 0, 0, 0);
    g.drawString("Tap each side of the screen to move in that direction.", 640, 300, paint);
  }
  
  private void drawRunningUI() {
//    Graphics g = game.getGraphics();
  }
  
  private void drawPausedUI() {
    Graphics g = game.getGraphics();
    // Darken the entire screen so you can display the Paused screen.
    g.drawARGB(155, 0, 0, 0);
  }
  
  private void drawGameOverUI() {
    Graphics g = game.getGraphics();
    g.drawRect(0, 0, 1281, 801, Color.BLACK);
    g.drawString("GAME OVER", 640, 300, paint);
  }
  
  @Override
  public void pause() {
    if (state == GameState.Running) {
      state = GameState.Paused;
    }
  }
  
  @Override
  public void resume() {
    
  }
  
  @Override
  public void dispose() {
    
  }
  
  @Override
  public void backButton() {
    pause();
  }

}
