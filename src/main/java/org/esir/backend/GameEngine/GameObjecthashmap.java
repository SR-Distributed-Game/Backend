package org.esir.backend.GameEngine;

import org.json.JSONObject;

import java.util.HashMap;

public class GameObjecthashmap extends HashMap<Integer, GameObject> implements JsonSerializable{
    public GameObjecthashmap(){
        super();
    }
    public GameObjecthashmap(HashMap<Integer, GameObject> gameObjects){
        super(gameObjects);
    }
    public GameObjecthashmap copy(){
        GameObjecthashmap gameObjecthashmap = new GameObjecthashmap();
        for (Integer key : this.keySet()) {
            gameObjecthashmap.put(key, this.get(key).copy());
        }
        return gameObjecthashmap;
    }

    @Override
    public void updateFromJson(JSONObject json) {

    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        for (Integer key : this.keySet()) {
            json.put(key.toString(), this.get(key).toSerialized());
        }
        return json;
    }
}
