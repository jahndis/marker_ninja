package com.jahndis.markerninja.game;

import android.graphics.Color;
import android.graphics.Rect;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.gameobject.GameObject;
import com.jahndis.whalebot.gameobject.framework.Collidable;
import com.jahndis.whalebot.gameobject.framework.Paintable;

public class SlideSpot extends GameObject implements Paintable, Collidable {
  
  enum SlideDirection {
    RIGHT, LEFT
  }
  
  private Rect collisionMask;
  public SlideDirection direction;
  
  public SlideSpot(Game game, SlideDirection direction, int x, int y) {
    super(game, x, y, 50, 50);
    this.direction = direction;
    this.collisionMask = new Rect(x, y, x + width, y + height);
  }

  @Override
  public void paint(Graphics g) {
    g.drawRect(x + 1, y + 1, width - 2, height - 2, Color.CYAN);
    if (direction == SlideDirection.LEFT) { 
      g.drawLine(x, (int) (y + height * 0.5), x + width, (int) (y + height * 0.5), Color.BLACK);
      g.drawLine(x, (int) (y + height * 0.5), (int) (x + width * 0.5), y, Color.BLACK);
      g.drawLine(x, (int) (y + height * 0.5), (int) (x + width * 0.5), y + height, Color.BLACK);
    } else {
      g.drawLine(x, (int) (y + height * 0.5), x + width, (int) (y + height * 0.5), Color.BLACK);
      g.drawLine(x + width, (int) (y + height * 0.5), (int) (x + width * 0.5), y, Color.BLACK);
      g.drawLine(x + width, (int) (y + height * 0.5), (int) (x + width * 0.5), y + height, Color.BLACK);
    }
  }

  @Override
  public Rect getCollisionMask() {
    return collisionMask;
  }

}
