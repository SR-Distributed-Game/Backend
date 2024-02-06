package org.esir.backend.GameEngine;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Setter
@Getter
public class Transform implements JsonSerializable {

    @Serializable
    private Vector2 position;

    @Serializable
    private Vector2 scale;

    @Serializable
    private float rotation;

    public Transform(){
        this.position = new Vector2();
        this.scale = new Vector2(1, 1);
        this.rotation = 0;
    }


    @Override
    public void updateFromJson(JSONObject json) {
        position.updateFromJson(json.getJSONObject("position"));
        scale.updateFromJson(json.getJSONObject("scale"));
        rotation = json.getFloat("rotation");
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("position", position.toJson());
        json.put("scale", scale.toJson());
        json.put("rotation", rotation);
        return json;
    }

}
