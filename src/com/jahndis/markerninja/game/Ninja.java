package com.jahndis.markerninja.game;

import java.util.HashSet;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.jahndis.markerninja.game.SlideSpot.SlideDirection;
import com.jahndis.whalebot.collisions.CollisionManager;
import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.framework.Input.TouchEvent;
import com.jahndis.whalebot.gameobject.GameObject;
import com.jahndis.whalebot.gameobject.framework.Collidable;
import com.jahndis.whalebot.gameobject.framework.Paintable;
import com.jahndis.whalebot.gameobject.framework.Touchable;
import com.jahndis.whalebot.gameobject.framework.Updateable;

public class Ninja extends GameObject implements Paintable, Updateable, Touchable, Collidable {
  
  private final static int TOUCH_VELOCITY_THRESHOLD = 20;
  private final static int JUMP_SPEED = 20;
  private final static int SLIDE_SPEED = 10;
  private final static int FALL_SPEED = 10;
  private final static int THROW_TOUCH_TIME = 20;
  private final static int MAX_THROW_SPEED = 80;
  private final static int MIN_THROW_SPEED = 10;
  
  enum NinjaState {
    AGAINST_TOP, AGAINST_BOTTOM, AGAINST_LEFT, AGAINST_RIGHT, 
    STANDING, JUMPING, CLINGING, FALLING, HIDING,
    CAN_SLIDE_LEFT, CAN_SLIDE_RIGHT, SLIDING, 
    MARKING, THROWING, INTERACTING, 
    CAUGHT, WINNING
  }
  
  public float speed;
  public float direction;
  public float throwDirection;
  public float throwSpeed;
  public HashSet<NinjaState> state;
  
  private Rect collisionMask;
  
  private boolean touchDown;
  private PointF touchStart;
  private PointF touchDragged;
  private PointF touchEnd;
  private int touchTime;
  private float touchVelocity;
  private float touchDirection;
  
  public Ninja(Game game, int x, int y) {
    super(game, x, y, 50, 50);
    
    speed = 0;
    direction = 0;
    throwDirection = 0;
    throwSpeed = 0;
    state = new HashSet<NinjaState>();
    state.add(NinjaState.FALLING);
    
    touchDown = false;
    touchStart = new PointF(0, 0);
    touchDragged = new PointF(0, 0);
    touchEnd = new PointF(0, 0);
    touchTime = 0;
    touchVelocity = 0;
    touchDirection = 0;
  }
  
  @Override
  public void update(float deltaTime) { 
    if (!state.contains(NinjaState.AGAINST_BOTTOM) && !state.contains(NinjaState.JUMPING)) {
      state.add(NinjaState.FALLING);
    }
    
    if (state.contains(NinjaState.FALLING)) {
      speed = FALL_SPEED;
      direction = (float) (Math.PI * 1.5);
    }
    
    move((float) Math.cos(direction) * speed * deltaTime, (float) -(Math.sin(direction) * speed * deltaTime));
    
    if (touchDown) {
      touchTime += 1;
      
      if (touchTime >= THROW_TOUCH_TIME && 
          PointF.length(x + (width * 0.5f) - touchStart.x, y + (height * 0.5f) - touchStart.y) < width &&
          PointF.length(x + (width * 0.5f) - touchDragged.x, y + (height * 0.5f) - touchDragged.y) < width) {
        state.add(NinjaState.THROWING);
      }
    }
  }

  @Override
  public void paint(Graphics g) {
    if (state.contains(NinjaState.THROWING)) {
      g.drawCircle((int) (x + width * 0.5), (int) (y + height * 0.5), 40, Color.YELLOW);
      
      // Draw throwing arc
      int x, y = 0;
      float t;
      for (int i = 0; i < 10; i++) {
        t = i;
        x = (int) ((this.x + this.width * 0.5) + (Math.cos(this.throwDirection) * this.throwSpeed * t)); 
        y = (int) ((this.y + this.height * 0.5) + (0.5 * FALL_SPEED * t * t) + (-Math.sin(this.throwDirection) * this.throwSpeed * t));
        
        g.drawCircle(x, y, (int) ((10 - i) * 0.3) + 1, Color.YELLOW);
      }
    }
    
    if (state.contains(NinjaState.SLIDING)) {
      g.drawRect(x, (int) (y + width * 0.5), width, (int) (height * 0.5), Color.GREEN);
    } else {
      g.drawRect(x, y, width, height, Color.GREEN);
    }
  }

