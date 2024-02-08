package org.esir.backend.ImplementedGame;

import org.esir.backend.GameEngine.GameObject;
import org.esir.backend.GameEngine.Serializable;

public class player extends GameObject {
    @Serializable
    private int points;
    @Serializable
    private int speed ;

    @Serializable
    private int clientID;

    public player(){
        this.points = 0;
        this.speed = 5;
        this.AcquireNewId();
    }

    @Override
    public void start() {
        this.getTransform().getScale().setX(20);
        this.getTransform().getScale().setY(20);
    }
}
