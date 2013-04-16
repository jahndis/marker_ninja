package com.jahndis.markerninja.mainmenu;

import android.graphics.Color;
import android.graphics.Paint;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.gameobject.Button;
import com.jahndis.whalebot.framework.Input.TouchEvent;

public class QuitButton extends Button {
  
  public QuitButton(Game game) {
    super(game);
    x = game.getGraphics().getWidth() / 2 - 150;
    y = 300 + 100 * 2;
    width = 300;
    height = 100;
  }
  
  @Override
  public void onPress(TouchEvent event) {
    
  }
  
  @Override
  public void onDrag(TouchEvent event) {
    
  }
  
  @Override
  public void onHold(TouchEvent event) {
    
  }
  
  @Override
  public void onRelease(TouchEvent event) {
    game.exit();
  }
  
  @Override
  public void paint(Graphics g) {
    if (pressed) {
      g.drawRect(x, y, width, height, Color.RED);
    } else {
      g.drawRect(x, y, width, height, Color.BLUE);
    }
    
    Paint stringPaint = new Paint();
    stringPaint.setTextAlign(Paint.Align.CENTER);
    stringPaint.setTextSize(32);
    stringPaint.setColor(Color.WHITE);
    g.drawString("Quit", x + width / 2, y + height / 2, stringPaint);
  }

}
