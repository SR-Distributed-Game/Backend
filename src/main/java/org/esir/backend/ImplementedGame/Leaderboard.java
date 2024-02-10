package org.esir.backend.ImplementedGame;

import org.esir.backend.GameEngine.GameObject;
import org.esir.backend.GameEngine.JsonSerializable;
import org.esir.backend.GameEngine.Serializable;
import org.json.JSONObject;

import java.util.ArrayList;

public class Leaderboard extends GameObject {

    @Serializable
    private JSONObject leaderboard;


    public Leaderboard(){
        this.leaderboard = new JSONObject();
        this.AcquireNewId();
    }

    @Override
    public void start() {
        this.setTag("Leaderboard");
        this.getTransform().getPosition().setX(0);
        this.getTransform().getPosition().setY(0);
    }

    public void addPlayer(int id, int score){
        this.leaderboard.put(Integer.toString(id), score);
        updateSyncState();
    }

    public void removePlayer(int id){
        this.leaderboard.remove(Integer.toString(id));
        updateSyncState();
    }

    public void updateScore(int id, int score){
        this.leaderboard.put(Integer.toString(id), score);
        System.out.println("Leaderboard updated");
        updateSyncState();
    }

}
