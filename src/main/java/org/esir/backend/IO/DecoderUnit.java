package org.esir.backend.IO;
import org.esir.backend.IOFormat.JSONFormat;
import org.esir.backend.Requests.packet;
import org.esir.backend.Transport.QueueMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DecoderUnit {
    private static final Logger log = LoggerFactory.getLogger(DecoderUnit.class);

    private decoder decoder;

    public DecoderUnit() {
        decoder = new decoder(new JSONFormat("default"));
    }


    @Scheduled(fixedRateString = "${DecoderUnit.fixedRate}")
    public void run(){
        if (!QueueMaster.getInstance().get_queueDecoderIn().isEmpty()){
            String payload = QueueMaster.getInstance().get_queueDecoderIn().poll();

            decoder.setMessage(payload);

            decoder.run();

            packet packet = decoder.getPackets();

            if (packet != null){
                QueueMaster.getInstance().get_queuePUIn().add(packet);
            }
        }
    }
}
