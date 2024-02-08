package org.esir.backend.GameEngine;

import org.esir.backend.GameEngine.Components.ColliderComponent;

public class CollisionSystem {
    public void insert(ColliderComponent colliderComponent) {
    }

    public void remove(ColliderComponent colliderComponent) {
    }

    public void clear() {
    }

    public void update() {
    }

    public void checkCollisions() {
    }

    public boolean checkAABBCollision(ColliderComponent collider1, ColliderComponent collider2) {
        // Assuming ColliderComponent or its parent GameObject has methods to get position and size
        var transform1 = collider1.getParent().getTransform();
        var transform2 = collider2.getParent().getTransform();
        return transform2.intersects(transform1);
    }


}
