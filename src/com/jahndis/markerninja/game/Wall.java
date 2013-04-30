package com.jahndis.markerninja.game;

import android.graphics.Color;
import android.graphics.Rect;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.gameobject.GameObject;
import com.jahndis.whalebot.gameobject.framework.Collidable;
import com.jahndis.whalebot.gameobject.framework.Paintable;

public class Wall extends GameObject implements Paintable, Collidable {

  private Rect collisionMask;
  
  public Wall(Game game, int x, int y) {
    super(game, x, y, 50, 50);
    this.collisionMask = new Rect(x, y, x + width, y + height);
  }

  @Override
  public void paint(Graphics g) {
    g.drawRect(x + 1, y + 1, width - 2, height - 2, Color.RED);
  }

  @Override
  public Rect getCollisionMask() {
    return collisionMask;
  }

}
