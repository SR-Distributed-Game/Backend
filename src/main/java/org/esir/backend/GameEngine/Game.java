package org.esir.backend.GameEngine;
import lombok.Getter;
import lombok.Setter;
import org.esir.backend.Requests.packet;

@Setter
@Getter
public class Game {

    static Game instance ;
    private Scene scene;
    private int roomId;
    private int idCounter = 0;
    private CollisionSystem collisionSystem;


    private Game(){
        this.collisionSystem = new SpatialHashmap(100);
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public CollisionSystem getCollisionSystem(){
        return collisionSystem;
    }


    protected void start(){
        this.scene.Mstart();
    };

    protected void end(){
        this.scene.Mend();
    };


    public void Mupdate(float dt){
        this.collisionSystem.clear();
        this.scene.Mupdate(dt);
        this.collisionSystem.update();
    }



    public void handleSpawnObject(packet packet){
        this.scene.handleSpawnObject(packet);
    };

    public void handleDestroyObject(packet packet){
        this.scene.handleDestroyObject(packet);
    };

    public void handleUpdateObject(packet packet){
        this.scene.handleUpdateObject(packet);
    };



    public void handleFullState(packet packet){
        this.scene.handleSendFullState(packet);
    };

    public int getNewId(){
        return idCounter++;
    }


}
