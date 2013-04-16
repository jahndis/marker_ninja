package com.jahndis.markerninja.game;

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
    Ready, Running, Paused, ShowItems, Caught, LevelFail, LevelSuccess
  }
  GameState state = GameState.Ready;
  
  Paint paint;
  Ninja ninja;
  
  public GameScreen(Game game) {
    super(game);
    
    // Initialize game objects here
    ninja = new Ninja(game.getGraphics().getWidth()/2, game.getGraphics().getHeight()/2);
    
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
    case ShowItems:
      break;
    case Caught:
      break;
    case LevelFail:
      break;
    case LevelSuccess:
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
      
      ninja.respondToTouchEvent(event);
    }
    
    // 2. Check miscellaneous events like death
    
    // 3. Call individual update method here
    ninja.update(deltaTime);
  }
  
  private void updatePaused(List<TouchEvent> touchEvents) {
    int len = touchEvents.size();
    for (int i = 0; i < len; i++) {
      TouchEvent event = touchEvents.get(i);
      if (event.type == TouchEventType.TOUCH_UP) {
        
      }
    }
  }
  
  private void nullify() {
    paint = null;
    System.gc();
  }
  
  @Override
  public void paint(float deltaTime) {
    Graphics g = game.getGraphics();
    g.clearScreen(Color.BLACK);

    switch (state) {
    case Ready:
      drawReadyUI(g);
      break;
    case Running:
      drawRunningUI(g);
      break;
    case Caught:
      break;
    case LevelFail:
      break;
    case LevelSuccess:
      break;
    case Paused:
      break;
    case ShowItems:
      break;
    }
  }
  
  private void drawReadyUI(Graphics g) {
    g.drawARGB(155, 0, 0, 0);
    g.drawString("Tap to begin.", g.getWidth() / 2, 300, paint);
  }
  
  private void drawRunningUI(Graphics g) {
    ninja.paint(g);
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
//    pause();
    game.exit();
  }

}
