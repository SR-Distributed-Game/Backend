package org.esir.backend.IO;

public class decoder extends IO {

    public decoder(IOFormat format){
        super(format);
    }
    @Override
    public String run(String Message) {
        checkMessageFormat(Message);
        // TODO: decode the message
        return "decoded message";
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
