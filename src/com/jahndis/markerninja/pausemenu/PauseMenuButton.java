package com.jahndis.markerninja.pausemenu;

import android.graphics.Color;
import android.graphics.Paint;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.framework.Input.TouchEvent;
import com.jahndis.whalebot.gameobject.Button;

public class PauseMenuButton extends Button {
  
  private String label;
  private Paint stringPaint;
  
  public PauseMenuButton(Game game, String label, int x, int y) {
    super(game, x, y, 300, 100);
    this.label = label;
    
    stringPaint = new Paint();
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
