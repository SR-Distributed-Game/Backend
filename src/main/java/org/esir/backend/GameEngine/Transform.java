package org.esir.backend.GameEngine;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Setter
@Getter
public class Transform extends JSONObject {

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
    public String toString() {
        return "{" +
                "position:" + position +
                ", scale:" + scale +
                ", rotation:" + rotation +
                '}';
    }


}
