package org.esir.backend.ImplementedGame;

import org.esir.backend.GameEngine.Game;
import org.esir.backend.GameEngine.Scene;
import org.esir.backend.GameEngine.Vector2;
import org.esir.backend.Requests.packet;
import org.esir.backend.Transport.QueueMaster;
import org.json.JSONObject;


public class SweetGameScene extends Scene {


    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public void update() {
        packet packet = spawnRandomFruit();
        QueueMaster.getInstance().get_queuePUOut().add(packet);
    }


    private packet spawnRandomFruit(){
        fruit f = new fruit();
        f.SerializationTest = "TEST FOR SERIALIZATION";
        f.getTransform().getPosition().setX((float)Math.random()*2000);
        f.getTransform().getPosition().setY((float)Math.random()*2000);
        f.getTransform().setScale(new Vector2(10,10));
        f.setId(Game.getInstance().getNewId());

        JSONObject metadata = f.toSerialized();
        JSONObject ret = new JSONObject();

        ret.put("objectData",metadata);
        // TODO: this has to be defined in another class / factory
        packet packet = new packet("SpawnObject",-2,roomId,ret);
        return packet;
    }




}
