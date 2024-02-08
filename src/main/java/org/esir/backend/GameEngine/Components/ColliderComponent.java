package org.esir.backend.GameEngine.Components;

import org.esir.backend.GameEngine.Component;
import org.esir.backend.GameEngine.Game;
import org.esir.backend.GameEngine.GameObject;

public class ColliderComponent extends Component {
    public ColliderComponent(GameObject parent) {
        super(parent);
    }

    public void start() {
    }

    public void update(float dt) {
        Game.getInstance().getCollisionSystem().insert(this);
    }

    public void onCollisionEnter(ColliderComponent other) {
        this.parent.onCollisionEnter(other);
    }
}
