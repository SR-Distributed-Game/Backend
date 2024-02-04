package org.esir.backend.ProcessUnit;

import org.esir.backend.Requests.packet;
import org.esir.backend.Transport.QueueMaster;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


@Service
public class PU {

    private static final Logger log = LoggerFactory.getLogger(PU.class);

    private final QueueMaster queueMaster;

    private final Map<Integer, Integer> _leaderboard = new HashMap<>(); // ID, score

    private final Map<Integer, String> _players = new HashMap<>(); // ID, name


    public PU() {
        queueMaster = QueueMaster.getInstance();
    }


    @Scheduled(fixedRateString = "${pu.fixedRate}")
    public void run() {
        if (!queueMaster.get_queuePUIn().isEmpty()) {
            packet packet = queueMaster.get_queuePUIn().poll();

            switch (packet.getType()) {
                case "SpawnObject" -> {
                    packet = handleSpawnObject(packet);
                }
                case "DestroyObject" -> {
                    packet = handleDestroyObject(packet);
                }
                case "UpdateObject" -> {
                    packet = handleUpdateObject(packet);
                }
                case "JoinRoom" -> {
                    packet = handleJoinRoom(packet);
                }
                case "CreateRoom" -> {
                    packet = handleCreateRoom(packet);
                }
                case "ClosingRoom" -> {
                    packet = handleClosingRoom(packet);
                }
                case "LeavingRoom" -> {
                    packet = handleLeavingRoom(packet);
                }
                case "ConnectSucces" -> {
                    packet = handleConnectSucces(packet);
                }
                default -> throw new IllegalStateException("Unexpected value: " + packet.getType());
            }
            queueMaster.get_queuePUOut().add(packet);
        }
    }


    private int getNewPlayerID() {  //TODO : find a better way to get a new ID
        return _players.size();
    }

    private packet handleConnectSucces(packet packet) {
        int newID = getNewPlayerID();
        _players.put(newID, (packet.getMetadata().getString("playername")));
        packet.setClientId(-2);
        packet.setRoomId(-1);
        JSONObject metadata = new JSONObject();
        metadata.put("clientID", newID);
        packet.setMetadata(metadata);
        return packet;
    }


    private packet handleJoinRoom(packet packet) {
        _leaderboard.put(packet.getClientId(), 0);

        JSONObject metadata = new JSONObject();
        metadata.put("leaderboard", _leaderboard);
        metadata.put("players", _players);
        packet.setMetadata(metadata);
        packet.setRoomId(-1);
        packet.setClientId(-2);

        return packet;
    }

    private packet handleLeavingRoom(packet packet) {
        _leaderboard.remove(packet.getClientId());
        JSONObject metadata = new JSONObject();
        metadata.put("leaderboard", _leaderboard);
        metadata.put("clientID", packet.getClientId());
        packet.setMetadata(metadata);
        packet.setRoomId(-1);
        packet.setClientId(-2);
        return packet;
    }

    private packet handleClosingRoom(packet packet) {
        ThrowNotHandledException(packet);
        return null;
    }

    private packet handleCreateRoom(packet packet) {
        ThrowNotHandledException(packet);
        return null;
    }

    private packet handleUpdateObject(packet packet) {
        ThrowNotHandledException(packet);
        return null;
    }

    private packet handleDestroyObject(packet packet) {
        ThrowNotHandledException(packet);
        return null;
    }

    private packet handleSpawnObject(packet packet) {
        ThrowNotHandledException(packet);
        return null;
    }


    private void ThrowNotHandledException(packet packet) {
        throw new IllegalStateException("Leading to an unhandled case yet: " + packet.getType());
    }

}
