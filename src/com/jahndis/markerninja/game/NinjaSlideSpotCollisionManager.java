package com.jahndis.markerninja.game;

import java.util.ArrayList;

import com.jahndis.markerninja.game.Ninja.NinjaState;
import com.jahndis.whalebot.collisions.CollisionManager;

public class NinjaSlideSpotCollisionManager extends CollisionManager<Ninja, SlideSpot> {

  public NinjaSlideSpotCollisionManager(Ninja object, ArrayList<SlideSpot> others) {
    super(object, others);
  }

  @Override
  public void onCollision(Ninja object, SlideSpot other) {
    if (object.state.contains(NinjaState.STANDING)) {
      if (other.direction == SlideSpot.SlideDirection.LEFT) { 
        object.state.add(NinjaState.CAN_SLIDE_LEFT);
      } else if (other.direction == SlideSpot.SlideDirection.RIGHT) { 
        object.state.add(NinjaState.CAN_SLIDE_RIGHT);
      }  
    } else if (object.state.contains(NinjaState.SLIDING)){
      if ((Float.compare(object.direction, (float) Math.PI) == 0 && other.direction == SlideSpot.SlideDirection.RIGHT && object.x <= other.x) ||
          (Float.compare(object.direction, 0) == 0 && other.direction == SlideSpot.SlideDirection.LEFT && object.x >= other.x)) {
        object.x = other.x;
        object.state.remove(NinjaState.SLIDING);
        object.state.add(NinjaState.STANDING);
        object.speed = 0;
      }
    }
  }

  @Override
  public void onNoCollision(Ninja object) {
    
  }

}
