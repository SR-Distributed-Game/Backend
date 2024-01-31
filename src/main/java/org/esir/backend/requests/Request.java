package org.esir.backend.requests;

public class Request {

    class Metadata {
        private color color;
        private String value;
    }

    public Request(String type){
        this.type = type;
    }

    private String type;
    private int clientID;
    private  Metadata metadata;

}

class color {
    private int R;

    private int G;

    private int B;

    public color(int R, int G, int B){
        this.R = R;
        this.G = G;
        this.B = B;
    }

    public color(String hex){
        this.R = Integer.parseInt(hex.substring(0, 2), 16);
        this.G = Integer.parseInt(hex.substring(2, 4), 16);
        this.B = Integer.parseInt(hex.substring(4, 6), 16);
    }
}
