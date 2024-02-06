package org.esir.backend.GameEngine;


import lombok.Getter;
import lombok.Setter;
import org.esir.backend.ImplementedGame.fruit;
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
        GameObject gameObject = gameObjects.get(id);
        if (gameObject == null)
            return;
        gameObject = gameObject.copy();
        gameObjects.remove(id);

        JSONObject metadata = gameObject.toSerialized();
        JSONObject ret = new JSONObject();
        ret.put("objectData",metadata);
        // TODO: this has to be defined in another class / factory
        packet packet = new packet("DestroyObject",-2,roomId,ret);
        QueueMaster.getInstance().get_queuePUOut().add(packet);

    }

    public void handleSpawnObject(packet packet){
        JSONObject objectData = packet.getMetadata().getJSONObject("objectData");
        Class<? extends GameObject> cls = getClassBasedOnString(objectData.getString("Type"));
        GameObject gameObject = GameObject.fromSerialized(cls, objectData);
        gameObject.AcquireNewId();
        addGameObject(cls.cast(gameObject));
    }

    public void handleDestroyObject(packet packet){
        int id = packet.getMetadata().getJSONObject("objectData").getInt("id");
        removeGameObject(id);
    }

public void handleSendFullState(packet packet){
        JSONObject ret = new JSONObject();
        JSONObject objectData = new JSONObject();
        for (GameObject gameObject : gameObjects.values()){
            objectData.put(String.valueOf(gameObject.getId()),gameObject.toSerialized());
        }
        ret.put("objectData",objectData);
        packet packet1 = new packet("FullState",-2,roomId,ret);
        QueueMaster.getInstance().get_queuePUOut().add(packet1);
    }

    private Class<? extends GameObject> getClassBasedOnString(String type){
        switch (type){
            case "fruit":
                return fruit.class;
            default:
                return fruit.class;
        }
    }

}
