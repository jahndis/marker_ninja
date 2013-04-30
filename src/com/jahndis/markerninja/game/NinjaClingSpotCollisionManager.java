package com.jahndis.markerninja.game;

import java.util.ArrayList;

import com.jahndis.markerninja.game.Ninja.NinjaState;
import com.jahndis.whalebot.collisions.CollisionManager;

public class NinjaClingSpotCollisionManager extends CollisionManager<Ninja, ClingSpot> {

  public NinjaClingSpotCollisionManager(Ninja object, ArrayList<ClingSpot> others) {
    super(object, others);
  }

  @Override
  public void onCollision(Ninja object, ClingSpot other) {
    if (!object.state.contains(NinjaState.CLINGING)) { 
      object.setPosition(other.getPosition());
      object.speed = 0;
      
      object.state.remove(NinjaState.JUMPING);
      object.state.remove(NinjaState.FALLING);
      object.state.remove(NinjaState.WALL_SLIDING);
      object.state.add(NinjaState.CLINGING);
    }
  }

  @Override
  public void onNoCollision(Ninja object) {
    object.state.remove(NinjaState.CLINGING);
  }

}
