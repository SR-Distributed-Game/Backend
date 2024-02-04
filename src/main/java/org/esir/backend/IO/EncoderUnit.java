package org.esir.backend.IO;

import org.esir.backend.Requests.packet;
import org.esir.backend.Transport.QueueMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EncoderUnit {

    private static final Logger log = LoggerFactory.getLogger(EncoderUnit.class);

    private QueueMaster queueMaster;

    private encoder encoder;

    public EncoderUnit() {
        queueMaster = QueueMaster.getInstance();
        encoder = new encoder(new JSONFormat("default"));
    }

    @Scheduled(fixedRateString = "${EncoderUnit.fixedRate}")
    public void run(){
        if (!queueMaster.get_queuePUOut().isEmpty()){
            packet packet = queueMaster.get_queuePUOut().poll();
            log.info("EncoderUnit: " + packet.toString());

            encoder.setPacket(packet);

            encoder.run();

            String payload = encoder.getMessage();

            if (payload != null){
                queueMaster.get_queueEncoderOut().add(payload);
                log.info("EncoderUnit added payload to queueEncoderOut");
            }

        }
    }
}