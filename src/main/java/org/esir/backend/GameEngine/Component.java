package org.esir.backend.GameEngine;

import org.esir.backend.GameEngine.Components.ColliderComponent;

public class Component {

    protected GameObject parent;

    public Component(GameObject parent){
        this.parent = parent;
    }

    public GameObject getParent() {
        return parent;
    }

    public void start(){};

    public void update(float dt){};

    public void end(){};



}
