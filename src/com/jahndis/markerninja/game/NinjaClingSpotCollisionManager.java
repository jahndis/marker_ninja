package com.jahndis.markerninja.game;

import java.util.ArrayList;

import com.jahndis.whalebot.collisions.CollisionManager;

public class NinjaClingSpotCollisionManager extends CollisionManager<Ninja, ClingSpot> {

  public NinjaClingSpotCollisionManager(Ninja object, ArrayList<ClingSpot> others) {
    super(object, others);
  }

  @Override
  public void onCollision(Ninja object, ClingSpot other) {
    object.respondToCollisionWithClingSpot(other);
  }

  @Override
  public void onNoCollision(Ninja object) {
    object.respondToNoCollisionWithClingingSpot();
  }

}
