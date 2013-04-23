package com.jahndis.markerninja.game;

import java.util.Collection;
import java.util.HashSet;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.framework.Input.TouchEvent;
import com.jahndis.whalebot.gameobject.GameObject;
import com.jahndis.whalebot.gameobject.framework.Collidable;
import com.jahndis.whalebot.gameobject.framework.Paintable;
import com.jahndis.whalebot.gameobject.framework.Touchable;
import com.jahndis.whalebot.gameobject.framework.Updateable;
import com.jahndis.whalebot.utils.CollisionHandler;

public class Ninja extends GameObject implements Paintable, Updateable, Touchable, Collidable {
  
  private final static int JUMP_VELOCITY_THRESHOLD = 30;
  private final static int JUMP_SPEED = 20;
  private final static int FALL_SPEED = 10;
  
  enum NinjaState {
    STANDING, JUMPING, CLINGING, FALLING, MARKING, THROWING, INTERACTING, HIDING, CAUGHT, WINNING
  }
  
  public float speed;
  public float direction;
  public HashSet<NinjaState> state;
  
  private Rect collisionMask;
  public boolean hasCollisionWithAnyHidingSpot;
  
  private PointF touchStart;
  private PointF touchDragged;
  private PointF touchEnd;
  private int touchTime;
  private float touchVelocity;
  private float touchDirection;
  
  public Ninja(Game game, int x, int y) {
    super(game, x, y, 50, 50);
    
    state = new HashSet<NinjaState>();
    state.add(NinjaState.STANDING);
    
    touchStart = new PointF(0, 0);
    touchDragged = new PointF(0, 0);
    touchEnd = new PointF(0, 0);
    touchTime = 0;
    touchVelocity = 0;
    touchDirection = 0;
  }
  
  @Override
  public void update(float deltaTime) { 
    if (state.contains(NinjaState.FALLING)) {
      speed = FALL_SPEED;
      direction = (float) (Math.PI * 1.5);
    }
    
    move((float) Math.cos(direction) * speed * deltaTime, (float) -(Math.sin(direction) * speed * deltaTime));
  }

  @Override
  public void paint(Graphics g) {
    g.drawRect((int) x, (int) y, (int) width, (int) height, Color.GREEN);
  }

  @Override
  public void respondToTouchEvent(TouchEvent event) {
    switch (event.type) {
    case TOUCH_DOWN:
      touchStart.set(event.x, event.y);
      touchDragged.set(event.x, event.y);
      touchTime = 1;
      break;
    case TOUCH_DRAGGED:
    case TOUCH_HOLD:
      touchDragged.set(event.x, event.y);
      touchTime += 1;
      break;
    case TOUCH_UP:
      touchEnd.set(event.x, event.y);
      touchVelocity = findTouchVelocity(touchStart, touchEnd, touchTime);
      touchDirection = findTouchDirection(touchStart, touchEnd);
      
      if (touchVelocity > Ninja.JUMP_VELOCITY_THRESHOLD) {
        if (state.contains(NinjaState.STANDING) || 
            state.contains(NinjaState.CLINGING) || 
            state.contains(NinjaState.HIDING)) {
          jump(touchDirection);
        }
      }
      break;
    }
  }
  
  public void jump(float direction) {
    state.remove(NinjaState.STANDING);
    state.add(NinjaState.JUMPING);
    speed = Ninja.JUMP_SPEED;
    this.direction = direction;
  }
  
  @Override
  public void checkForCollisions(Collection<? extends Collidable> others) {
    CollisionHandler.checkForCollisions(this, others);
  }
  
  @Override
  public boolean hasCollision(Collidable other) {
    return CollisionHandler.hasCollision(this, other);
  }
  
  @Override
  public Rect getCollisionMask() {
    if (collisionMask == null) {
      collisionMask = new Rect(x, y, x + width, y + height);
    }
    collisionMask.set(x, y, x + width, y + height);
    return collisionMask;
  }
  
  @Override
  public void respondToCollision(Collidable other) {
    if (other instanceof Wall) {
      PointF moveOutVector = CollisionHandler.getCollisionResolutionVector(this, other, (float) (direction + Math.PI));
      move(moveOutVector.x, moveOutVector.y);
      if (onGround((Wall) other)) {
        state.remove(NinjaState.JUMPING);
        state.remove(NinjaState.FALLING);
        state.add(NinjaState.STANDING);
        speed = 0;
      } else {
        state.remove(NinjaState.JUMPING);
        state.add(NinjaState.FALLING);
      }
      
    } else if (other instanceof HidingSpot) {
      if (!state.contains(NinjaState.HIDING)) { 
        setPosition(((HidingSpot) other).getPosition());
        speed = 0;
        state.remove(NinjaState.JUMPING);
        state.remove(NinjaState.FALLING);
        state.add(NinjaState.HIDING);
      }
      
    } else if (other instanceof ClingSpot) {
      if (!state.contains(NinjaState.CLINGING)) { 
        setPosition(((ClingSpot) other).getPosition());
        speed = 0;
        state.remove(NinjaState.JUMPING);
        state.remove(NinjaState.FALLING);
        state.add(NinjaState.CLINGING);
      }
      
    }
  }
  
  @Override
  public void respondToNoCollision(Collidable other) {
    if (other instanceof HidingSpot) {
      state.remove(NinjaState.HIDING);
      
    } else if (other instanceof ClingSpot) {
      state.remove(NinjaState.CLINGING);
      
    }
  }
  
  
  /* Private Methods */
  
  private static float findTouchVelocity(PointF touchStart, PointF touchEnd, int touchTime) {
    float touchDistance = PointF.length(touchEnd.x - touchStart.x, touchEnd.y - touchStart.y);
    
    if (touchTime <= 1 || touchDistance < 5) {
      return 0;
    } else {
      return touchDistance / touchTime;
    }
  }
  
  private static float findTouchDirection(PointF touchStart, PointF touchEnd) {
    return (float) (Math.atan2((touchEnd.y - touchStart.y), -(touchEnd.x - touchStart.x)) + Math.PI);
  }
  
  private boolean onGround(Wall wall) {
    if ((getCollisionMask().left < wall.getCollisionMask().right || getCollisionMask().right > wall.getCollisionMask().left) &&
        Float.compare(getCollisionMask().bottom, wall.getCollisionMask().top) == 0 ) {
      return true;
    } else {
      return false;
    }
  }

}
