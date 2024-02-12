package org.esir.backend.ImplementedGame;

import org.esir.backend.GameEngine.Components.ColliderComponent;
import org.esir.backend.GameEngine.Game;
import org.esir.backend.GameEngine.GameObject;
import org.esir.backend.GameEngine.Scene;
import org.esir.backend.GameEngine.Serializable;

import java.math.BigDecimal;

public class player extends GameObject {

    @Serializable
    private int points;
    @Serializable
    private BigDecimal speed ;

    @Serializable
    private int clientID;

    @Serializable
    private boolean hasBeenEaten;

    @Serializable
    private String color;

    public player(){

        this.points = 0;
        this.speed = new BigDecimal(5);
        this.hasBeenEaten = false;
        this.AcquireNewId();
        this.addComponent(new ColliderComponent(this));
    }

    @Override
    public void start() {
        this.getTransform().getPosition().setX((float)Math.random()*2000);
        this.getTransform().getPosition().setY((float)Math.random()*2000);
        this.getTransform().getScale().setX(20);
        this.getTransform().getScale().setY(20);
        this.setTag("player");
        ((Leaderboard)scene.getGameObjectWithTag("Leaderboard").getFirst()).addPlayer(this.getId(), this.getPoints());
    }

    public int getPoints() {
        return points;
    }

    public float sizeFunction(float x){
        return (float) (20 + Math.sqrt(x));
    }

    public void setPoints(int points) {
        this.points = points;
        this.getTransform().getScale().setX(sizeFunction(points));
        this.getTransform().getScale().setY(sizeFunction(points));
    }



    @Override
    public void onCollisionEnter(ColliderComponent other) {
        if(other.getParent().getTag().equals("fruit")){
            fruit otherFruit = (fruit) other.getParent();
             this.setPoints(this.points+ otherFruit.getLifeTime());
            ((Leaderboard)scene.getGameObjectWithTag("Leaderboard").getFirst()).updateScore(this.getId(), this.getPoints());
            otherFruit.destroy();
            updateSyncState();
        } else if (other.getParent().getTag().equals("player")){
            player p = (player) other.getParent();
            if(p.getId() != this.getId()){
                if(p.getPoints() > this.getPoints()){
                    this.hasBeenEaten = true;
                    this.destroy();
                    updateSyncState();
                }
            }
        }
    }

    @Override
    public void end(){
        ((Leaderboard)scene.getGameObjectWithTag("Leaderboard").getFirst()).removePlayer(this.getId());
    }


}
