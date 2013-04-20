package com.jahndis.markerninja.game;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.framework.Input.TouchEvent;
import com.jahndis.whalebot.gameobject.framework.Collidable;
import com.jahndis.whalebot.gameobject.framework.Paintable;
import com.jahndis.whalebot.gameobject.framework.Touchable;
import com.jahndis.whalebot.gameobject.framework.Updateable;

public class Ninja implements Paintable, Updateable, Touchable, Collidable {
  
  private final static int JUMP_VELOCITY_THRESHOLD = 30;
  private final static int JUMP_SPEED = 10;
  
  enum NinjaState {
    STANDING, JUMPING, CLINGING, MARKING, THROWING, INTERACTING, HIDING, CAUGHT, WINNING
  }
  
  public int x;
  public int y;
  public int width;
  public int height;
  public float speed;
  public float direction;
  public NinjaState state;
  private Rect collisionMask;
  
  private PointF touchStart;
  private PointF touchDragged;
  private PointF touchEnd;
  private int touchTime;
  private float touchVelocity;
  private float touchDirection;
  
  public Ninja(int x, int y) {
    this.x = x;
    this.y = y;
    this.width = 50;
    this.height = 50;
    
    state = NinjaState.STANDING;
    collisionMask = new Rect(x, y, x + width, y + height);
    
    touchStart = new PointF(0, 0);
    touchDragged = new PointF(0, 0);
    touchEnd = new PointF(0, 0);
    touchTime = 0;
    touchVelocity = 0;
    touchDirection = 0;
  }
  
  @Override
  public void update(float deltaTime) {
    switch (state) {
    case CAUGHT:
      break;
    case CLINGING:
      break;
    case HIDING:
      break;
    case INTERACTING:
      break;
    case JUMPING:
      break;
    case MARKING:
      break;
    case STANDING:
      break;
    case THROWING:
      break;
    case WINNING:
      break;
    }
    
    x += Math.cos(direction) * speed * deltaTime;
    y -= Math.sin(direction) * speed * deltaTime;
    
    collisionMask.left = x;
    collisionMask.right = x + width;
    collisionMask.top = y;
    collisionMask.bottom = y + height;
  }

  @Override
  public void paint(Graphics g) {
    g.drawRect(x, y, width, height, Color.GREEN);
  }

  @Override
  public void respondToTouchEvent(TouchEvent event) {
    
    switch (event.type) {
    case TOUCH_DOWN:
      touchStart.x = event.x;
      touchStart.y = event.y;
      touchDragged.x = event.x;
      touchDragged.y = event.y;
      touchTime = 1;
      break;
    case TOUCH_DRAGGED:
    case TOUCH_HOLD:
      touchDragged.x = event.x;
      touchDragged.y = event.y;
      touchTime += 1;
      break;
    case TOUCH_UP:
      touchEnd.x = event.x;
      touchEnd.y = event.y;
      touchVelocity = findTouchVelocity(touchStart, touchEnd, touchTime);
      touchDirection = findTouchDirection(touchStart, touchEnd);
      if (touchVelocity > Ninja.JUMP_VELOCITY_THRESHOLD) {
        jump(touchDirection);
      }
      break;
    }
    
  }
  
  public void jump(float direction) {
    Log.i("Ninja Jump", "HIYAA! in direction: " + direction);
    this.state = NinjaState.JUMPING;
    this.speed = Ninja.JUMP_SPEED;
    this.direction = direction;
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
    if (other instanceof Wall) {
      Log.i("Ninja Collision", "With wall");
      moveOutOfCollision((Wall) other);
      this.speed = 0;
      this.state = NinjaState.CLINGING;
    }
  }
  
