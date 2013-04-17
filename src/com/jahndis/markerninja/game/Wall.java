package com.jahndis.markerninja.game;

import android.graphics.Color;

import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.gameobject.framework.Paintable;

public class Wall implements Paintable {
  
  public int x;
  public int y;
  public int width;
  public int height;
  
  public Wall(int x, int y) {
    this.x = x;
    this.y = y;
    this.width = 100;
    this.height = 100;
  }

  @Override
  public void paint(Graphics g) {
    g.drawRect(x, y, width, height, Color.RED);
  }

}
