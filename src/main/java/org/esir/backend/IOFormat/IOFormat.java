package org.esir.backend.IOFormat;
import org.esir.backend.Requests.packet;

public interface IOFormat {
    public Boolean IsFormatCorrect(String message);
    public void isSchemaCorrect();

    public packet FromString(String message);

    public String FromPacket(packet packet);
}
