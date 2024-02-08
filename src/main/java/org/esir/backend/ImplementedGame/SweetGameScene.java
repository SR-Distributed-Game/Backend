package org.esir.backend.ImplementedGame;

import org.esir.backend.GameEngine.Scene;
import org.esir.backend.GameEngine.Vector2;
import org.esir.backend.ImplementedGame.fruit;
import org.esir.backend.ImplementedGame.player;


public class SweetGameScene extends Scene {

    private float spawnRateSecond = 1000f;
    private float spawnTimer = 0.0f;

    private int maxFruit = 400;

    @Override
    public void start() {
        // super important for the serialization
        this.getTypeRegistry().registerType("fruit",fruit.class);
        this.getTypeRegistry().registerType("player",player.class);
        this.getTypeRegistry().registerType("Leaderboard",Leaderboard.class);

        addGameObject(new Leaderboard());

        for (int i = 0; i < 100; i++) {
            spawnRandomFruit();
        }

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
