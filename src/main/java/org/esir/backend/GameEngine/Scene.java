package org.esir.backend.GameEngine;


import lombok.Getter;
import lombok.Setter;
import org.esir.backend.Requests.packet;
import org.esir.backend.Transport.QueueMaster;
import org.json.JSONObject;

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


    public HashMap<Integer, GameObject > getGameObjects(){
        return gameObjects;
    }

    public void addGameObject(GameObject gameObject){
        gameObjects.put(gameObject.getId(),gameObject);
        JSONObject metadata = gameObject.toSerialized();
        JSONObject ret = new JSONObject();

        ret.put("objectData",metadata);
        // TODO: this has to be defined in another class / factory
        packet packet = new packet("SpawnObject",-2,roomId,ret);
        QueueMaster.getInstance().get_queuePUOut().add(packet);
    }

    public GameObject getGameObject(int id){
        return gameObjects.get(id);
    }

    public void removeGameObject(int id){
        gameObjects.remove(id);
    }

}
