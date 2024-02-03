package org.esir.backend.Requests;

import org.json.JSONObject;

import java.util.Map;

public abstract class packet {

    private int _ClientId;
    private JSONObject _metadata;

    private int _RoomId ;

    public packet(int ClientId, int RoomId, JSONObject metadata){
        _metadata = metadata;
        _ClientId = ClientId;
        _RoomId = RoomId;

    }

    public int getRoomId() {
        return _RoomId;
    }

    public void setRoomId(int RoomId) {
        _RoomId = RoomId;
    }

    public int getClientId() {
        return _ClientId;
    }

    public void setClientId(int ClientId) {
        _ClientId = ClientId;
    }

    public JSONObject getMetadata() {
        return _metadata;
    }

    public void setMetadata(JSONObject metadata) {
        _metadata = metadata;
    }

    public abstract String getType();

    public JSONObject toJSONObject() {
        JSONObject res = new JSONObject();

        res.put("Type", getType());
        res.put("ClientId", Integer.toString(getClientId()));
        res.put("RoomId", Integer.toString(getRoomId()));
        res.put("metadata", getMetadata());

        return res;
    }

}
