package org.esir.backend.GameEngine;

import org.esir.backend.Requests.packet;

import java.util.ArrayList;

public abstract class RequestAccepter {
    protected ArrayList<packet> remoteRequests = new ArrayList<>();

    public void acceptRequest(packet p) {
        remoteRequests.add(p);
    }


    abstract void handleRemoteRequests();

}
