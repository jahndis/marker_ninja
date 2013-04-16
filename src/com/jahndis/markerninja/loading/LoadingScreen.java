package com.jahndis.markerninja.loading;

import com.jahndis.markerninja.Assets;
import com.jahndis.markerninja.mainmenu.MainMenuScreen;
import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Screen;

public class LoadingScreen extends Screen {
  
  public LoadingScreen(Game game) {
    super(game);
  }
  
  @Override
  public void update(float deltaTime) {
    Assets.load();
    game.setScreen(new MainMenuScreen(game));
  }
  
  @Override
  public void paint(float deltaTime) {

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
