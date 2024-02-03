package org.esir.backend.Requests;

import org.json.JSONObject;

import java.util.Map;

public class packetConnect extends packet {

    public packetConnect(int _ClientId, int RoomId, JSONObject metadata) {
        super(_ClientId, RoomId, metadata);
    }

    @Override
    public String getType() {
        return "ConnectSucces";
    }

}
