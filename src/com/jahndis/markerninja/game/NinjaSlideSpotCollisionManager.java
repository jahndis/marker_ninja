package com.jahndis.markerninja.game;

import java.util.ArrayList;

import com.jahndis.whalebot.collisions.CollisionManager;

public class NinjaSlideSpotCollisionManager extends CollisionManager<Ninja, SlideSpot> {

  public NinjaSlideSpotCollisionManager(Ninja object, ArrayList<SlideSpot> others) {
    super(object, others);
  }

  @Override
  public void onCollision(Ninja object, SlideSpot other) {
    object.respondToCollisionWithSlideSpot(other);
  }

  @Override
  public void onNoCollision(Ninja object) {
    
  }

}
