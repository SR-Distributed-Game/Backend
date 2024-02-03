package org.esir.backend.Requests;

import org.json.JSONObject;

public class packetJoinRoom extends packet {

    public packetJoinRoom(int _ClientId, int RoomId, JSONObject metadata) {
        super(_ClientId, RoomId, metadata);
    }

    @Override
    public String getType() {
        return "JoinRoom";
    }
}
