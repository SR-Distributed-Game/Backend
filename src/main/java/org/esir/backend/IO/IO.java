package org.esir.backend.IO;

import org.esir.backend.requests.packet;

public abstract class IO {

    protected  IOFormat format;

    public IO(IOFormat format){
        this.format = format;
    }
    public abstract void run();
    protected abstract Boolean checkMessageFormat(String Message);
}
