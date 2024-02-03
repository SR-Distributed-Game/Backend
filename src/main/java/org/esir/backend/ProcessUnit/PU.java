package org.esir.backend.ProcessUnit;

import ch.qos.logback.core.joran.sanity.Pair;
import org.esir.backend.IO.JSONFormat;
import org.esir.backend.Requests.packet;
import org.esir.backend.Requests.packetConnect;
import org.esir.backend.Requests.packetJoinRoom;
import org.esir.backend.Transport.QueueMaster;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;


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
                    return;
                }
                case "DestroyObject" -> {
                    log.error("Error request: " + "DestroyObject is not implemented");
                    return;
                }
                case "UpdateObject" -> {
                    log.error("Error request: " + "UpdateObject is not implemented");
                    return;
                }
                case "JoinRoom" -> {
                    packet = handleJoinRoom((packetJoinRoom) packet);
                }
                case "CreateRoom" -> {
                    log.error("Error request: " + "CreateRoom is not implemented");
                    return;
                }
                case "ClosingRoom" -> {
                    log.error("Error request: " + "ClosingRoom is not implemented");
                    return;
                }
                case "LeavingRoom" -> {
                    log.error("Error request: " + "LeavingRoom is not implemented");
                    return;
                }
                case "ConnectSucces" -> {
                    packet = handleConnectSucces((packetConnect) packet);
                }
                default -> throw new IllegalStateException("Unexpected value: " + packet.getType());
            }
            queueMaster.get_queuePUOut().add(packet);
        }
    }


    private int getNewPlayerID() {  //TODO : find a better way to get a new ID
        return _players.size();
    }

    private packet handleConnectSucces(packetConnect packet) {
        int newID = getNewPlayerID();
        _players.put(newID, (packet.getMetadata().getString("playername")));

        packet.setClientId(-2);
        packet.setRoomId(-1);
        JSONObject metadata = new JSONObject();
        metadata.put("playerId", newID); //TODO : change to clientID
        packet.setMetadata(metadata);

        return packet;
    }

    private packet handleJoinRoom(packetJoinRoom packet) {
        _leaderboard.put(packet.getClientId(), 0);

        JSONObject metadata = new JSONObject();
        metadata.put("playerId", packet.getClientId());
        packet.setMetadata(metadata);
        packet.setRoomId(-1);
        packet.setClientId(-2);

        return packet;
    }


}
