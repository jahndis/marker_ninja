package com.jahndis.markerninja.game;

import java.util.ArrayList;

import com.jahndis.whalebot.collisions.CollisionManager;

public class NinjaHidingSpotCollisionManager extends CollisionManager<Ninja, HidingSpot> {

  public NinjaHidingSpotCollisionManager(Ninja object, ArrayList<HidingSpot> others) {
    super(object, others);
  }

  @Override
  public void onCollision(Ninja object, HidingSpot other) {
    object.respondToCollisionWithHidingSpot(other);
  }

  @Override
  public void onNoCollision(Ninja object) {
    object.respondToNoCollisionWithHidingSpot();
  }

}