  @Override
  public void respondToTouchEvent(TouchEvent event) {
    switch (event.type) {
    case TOUCH_DOWN:
      touchStart.set(event.x, event.y);
      touchDragged.set(event.x, event.y);
      touchTime = 1;
      throwDirection = (float) (Math.PI * 0.5);
      throwSpeed = MIN_THROW_SPEED;
      touchDown = true;
      break;
    case TOUCH_DRAGGED:
    case TOUCH_HOLD:
      touchDragged.set(event.x, event.y);
      if (state.contains(NinjaState.THROWING)) {
        throwDirection = (float) (findTouchDirection(touchStart, touchDragged) + Math.PI);
        throwSpeed = Math.max(Math.min((findTouchDistance(touchStart, touchDragged)), MAX_THROW_SPEED), MIN_THROW_SPEED);
      }
      break;
    case TOUCH_UP:
      touchEnd.set(event.x, event.y);
      touchVelocity = findTouchVelocity(touchStart, touchEnd, touchTime);
      touchDirection = findTouchDirection(touchStart, touchEnd);
      state.remove(NinjaState.THROWING);
      
      if (touchVelocity > Ninja.TOUCH_VELOCITY_THRESHOLD) {
        // Slide when able to slide
        if (state.contains(NinjaState.CAN_SLIDE_LEFT) && touchDirection >= Math.PI && touchDirection <= Math.PI * 1.5) {
          slide((float) Math.PI);
        } else if (state.contains(NinjaState.CAN_SLIDE_RIGHT) && touchDirection >= Math.PI * 1.5 && touchDirection <= Math.PI * 2) {
          slide(0);
        } 
        
        if (canJump(touchDirection)) {
          jump(touchDirection);
        }
      }
      
      touchDown = false;
      break;
    }
  }
  
  private void jump(float direction) {
//    state.remove(NinjaState.STANDING);
//    state.remove(NinjaState.AGAINST_LEFT);
//    state.remove(NinjaState.AGAINST_RIGHT);
//    state.remove(NinjaState.CAN_SLIDE_LEFT);
//    state.remove(NinjaState.CAN_SLIDE_RIGHT);
//    state.remove(NinjaState.FALLING);
    state.add(NinjaState.JUMPING);
    speed = Ninja.JUMP_SPEED;
    this.direction = direction;
  }
  
  private void slide(float direction) {
    state.remove(NinjaState.STANDING);
    state.remove(NinjaState.AGAINST_LEFT);
    state.remove(NinjaState.AGAINST_RIGHT);
    state.remove(NinjaState.CAN_SLIDE_LEFT);
    state.remove(NinjaState.CAN_SLIDE_RIGHT);
    state.add(NinjaState.SLIDING);
    speed = Ninja.SLIDE_SPEED;
    this.direction = direction;
  }
  
  @Override
  public Rect getCollisionMask() {
    if (collisionMask == null) {
      collisionMask = new Rect(x, y, x + width, y + height);
    }
    if (state.contains(NinjaState.SLIDING)) {
      collisionMask.set(x, (int) (y + height * 0.5), x + width, y + height);
    } else {
      collisionMask.set(x, y, x + width, y + height);
    }
    return collisionMask;
  }
  
  public void respondToCollisionWithWall(Wall other) {
//    PointF moveOutVector = CollisionManager.getCollisionResolutionVector(this, other, (float) (direction + Math.PI));
//    move(moveOutVector.x, moveOutVector.y);

//    if (onGround(other)) {
//      state.remove(NinjaState.JUMPING);
//      state.remove(NinjaState.FALLING);
//      state.add(NinjaState.STANDING);
//      speed = 0;
//    } else {
//      state.remove(NinjaState.JUMPING);
//      state.add(NinjaState.FALLING);
//    }
//    
//    if (!againstCeiling(other)) {
//      if (againstLeft(other)) {
//        state.add(NinjaState.AGAINST_LEFT);
//      } else if (againstRight(other)) {
//        state.add(NinjaState.AGAINST_RIGHT);
//      }
//    } else {
//      state.remove(NinjaState.AGAINST_LEFT);
//      state.remove(NinjaState.AGAINST_RIGHT);
//    }
  }
  
  public void respondToCollisionWithHidingSpot(HidingSpot other) {
//    if (!state.contains(NinjaState.HIDING) || 
//        state.contains(NinjaState.STANDING) || 
//        state.contains(NinjaState.CLINGING) || 
//        state.contains(NinjaState.FALLING)
//        ) { 
//      setPosition(other.getPosition());
//      speed = 0;
//      state.remove(NinjaState.JUMPING);
//      state.remove(NinjaState.FALLING);
//      state.remove(NinjaState.SLIDING);
//      state.add(NinjaState.HIDING);
//    }
  }
  
  public void respondToCollisionWithClingSpot(ClingSpot other) {
//    if (!state.contains(NinjaState.CLINGING) || state.contains(NinjaState.FALLING)) { 
//      setPosition(other.getPosition());
//      speed = 0;
//      state.remove(NinjaState.JUMPING);
//      state.remove(NinjaState.FALLING);
//      state.remove(NinjaState.SLIDING);
//      state.add(NinjaState.CLINGING);
//    }
  }
  
