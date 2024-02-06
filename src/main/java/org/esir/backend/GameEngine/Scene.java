package org.esir.backend.GameEngine;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class Scene {
    HashMap<Integer, GameObject > gameObjects = new HashMap<Integer, GameObject>();

    @Getter
    @Setter
    protected int roomId;
    public void start(){};

    public void end(){};

    public void Mupdate(){
        update();
    };

    public void update(){};


    public void addGameObject(GameObject gameObject){
        gameObjects.put(gameObject.getId(),gameObject);
    }

    public GameObject getGameObject(int id){
        return gameObjects.get(id);
    }

    public void removeGameObject(int id){
        gameObjects.remove(id);
    }

}
