package com.jahndis.markerninja.game;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.framework.Input.TouchEvent;
import com.jahndis.whalebot.gameobject.framework.Paintable;
import com.jahndis.whalebot.gameobject.framework.Touchable;
import com.jahndis.whalebot.gameobject.framework.Updateable;

public class Ninja implements Paintable, Updateable, Touchable {
  
  private final static int JUMP_VELOCITY_THRESHOLD = 30;
  private final static int JUMP_SPEED = 15;
  
  enum NinjaState {
    STANDING, JUMPING, CLINGING, MARKING, THROWING, INTERACTING, HIDING, CAUGHT, WINNING
  }
  
  public int x;
  public int y;
  public float speed;
  public float direction;
  public NinjaState state;
  
  private PointF touchStart;
  private PointF touchDragged;
  private PointF touchEnd;
  private int touchTime;
  private float touchVelocity;
  private float touchDirection;
  
  public Ninja(int x, int y) {
    this.x = x;
    this.y = y;
    state = NinjaState.STANDING;
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
      x += Math.cos(direction) * speed;
      y -= Math.sin(direction) * speed;
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
  }

  @Override
  public void paint(Graphics g) {
    g.drawRect(x, y, 50, 50, Color.GREEN);
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
