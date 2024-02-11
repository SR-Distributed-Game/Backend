package org.esir.backend.GameEngine;

import lombok.Getter;
import lombok.Setter;
import org.esir.backend.GameEngine.Components.ColliderComponent;

import java.util.ArrayList;

public class GameObject extends SerializableGameObject{

    protected Scene scene;

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

    @Getter
    @Setter
    @Serializable
    protected String tag;

    private ArrayList<Component> components = new ArrayList<>();

    protected String Type;

    protected boolean destroyed = false;

    public GameObject(){
        this.name = "GameObject";
    }

    public void destroy(){
        this.destroyed = true;
    }
    public boolean isDestroyed(){
        return this.destroyed;
    }


    public int AcquireNewId(){
        this.id = Game.getInstance().getNewId();
        return this.id;
    }

    public void addComponent(Component component){
        components.add(component);
    }

    public void removeComponent(Component component){
        components.remove(component);
    }

    public void Mstart(){
        start();
    };

    public void start(){};

    public void Mupdate(float dt){
        if (isDestroyed()){
            return;
        }
        for (Component component : components) {
            component.update(dt);
        }
        update(dt);
    };

    public void Mend(){
        end();
    };

    public void end(){};

    public void update(float dt){};

    public void updateSyncState(){
        Game.getInstance().getScene().updateGameObject(this);
    }

    public GameObject copy(){
        GameObject gameObject = new GameObject();
        gameObject.id = this.id;
        gameObject.name = this.name;
        gameObject.transform = this.transform;
        return gameObject;
    }

    public void onCollisionEnter(ColliderComponent other) {

    }


}
