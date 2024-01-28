package org.esir.backend.IO;

public abstract class IO {

    protected  IOFormat format;

    public IO(IOFormat format){
        this.format = format;
    }
    public abstract String run(String Message);
    protected abstract Boolean checkMessageFormat(String Message);
}
