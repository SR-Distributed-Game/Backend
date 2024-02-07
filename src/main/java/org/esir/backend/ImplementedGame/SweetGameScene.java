package org.esir.backend.ImplementedGame;

import org.esir.backend.GameEngine.Game;
import org.esir.backend.GameEngine.Scene;
import org.esir.backend.GameEngine.Vector2;
import org.esir.backend.Requests.packet;
import org.esir.backend.Transport.QueueMaster;
import org.json.JSONObject;


public class SweetGameScene extends Scene {

    private float spawnRateSecond = 1000f;
    private float spawnTimer = 0.0f;

    private int maxFruit = 400;

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public void update(float dt) {
        if(spawnTimer == 0.0f){
            if(fruit.getFruitCount() < maxFruit){
                spawnRandomFruit();
            }

        }
        spawnTimer = (spawnTimer + dt)%spawnRateSecond;
    }

    private void spawnRandomFruit(){
        fruit f = new fruit();
        f.SerializationTest = "TEST FOR SERIALIZATION";
        f.getTransform().getPosition().setX((float)Math.random()*2000);
        f.getTransform().getPosition().setY((float)Math.random()*2000);
        f.getTransform().setScale(new Vector2(10,10));
        f.AcquireNewId();
        addGameObject(f);
    }
}
