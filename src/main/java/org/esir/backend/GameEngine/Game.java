package org.esir.backend.GameEngine;
import lombok.Getter;
import lombok.Setter;
import org.esir.backend.Requests.packet;

@Setter
@Getter
public class Game {

    static Game instance ;
    protected Scene scene;
    protected int roomId;
    protected int idCounter = 0;

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    protected void start(){};

    public void end(){};

    public void Mupdate(){
        this.scene.Mupdate();
    }



    public void handleSpawnObject(packet packet){
        this.scene.handleSpawnObject(packet);
    };

    public void handleDestroyObject(packet packet){
        this.scene.handleDestroyObject(packet);
    };

    public void handleUpdateObject(packet packet){};

    public void handleSendFullState(packet packet){
        this.scene.handleSendFullState(packet);
    };

    public int getNewId(){
        return idCounter++;
    }


}