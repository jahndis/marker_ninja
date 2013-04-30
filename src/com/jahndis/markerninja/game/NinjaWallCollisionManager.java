package com.jahndis.markerninja.game;

import java.util.ArrayList;

import android.graphics.PointF;
import android.graphics.Rect;

import com.jahndis.markerninja.game.Ninja.NinjaState;
import com.jahndis.whalebot.collisions.CollisionManager;

public class NinjaWallCollisionManager extends CollisionManager<Ninja, Wall> {

  public NinjaWallCollisionManager(Ninja object, ArrayList<Wall> others) {
    super(object, others);
  }
  
  @Override
  public boolean check() {
    boolean hasCollision = false;
    boolean needToRecheckCollisions = false;
    
    do {
      hasCollision = false;
      needToRecheckCollisions = false;
      
      for (Wall other : getOthers()) {
        if (other != null) {
          if (objectInWall(other)) {
            onCollision(getObject(), other);
            hasCollision = true;
            needToRecheckCollisions = true;
            break;
          }
          
          if (objectAgainstTopOfWall(other)) {
            onCollisionDown(getObject(), other);
            hasCollision = true;
          }
          if (objectAgainstBottomOfWall(other)) {
            onCollisionUp(getObject(), other);
            hasCollision = true;
          }
          if (objectAgainstLeftSideOfWall(other)) {
            onCollisionRight(getObject(), other);
            hasCollision = true;
          }
          if (objectAgainstRightSideOfWall(other)) {
            onCollisionLeft(getObject(), other);
            hasCollision = true;
          }
        }
      }
    } while (needToRecheckCollisions);
    
    if (hasCollision) {
      return true;
    }
    
    onNoCollision(getObject());
    return false;
  }

  @Override
  public void onCollision(Ninja object, Wall other) {
    PointF moveOutVector = getCollisionResolutionVector(object, other, (float) (object.direction + Math.PI));
    object.move(moveOutVector.x, moveOutVector.y);
    object.speed = 0;
    
    object.state.remove(NinjaState.AGAINST_TOP);
    object.state.remove(NinjaState.AGAINST_BOTTOM);
    object.state.remove(NinjaState.AGAINST_RIGHT);
    object.state.remove(NinjaState.AGAINST_LEFT);
    
    object.state.remove(NinjaState.JUMPING);
  }
  
  public void onCollisionDown(Ninja object, Wall other) {
    object.state.add(NinjaState.AGAINST_BOTTOM);
    
    object.state.remove(NinjaState.FALLING);
    object.state.remove(NinjaState.WALL_SLIDING);
    if (!object.state.contains(NinjaState.SLIDING)) {
      object.state.add(NinjaState.STANDING);
    }
  }
  
  public void onCollisionUp(Ninja object, Wall other) {
    object.state.add(NinjaState.AGAINST_TOP);
  }
  
  public void onCollisionRight(Ninja object, Wall other) {
    object.state.add(NinjaState.AGAINST_RIGHT);
  }
  
  public void onCollisionLeft(Ninja object, Wall other) {
    object.state.add(NinjaState.AGAINST_LEFT);
  }

  @Override
  public void onNoCollision(Ninja object) {
    object.state.remove(NinjaState.AGAINST_BOTTOM);
    object.state.remove(NinjaState.AGAINST_TOP);
    object.state.remove(NinjaState.AGAINST_RIGHT);
    object.state.remove(NinjaState.AGAINST_LEFT);
    
    object.state.remove(NinjaState.STANDING);
    object.state.remove(NinjaState.WALL_SLIDING);
    object.state.remove(NinjaState.SLIDING);
  }
  
  
  /* Private methods */
  
  private boolean objectInWall(Wall other) {
    return Rect.intersects(getObject().getCollisionMask(), other.getCollisionMask());
  }
  
  private boolean objectAgainstTopOfWall(Wall other) {
    if (((getObject().getCollisionMask().left < other.getCollisionMask().right && getObject().getCollisionMask().left > other.getCollisionMask().left) || 
        (getObject().getCollisionMask().left + (getObject().width * 0.5) < other.getCollisionMask().right && getObject().getCollisionMask().left + (getObject().width * 0.5) > other.getCollisionMask().left) ||
        (getObject().getCollisionMask().right < other.getCollisionMask().right && getObject().getCollisionMask().right > other.getCollisionMask().left)) &&
        getObject().getCollisionMask().bottom == other.getCollisionMask().top) {
      return true;
    } else {
      return false;
    }
  }
  
  private boolean objectAgainstBottomOfWall(Wall other) {
    if (((getObject().getCollisionMask().left < other.getCollisionMask().right && getObject().getCollisionMask().left > other.getCollisionMask().left) || 
        (getObject().getCollisionMask().left + (getObject().width * 0.5) < other.getCollisionMask().right && getObject().getCollisionMask().left + (getObject().width * 0.5) > other.getCollisionMask().left) ||
        (getObject().getCollisionMask().right < other.getCollisionMask().right && getObject().getCollisionMask().right > other.getCollisionMask().left)) &&
        getObject().getCollisionMask().top == other.getCollisionMask().bottom) {
      return true;
    } else {
      return false;
    }
  }
  
  private boolean objectAgainstLeftSideOfWall(Wall other) {
    if (((getObject().getCollisionMask().top < other.getCollisionMask().bottom && getObject().getCollisionMask().top > other.getCollisionMask().top) || 
        (getObject().getCollisionMask().top + (getObject().height * 0.5) < other.getCollisionMask().bottom && getObject().getCollisionMask().top + (getObject().height * 0.5) > other.getCollisionMask().top) ||
        (getObject().getCollisionMask().bottom < other.getCollisionMask().bottom && getObject().getCollisionMask().bottom > other.getCollisionMask().top)) &&
        getObject().getCollisionMask().right == other.getCollisionMask().left) {
      return true;
    } else {
      return false;
    }
  }
  
  private boolean objectAgainstRightSideOfWall(Wall other) {
    if (((getObject().getCollisionMask().top < other.getCollisionMask().bottom && getObject().getCollisionMask().top > other.getCollisionMask().top) || 
        (getObject().getCollisionMask().top + (getObject().height * 0.5) < other.getCollisionMask().bottom && getObject().getCollisionMask().top + (getObject().height * 0.5) > other.getCollisionMask().top) ||
        (getObject().getCollisionMask().bottom < other.getCollisionMask().bottom && getObject().getCollisionMask().bottom > other.getCollisionMask().top)) &&
        getObject().getCollisionMask().left == other.getCollisionMask().right) {
      return true;
    } else {
      return false;
    }
  }

}