  public void respondToCollisionWithSlideSpot(SlideSpot other) {
//    if (state.contains(NinjaState.STANDING)) {
//      if (other.direction == SlideDirection.LEFT) { 
//        state.add(NinjaState.CAN_SLIDE_LEFT);
//      } else if (other.direction == SlideDirection.RIGHT) { 
//        state.add(NinjaState.CAN_SLIDE_RIGHT);
//      }  
//    } else if (state.contains(NinjaState.SLIDING)){
//      if ((Float.compare(direction, (float) Math.PI) == 0 && other.direction == SlideDirection.RIGHT && x <= other.x) ||
//          (Float.compare(direction, 0) == 0 && other.direction == SlideDirection.LEFT && x >= other.x)) {
//        x = other.x;
//        state.remove(NinjaState.SLIDING);
//        state.add(NinjaState.STANDING);
//        speed = 0;
//      }
//    }
  }

  public void respondToNoCollisionWithHidingSpot() {
//    state.remove(NinjaState.HIDING);
  }

  public void respondToNoCollisionWithClingingSpot() {
//    state.remove(NinjaState.CLINGING);
  }
  
  
  /* Private Methods */
  
  private boolean canJump(float direction) {
    Log.i("Ninja", state.toString());
    if (state.isEmpty()) {
      return true;
    }
    
    if (state.contains(NinjaState.STANDING) || state.contains(NinjaState.CLINGING) || state.contains(NinjaState.HIDING)) {
      if (state.contains(NinjaState.AGAINST_BOTTOM)) {
        if (Math.sin(direction) < 0) {
          return false;
        }
      }
      if (state.contains(NinjaState.AGAINST_TOP)) {
        if (Math.sin(direction) > 0) {
          return false;
        }
      }
      if (state.contains(NinjaState.AGAINST_LEFT)) {
        if (Math.cos(direction) < 0) {
          return false;
        }
      }
      if (state.contains(NinjaState.AGAINST_RIGHT)) {
        if (Math.cos(direction) > 0) {
          return false;
        }
      }
      
      return true;
    } else {
      return false;
    } 
  }
  
  private static float findTouchDistance(PointF touchStart, PointF touchEnd) {
    return PointF.length(touchEnd.x - touchStart.x, touchEnd.y - touchStart.y);
  }
  
  private static float findTouchVelocity(PointF touchStart, PointF touchEnd, int touchTime) {
    float touchDistance = findTouchDistance(touchStart, touchEnd);
    
    if (touchTime <= 1 || touchDistance < 5) {
      // Return 0 velocity for a tap
      return 0;
    } else {
      return touchDistance / touchTime;
    }
  }
  
  private static float findTouchDirection(PointF touchStart, PointF touchEnd) {
    return (float) (Math.atan2((touchEnd.y - touchStart.y), -(touchEnd.x - touchStart.x)) + Math.PI);
  }
  
//  private boolean onGround(Wall wall) {
//    if (((getCollisionMask().left <= wall.getCollisionMask().right && getCollisionMask().left >= wall.getCollisionMask().left) || 
//        (getCollisionMask().right <= wall.getCollisionMask().right && getCollisionMask().right >= wall.getCollisionMask().left)) &&
//        Float.compare(getCollisionMask().bottom, wall.getCollisionMask().top) == 0) {
//      return true;
//    } else {
//      return false;
//    }
//  }
  
//  private boolean againstCeiling(Wall wall) {
//    if (((getCollisionMask().left <= wall.getCollisionMask().right && getCollisionMask().left >= wall.getCollisionMask().left) || 
//        (getCollisionMask().right <= wall.getCollisionMask().right && getCollisionMask().right >= wall.getCollisionMask().left)) &&
//        Float.compare(getCollisionMask().top, wall.getCollisionMask().bottom) == 0) {
//      return true;
//    } else {
//      return false;
//    }
//  }
  
//  private boolean againstLeft(Wall wall) {
//    if (((getCollisionMask().top <= wall.getCollisionMask().bottom && getCollisionMask().top >= wall.getCollisionMask().top) || 
//        (getCollisionMask().bottom <= wall.getCollisionMask().bottom && getCollisionMask().bottom >= wall.getCollisionMask().top)) &&
//        Float.compare(getCollisionMask().left, wall.getCollisionMask().right) == 0) {
//      return true;
//    } else {
//      return false;
//    }
//  }
  
//  private boolean againstRight(Wall wall) {
//    if (((getCollisionMask().top <= wall.getCollisionMask().bottom && getCollisionMask().top >= wall.getCollisionMask().top) || 
//        (getCollisionMask().bottom <= wall.getCollisionMask().bottom && getCollisionMask().bottom >= wall.getCollisionMask().top)) &&
//        Float.compare(getCollisionMask().right, wall.getCollisionMask().left) == 0) {
//      return true;
//    } else {
//      return false;
//    }
//  }

}
