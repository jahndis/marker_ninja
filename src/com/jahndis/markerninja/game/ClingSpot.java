package com.jahndis.markerninja.game;

import java.util.Collection;

import android.graphics.Color;
import android.graphics.Rect;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.gameobject.GameObject;
import com.jahndis.whalebot.gameobject.framework.Collidable;
import com.jahndis.whalebot.gameobject.framework.Paintable;
import com.jahndis.whalebot.utils.CollisionHandler;

public class ClingSpot extends GameObject implements Paintable, Collidable {
  
  private Rect collisionMask;
  
  public ClingSpot(Game game, int x, int y) {
    super(game, x, y, 50, 50);
    this.collisionMask = new Rect(x, y, x + width, y + height);
  }

  @Override
  public void paint(Graphics g) {
    g.drawRect(x + 1, y + 1, width - 2, height - 2, Color.BLUE);
  }
  
  @Override
  public void checkForCollisions(Collection<? extends Collidable> others) {
    
  }

  @Override
  public boolean hasCollision(Collidable other) {
    return CollisionHandler.hasCollision(this, other);
  }

  @Override
  public Rect getCollisionMask() {
    return collisionMask;
  }

  @Override
  public void respondToCollision(Collidable other) {
    
  }

  @Override
  public void respondToNoCollision(Collidable otherClass) {
    
  }

}
