package org.esir.backend.IO;

import org.esir.backend.IOFormat.IOFormat;
import org.esir.backend.IOFormat.JSONFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.esir.backend.Requests.packet;

public class encoder extends IO{

    private static final Logger logger = LoggerFactory.getLogger(JSONFormat.class);

    private String _message;

    private packet _packet;

    public encoder(IOFormat format) {
        super(format);
    }

    public void setPacket(packet packet) {
        _packet = packet;
    }

    public String getMessage() {
        return _message;
    }

    @Override
    public void run() {
        _message = format.FromPacket(_packet);
    }

    @Override
    protected Boolean checkMessageFormat(String Message) {
       return true;
    }
}