  private void moveOutOfCollision(Wall wall) {
    // Get overlap (intersection) of collision masks
    Rect intersect = new Rect();
    intersect.setIntersect(collisionMask, wall.getCollisionMask());
    Rect overlap = new Rect(0, intersect.height(), intersect.width(), 0);
    
    // Get the direction of the vector to move out of collision
    float moveOutX = 0;
    float moveOutY = 0;
    float moveOutDirection = direction;
    if (moveOutDirection >= Math.PI) {
      moveOutDirection -= Math.PI;
    } else {
      moveOutDirection += Math.PI;
    }
    
    // Variables for line equation y = px + q
    float p = (float) Math.tan(moveOutDirection);
    float q;
    float x;
    float y;
    
    if (moveOutDirection >= 0 && moveOutDirection < Math.PI * 0.5) {
      // Move out direction is up and to the right
      x = overlap.left;
      y = overlap.bottom;
      q = y - (p * x);
      
      // Find the intersect of the move out direction that lies on the overlap rectangle
      if ((p * overlap.right + q) <= overlap.top) {
        // Intersection with right side
        moveOutX = overlap.right;
        moveOutY = -(p * overlap.right + q);
      } else if ((overlap.top - q) / p <= overlap.right) {
        // Intersection with top side
        moveOutX = (overlap.top - q) / p;
        moveOutY = -overlap.top;
      }
    } else if (moveOutDirection >= Math.PI * 0.5 && moveOutDirection < Math.PI) {
      // Move out direction is up and to the left
      x = overlap.right;
      y = overlap.bottom;
      q = y - (p * x);
      
      // Find the intersect of the move out direction that lies on the overlap rectangle
      if ((p * overlap.left + q) <= overlap.top) {
        // Intersection with left side
        moveOutX = -(overlap.right - overlap.left);
        moveOutY = -(p * overlap.left + q);
      } else if ((overlap.top - q) / p >= overlap.left) {
        // Intersection with the top side
        moveOutX = -(overlap.right - ((overlap.top - q) / p));
        moveOutY = -overlap.top;
      }
    } else if (moveOutDirection >= Math.PI && moveOutDirection < Math.PI * 1.5) {
      // Move out direction is down and to the left
      x = overlap.right;
      y = overlap.top;
      q = y - (p * x);
      
      // Find the intersect of the move out direction that lies on the overlap rectangle
      if ((p * overlap.left + q) >= overlap.bottom) {
        // Intersection with left side
        moveOutX = -(overlap.right - overlap.left);
        moveOutY = overlap.top - (p * overlap.left + q);
      } else if ((overlap.bottom - q) / p >= overlap.left) {
        // Intersection with bottom side
        moveOutX = -(overlap.right - ((overlap.bottom - q) / p));
        moveOutY = overlap.top - overlap.bottom;
      }
    } else if (moveOutDirection >= Math.PI * 1.5 && moveOutDirection < Math.PI * 2) {
      // Move out direction is down and to the right
      x = overlap.left;
      y = overlap.top;
      q = y - (p * x);
      
      // Find the intersect of the move out direction that lies on the overlap rectangle
      if ((p * overlap.right + q) >= overlap.bottom) {
        // Intersection with right side
        moveOutX = overlap.right;
        moveOutY = overlap.top - (p * overlap.right + q);
      } else if ((overlap.bottom - q) / p <= overlap.right) {
        // Intersection with bottom side
        moveOutX = (overlap.bottom - q) / p;
        moveOutY = overlap.top - overlap.bottom;
      }
    } 
    
    this.x += Math.ceil(moveOutX);
    this.y += Math.ceil(moveOutY);

    collisionMask.left = this.x;
    collisionMask.right = this.x + this.width;
    collisionMask.top = this.y;
    collisionMask.bottom = this.y + this.height;
  }
  
  
  /* Private Methods */
  
  private static float findTouchVelocity(PointF touchStart, PointF touchEnd, int touchTime) {
    float touchDistance = PointF.length(touchEnd.x - touchStart.x, touchEnd.y - touchStart.y);
    
    if (touchTime <= 1 || touchDistance < 5) {
      return 0;
    } else {
      return  touchDistance / touchTime;
    }
  }
  
  private static float findTouchDirection(PointF touchStart, PointF touchEnd) {
    return (float) (Math.atan2((touchEnd.y - touchStart.y), -(touchEnd.x - touchStart.x)) + Math.PI);
  }

}
