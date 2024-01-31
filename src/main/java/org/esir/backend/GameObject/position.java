package org.esir.backend.GameObject;

public class position {
    private int _x;
    private int _y;

    public position(int x, int y){
        this._x = x;
        this._y = y;
    }

    public int getX(){
        return this._x;
    }

    public int getY(){
        return this._y;
    }

    public void setX(int x){
        this._x = x;
    }

    public void setY(int y){
        this._y = y;
    }
}
