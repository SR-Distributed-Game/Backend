package org.esir.backend.requests;

import org.esir.backend.GameObject.position;

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
        return "Create";
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


}
