package com.jahndis.markerninja.game;

import android.graphics.Color;
import android.graphics.Rect;

import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.gameobject.framework.Collidable;
import com.jahndis.whalebot.gameobject.framework.Paintable;

public class Wall implements Paintable, Collidable {
  
  public int x;
  public int y;
  public int width;
  public int height;
  private Rect collisionMask;
  
  public Wall(int x, int y) {
    this.x = x;
    this.y = y;
    this.width = 100;
    this.height = 100;
    this.collisionMask = new Rect(x, y, x + width, y + height);
  }

  @Override
  public void paint(Graphics g) {
    g.drawRect(x + 1, y + 1, width - 1, height - 1, Color.RED);
  }

  @Override
  public boolean hasCollision(Collidable other) {
    return Rect.intersects(collisionMask, other.getCollisionMask());
  }

  @Override
  public Rect getCollisionMask() {
    return collisionMask;
  }

  @Override
  public void respondToCollision(Collidable other) {
    
  }

}
