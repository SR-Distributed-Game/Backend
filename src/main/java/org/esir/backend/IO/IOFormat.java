package org.esir.backend.IO;
import org.esir.backend.requests.packet;

public interface IOFormat {
    public Boolean IsFormatCorrect(String message);
    public void isSchemaCorrect();

    public packet FromString(String message);

    public String FromPacket(packet packet);
}