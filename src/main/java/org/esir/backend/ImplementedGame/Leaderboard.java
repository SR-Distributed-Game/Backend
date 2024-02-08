package org.esir.backend.ImplementedGame;

import org.esir.backend.GameEngine.GameObject;
import org.esir.backend.GameEngine.Serializable;
import org.json.JSONObject;

import java.util.ArrayList;

public class Leaderboard extends GameObject {
    @Serializable
    private JSONObject leaderboard;

    public Leaderboard(){
        this.leaderboard = new JSONObject();
    }

    public void addPlayer(int id, int score){
        this.leaderboard.put(Integer.toString(id), score);
    }

    public void removePlayer(int id){
        this.leaderboard.remove(Integer.toString(id));
    }

    public void updateScore(int id, int score){
        this.leaderboard.put(Integer.toString(id), score);
    }

}
