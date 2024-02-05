package org.esir.backend.IO;

import org.esir.backend.Requests.packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class decoder extends IO {

    private static final Logger logger = LoggerFactory.getLogger(JSONFormat.class);
    private String _message;
    private packet _packet;

    public decoder(IOFormat format){
        super(format);
    }

    public void setMessage(String message) {
        _message = message;
    }

    public packet getPackets() {
        return _packet;
    }
    @Override
    public void run() {
        if (checkMessageFormat(_message)) {
            _packet = format.FromString(_message);
        }
        else {
            _packet = null;
            logger.error("Error reading JSON schema file: " + "Message format is not correct");
        }
    }

    @Override
    protected Boolean checkMessageFormat(String Message) {
        try {
            return format.IsFormatCorrect(Message);
        } catch (Exception e) {
            return false;
        }
    }
}
