package com.jahndis.markerninja.mainmenu;

import android.graphics.Color;
import android.graphics.Paint;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.framework.Input.TouchEvent;
import com.jahndis.whalebot.gameobject.Button;

public abstract class MainMenuButton extends Button {
  
  private String label;
  private Paint stringPaint;
  
  public MainMenuButton(Game game, String label) {
    super(game);
    this.label = label;
    width = 300;
    height = 100;
    
    Paint stringPaint = new Paint();
    stringPaint.setTextAlign(Paint.Align.CENTER);
    stringPaint.setTextSize(32);
    stringPaint.setColor(Color.WHITE);
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
    
  }
  
  @Override
  public void paint(Graphics g) {
    if (pressed) {
      g.drawRect(x, y, width, height, Color.RED);
    } else {
      g.drawRect(x, y, width, height, Color.BLUE);
    }
    
    g.drawString(label, x + width / 2, y + height / 2, stringPaint);
  }

}
