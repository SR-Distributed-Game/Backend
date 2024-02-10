package org.esir.backend.ImplementedGame;

import org.esir.backend.GameEngine.Components.ColliderComponent;
import org.esir.backend.GameEngine.GameObject;
import org.esir.backend.GameEngine.Serializable;

public class fruit extends GameObject {

    private static int fruitCount;
    @Serializable
    public String SerializationTest;

    @Serializable
    public int lifeTime;


    public int getLifeTime(){
        return lifeTime;
    }

    public fruit(){
        this.name = "fruit";
        this.lifeTime = 1;
        fruit.fruitCount++;

    }

    @Override
    public void start() {
        this.lifeTime = 1;
        this.setTag("fruit");
        this.addComponent(new ColliderComponent(this));
    }

    @Override
    public void end(){
        fruit.fruitCount--;
    }

    static int getFruitCount(){
        return fruitCount;
    }

}
