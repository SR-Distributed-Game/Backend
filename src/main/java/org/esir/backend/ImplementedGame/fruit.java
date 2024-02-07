package org.esir.backend.ImplementedGame;

import org.esir.backend.GameEngine.GameObject;
import org.esir.backend.GameEngine.Serializable;

public class fruit extends GameObject {

    private static int fruitCount;
    @Serializable
    public String SerializationTest;

    public fruit(){
        this.name = "fruit";
        fruit.fruitCount++;
    }

    @Override
    public void end(){
        fruit.fruitCount--;
    }

    static int getFruitCount(){
        return fruitCount;
    }

}
