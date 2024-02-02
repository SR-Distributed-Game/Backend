package org.esir.backend.Requests;

import org.esir.backend.GameObject.position;

import java.util.HashMap;
import java.util.Map;

public class packetCreate implements packet{


    private int _ClientId;
    private String _Type;
    private position _Position;

    public packetCreate(int ClientId, String Type, position Position){
        _ClientId = ClientId;
        _Type = Type;
        _Position = Position;
    }

    @Override
    public String getType() {
        return "SpawnObject";
    }

    public int getClientId() {
        return _ClientId;
    }

    public String getTypeObject() {
        return _Type;
    }

    public position getPosition() {
        return _Position;
    }

    public void setClientId(int ClientId) {
        _ClientId = ClientId;
    }

    public void setTypeObject(String Type) {
        _Type = Type;
    }

    public void setPosition(position Position) {
        _Position = Position;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Type", _Type);
        map.put("ClientId", Integer.toString(_ClientId));
        map.put("Position", _Position.toString());
        return map;
    }


}
