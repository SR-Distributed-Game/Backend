package org.esir.backend.GameEngine;

import lombok.Getter;
import lombok.Setter;

public class GameObject extends SerializableGameObject{

    @Getter
    @Serializable
    protected Transform transform = new Transform();

    @Serializable
    protected String name;

    @Getter
    @Setter
    @Serializable
    protected int id;

    public GameObject(){
        this.name = "GameObject";
        this.id = 0;
    }


}
