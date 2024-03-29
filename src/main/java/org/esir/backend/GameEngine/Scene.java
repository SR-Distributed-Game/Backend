package org.esir.backend.GameEngine;


import lombok.Getter;
import lombok.Setter;
import org.esir.backend.ImplementedGame.fruit;
import org.esir.backend.Requests.packet;
import org.esir.backend.Transport.QueueMaster;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Scene extends GameObject{

    @Getter
    TypeRegistry typeRegistry = new TypeRegistry();

    @Serializable
    GameObjecthashmap gameObjects = new GameObjecthashmap();

    HashMap<String,ArrayList<GameObject>> gameObjectsByTag = new HashMap<>();

    public ArrayList<GameObject> getGameObjectWithTag(String tag ){
        return gameObjectsByTag.get(tag);
    }

    public void addGameObjectWithTag(GameObject gameObject){
        if (gameObjectsByTag.get(gameObject.getTag()) == null){
            gameObjectsByTag.put(gameObject.getTag(),new ArrayList<>());
        }
        gameObjectsByTag.get(gameObject.getTag()).add(gameObject);
    }

    public void removeGameObjectWithTag(GameObject gameObject){
        gameObjectsByTag.get(gameObject.getTag()).remove(gameObject);
    }

    @Getter
    @Setter
    protected int roomId;

    @Override
    public void Mstart(){
        for (GameObject gameObject : gameObjects.values()) {
            gameObject.Mstart();
        }
        start();
    }

    @Override
    public void start(){}


    @Override
    public void end(){}

    @Override
    public void Mend(){
        for (GameObject gameObject : gameObjects.values()) {
            gameObject.Mend();
        }
        end();
    }

    @Override
    public void Mupdate(float dt){
        ArrayList<Integer> toRemove = new ArrayList<>();
        for (GameObject gameObject : gameObjects.values()) {
            if (gameObject.isDestroyed()){
                toRemove.add(gameObject.getId());
                continue;
            }
            gameObject.Mupdate(dt);

        }
        for (int id : toRemove){
            removeGameObject(id);
        }
        update(dt);
    }

    @Override
    public void update(float dt){

    }


    public HashMap<Integer, GameObject > getGameObjects(){
        return gameObjects;
    }

    public void addGameObject(GameObject gameObject){
        gameObject.scene = this;
        gameObject.Mstart();
        gameObjects.put(gameObject.getId(),gameObject);
        addGameObjectWithTag(gameObject);
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
        gameObject.Mend();
        removeGameObjectWithTag(gameObject);
        gameObject = gameObject.copy();

        gameObjects.remove(id);


        JSONObject metadata = gameObject.toSerialized();
        JSONObject ret = new JSONObject();
        ret.put("objectData",metadata);
        // TODO: this has to be defined in another class / factory
        packet packet = new packet("DestroyObject",-2,roomId,ret);
        QueueMaster.getInstance().get_queuePUOut().add(packet);

    }

    public void updateGameObject(GameObject gameObject) {
        JSONObject metadata = gameObject.toSerialized();
        JSONObject ret = new JSONObject();
        ret.put("objectData",metadata);
        packet packet = new packet("UpdateObject",-2,roomId,ret);
        QueueMaster.getInstance().get_queuePUOut().add(packet);
    }

    public void handleSpawnObject(packet packet){
        JSONObject objectData = packet.getMetadata().getJSONObject("objectData");
        Class<? extends GameObject> cls = (Class<? extends GameObject>) this.typeRegistry.getType(objectData.getString("Type"));
        GameObject gameObject = GameObject.fromSerialized(cls, objectData);
        gameObject.AcquireNewId();
        addGameObject(cls.cast(gameObject));
    }

    public void handleUpdateObject(packet packet){
        JSONObject objectData = packet.getMetadata().getJSONObject("objectData");
        int id = objectData.getInt("id");

        GameObject gameObject = gameObjects.get(id);

        if (gameObject != null){
            gameObject.updateFromRequest(objectData);
            updateGameObject(gameObject);
        }
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


}
