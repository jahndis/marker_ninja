package com.jahndis.markerninja.game;

import java.util.ArrayList;
import java.util.List;

import com.jahndis.markerninja.pausemenu.PauseMenu;
import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.framework.Input.TouchEvent;
import com.jahndis.whalebot.framework.Screen;

import android.graphics.Color;
import android.graphics.Paint;

public class GameScreen extends Screen {
  
  enum GameState {
    Ready, Running, Paused, ShowItems, Caught, LevelFail, LevelSuccess
  }
  GameState state = GameState.Ready;
  
  // Game objects
  Ninja ninja;
  ArrayList<Wall> walls;
  ArrayList<HidingSpot> hidingSpots;
  ArrayList<ClingSpot> clingSpots;
  ArrayList<SlideSpot> slideSpots;
  
  // Collision managers
  NinjaWallCollisionManager ninjaWallCollision;
  NinjaHidingSpotCollisionManager ninjaHidingSpotCollision;
  NinjaClingSpotCollisionManager ninjaClingSpotCollision;
  NinjaSlideSpotCollisionManager ninjaSlideSpotCollision;
  
  // Other stuff
  Paint paint;
  PauseMenu pauseMenu;
  
  public GameScreen(Game game) {
    super(game);
    
    // Initialize game objects here
    ninja = new Ninja(game, 8 * 50, 12 * 50);
    
    walls = new ArrayList<Wall>();
    for (int i = 0; i < 8 * 2; i++) {
      for (int j = 0; j < 12 * 2; j++) {
        if (i == 0 || j == 0 || i == 8 * 2 - 1 || j == 12 * 2 - 1) {
          walls.add(new Wall(game, i * 50, j * 50));
        }
      }
    }
    walls.add(new Wall(game, 10 * 50, (12 * 2 - 4) * 50 - 30));
    walls.add(new Wall(game, 10 * 50, (12 * 2 - 3) * 50 - 30));
    walls.add(new Wall(game, 10 * 50, (12 * 2 - 2) * 50 - 30));
    walls.add(new Wall(game, 9 * 50, (12 * 2 - 4) * 50 - 30));
    walls.add(new Wall(game, 9 * 50, (12 * 2 - 3) * 50 - 30));
    walls.add(new Wall(game, 9 * 50, (12 * 2 - 2) * 50 - 30));
    
    walls.add(new Wall(game, 1 * 50, 10 * 50));
    walls.add(new Wall(game, 2 * 50, 10 * 50));
    walls.add(new Wall(game, 3 * 50, 10 * 50));
    walls.add(new Wall(game, 4 * 50, 10 * 50));
    walls.add(new Wall(game, 5 * 50, 10 * 50));
    walls.add(new Wall(game, 6 * 50, 10 * 50));
    walls.add(new Wall(game, 7 * 50, 10 * 50));
    walls.add(new Wall(game, 8 * 50, 10 * 50));
    
    hidingSpots = new ArrayList<HidingSpot>();
    hidingSpots.add(new HidingSpot(game, 3 * 50, (12 * 2 - 2) * 50));
    hidingSpots.add(new HidingSpot(game, (8 * 2 - 2) * 50, 9 * 50));
    
    clingSpots = new ArrayList<ClingSpot>();
    clingSpots.add(new ClingSpot(game, 7 * 50, 1 * 50));
    clingSpots.add(new ClingSpot(game, 1 * 50, 5 * 50));
    clingSpots.add(new ClingSpot(game, 1 * 50, 14 * 50));
    
    slideSpots = new ArrayList<SlideSpot>();
    slideSpots.add(new SlideSpot(game, SlideSpot.SlideDirection.RIGHT, 8 * 50, (12 * 2 - 2) * 50));
    slideSpots.add(new SlideSpot(game, SlideSpot.SlideDirection.LEFT, 11 * 50, (12 * 2 - 2) * 50));
    
    slideSpots.add(new SlideSpot(game, SlideSpot.SlideDirection.RIGHT, 5 * 50, 9 * 50));
    
    // Define the collision managers
    ninjaWallCollision = new NinjaWallCollisionManager(ninja, walls);
    ninjaHidingSpotCollision = new NinjaHidingSpotCollisionManager(ninja, hidingSpots);
    ninjaClingSpotCollision = new NinjaClingSpotCollisionManager(ninja, clingSpots);
    ninjaSlideSpotCollision = new NinjaSlideSpotCollisionManager(ninja, slideSpots);
    
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
    // Handle touch input
    int len = touchEvents.size();
    for (int i = 0; i < len; i++ ) {
      ninja.respondToTouchEvent(touchEvents.get(i));
    }
    
    // Update objects
    ninja.update(deltaTime);
    
    // Detect collisions and resolve
    ninjaWallCollision.check();
    ninjaHidingSpotCollision.check();
    ninjaClingSpotCollision.check();
    ninjaSlideSpotCollision.check();
  }
  
  private void updatePaused(List<TouchEvent> touchEvents) {
    int len = touchEvents.size();
    for (int i = 0; i < len; i++) {
      TouchEvent event = touchEvents.get(i);
      
      pauseMenu.resumeButton.respondToTouchEvent(event);
      pauseMenu.restartButton.respondToTouchEvent(event);
      pauseMenu.exitButton.respondToTouchEvent(event);
    }
  }
  
  @Override
  public void paint(float deltaTime) {
    Graphics g = game.getGraphics();

    switch (state) {
    case Ready:
      drawReadyUI(g);
      break;
    case Running:
      g.clearScreen(Color.BLACK);
      drawRunningUI(g);
      break;
    case Caught:
      break;
    case LevelFail:
      break;
    case LevelSuccess:
      break;
    case Paused:
      drawPausedUI(g);
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
    for (Wall w : walls) {
      w.paint(g);
    }
    
    for (ClingSpot c : clingSpots) {
      c.paint(g);
    }
    
    for (SlideSpot s : slideSpots) {
      s.paint(g);
    }
    
    ninja.paint(g);
    
    for (HidingSpot h : hidingSpots) {
      h.paint(g);
    }
  }
  
  private void drawPausedUI(Graphics g) {
    drawRunningUI(g);
    pauseMenu.paint(g);
  }
  
  @Override
  public void pause() {
    if (state == GameState.Running) {
      state = GameState.Paused;
      pauseMenu = new PauseMenu(game);
    }
  }
  
  @Override
  public void resume() {
    if (state == GameState.Paused) {
      state = GameState.Running;
    }
  }
  
  @Override
  public void dispose() {
    nullify();
  }
  
  private void nullify() {
    paint = null;
    System.gc();
  }
  
  @Override
  public void backButton() {
    switch (state) {
    case Caught:
      break;
    case LevelFail:
      break;
    case LevelSuccess:
      break;
    case Paused:
      resume();
      break;
    case Ready:
      break;
    case Running:
      pause();
      break;
    case ShowItems:
      break;
    }
  }

}
