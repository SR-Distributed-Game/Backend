package org.esir.backend.GameEngine;

import lombok.Getter;
import lombok.Setter;

public class GameObject extends SerializableGameObject{

    @Getter
    @Serializable
    protected Transform transform = new Transform();

    @Getter
    @Setter
    @Serializable
    protected String name;

    @Getter
    @Setter
    @Serializable
    protected int id;

    protected String Type;

    public GameObject(){
        this.name = "GameObject";
    }

    public int AcquireNewId(){
        this.id = Game.getInstance().getNewId();
        return this.id;
    }

    public GameObject copy(){
        GameObject gameObject = new GameObject();
        gameObject.id = this.id;
        gameObject.name = this.name;
        gameObject.transform = this.transform;
        return gameObject;
    }

}
