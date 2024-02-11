package org.esir.backend.GameEngine;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
public class Vector2 implements JsonSerializable{


    @Serializable
    private float x;

    @Serializable
    private float y;

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2(){
        this.x = 0;
        this.y = 0;
    }

    public Vector2 add(Vector2 vector){
        return new Vector2(this.x + vector.x, this.y + vector.y);
    }

    public Vector2 sub(Vector2 vector){
        return new Vector2(this.x - vector.x, this.y - vector.y);
    }

    public Vector2 mul(Vector2 vector){
        return new Vector2(this.x * vector.x, this.y * vector.y);
    }

    public Vector2 div(Vector2 vector){
        return new Vector2(this.x / vector.x, this.y / vector.y);
    }

    public Vector2 add(float value){
        return new Vector2(this.x + value, this.y + value);
    }

    public Vector2 sub(float value){
        return new Vector2(this.x - value, this.y - value);
    }

    public Vector2 mul(float value){
        return new Vector2(this.x * value, this.y * value);
    }

    public Vector2 div(float value){
        return new Vector2(this.x / value, this.y / value);
    }

    public float magnitude(){
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2 normalize(){
        return this.div(this.magnitude());
    }

    public float dot(Vector2 vector){
        return this.x * vector.x + this.y * vector.y;
    }

    public float distance(Vector2 vector){
        return this.sub(vector).magnitude();
    }

    @Override
    public String toString() {
        return "{" +
                "x:" + x +
                ", y:" + y +
                '}';
    }

    @Override
    public void updateFromJson(JSONObject json) {
        x = json.getFloat("x");
        y = json.getFloat("y");
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("x", x);
        json.put("y", y);
        return json;
    }

}
