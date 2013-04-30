package com.jahndis.markerninja.game;

import java.util.ArrayList;

import com.jahndis.markerninja.game.Ninja.NinjaState;
import com.jahndis.whalebot.collisions.CollisionManager;

public class NinjaHidingSpotCollisionManager extends CollisionManager<Ninja, HidingSpot> {

  public NinjaHidingSpotCollisionManager(Ninja object, ArrayList<HidingSpot> others) {
    super(object, others);
  }

  @Override
  public void onCollision(Ninja object, HidingSpot other) {
    if (!object.state.contains(NinjaState.HIDING)) { 
      object.setPosition(other.getPosition());
      object.speed = 0;
      object.state.remove(NinjaState.JUMPING);
      object.state.remove(NinjaState.FALLING);
      object.state.remove(NinjaState.WALL_SLIDING);
      object.state.add(NinjaState.HIDING);
    }
  }

  @Override
  public void onNoCollision(Ninja object) {
    object.state.remove(NinjaState.HIDING);
  }

}
