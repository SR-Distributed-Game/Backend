package org.esir.backend.ImplementedGame;

import java.util.*;

import org.esir.backend.GameEngine.GameObject;
import org.esir.backend.GameEngine.Serializable;
import org.json.JSONObject;

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
        // Directly update the score
        this.leaderboard.put(Integer.toString(id), score);
        // Convert to a sortable structure and sort
        sortLeaderboard();
        updateSyncState();
    }

    private void sortLeaderboard() {
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        this.leaderboard.keySet().forEach(key -> {
            list.add(new AbstractMap.SimpleEntry<>(key, this.leaderboard.getInt(key)));
        });

        // Sort the list based on scores
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue()); // For descending order
            }
        });

        // Clear the existing leaderboard and repopulate it in sorted order
        JSONObject sortedLeaderboard = new JSONObject();
        for (Map.Entry<String, Integer> entry : list) {
            sortedLeaderboard.put(entry.getKey(), entry.getValue());
        }

        // Update the leaderboard with the sorted entries
        this.leaderboard = sortedLeaderboard;
    }
}
