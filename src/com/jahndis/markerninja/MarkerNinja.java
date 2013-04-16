package com.jahndis.markerninja;

import com.jahndis.markerninja.loading.LoadingScreen;
import com.jahndis.whalebot.framework.Screen;
import com.jahndis.whalebot.framework.implementation.AndroidGame;

public class MarkerNinja extends AndroidGame {
  
  @Override
  public Screen getInitScreen() {
    return new LoadingScreen(this);
  }
  
  @Override
  public void onBackPressed() {
    getCurrentScreen().backButton();
  }

}
