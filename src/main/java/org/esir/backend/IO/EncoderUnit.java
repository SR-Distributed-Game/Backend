package org.esir.backend.IO;

import org.esir.backend.IOFormat.JSONFormat;
import org.esir.backend.Requests.packet;
import org.esir.backend.Transport.QueueMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EncoderUnit {

    private static final Logger log = LoggerFactory.getLogger(EncoderUnit.class);
    private encoder encoder;

    public EncoderUnit() {
        encoder = new encoder(new JSONFormat("default"));
    }

    @Scheduled(fixedRateString = "${EncoderUnit.fixedRate}")
    public void run(){
        if (!QueueMaster.getInstance().get_queuePUOut().isEmpty()){
            packet packet = QueueMaster.getInstance().get_queuePUOut().poll();

            encoder.setPacket(packet);

            encoder.run();

            String payload = encoder.getMessage();

            if (payload != null){
                QueueMaster.getInstance().get_queueEncoderOut().add(payload);

            }

        }
    }
}
