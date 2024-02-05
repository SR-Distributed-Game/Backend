package org.esir.backend.IO;
import org.esir.backend.Requests.packet;
import org.esir.backend.Transport.QueueMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DecoderUnit {
    private static final Logger log = LoggerFactory.getLogger(DecoderUnit.class);

    private QueueMaster queueMaster;

    private decoder decoder;

    public DecoderUnit() {
        queueMaster = QueueMaster.getInstance();
        decoder = new decoder(new JSONFormat("default"));
    }



    @Scheduled(fixedRateString = "${DecoderUnit.fixedRate}")
    public void run(){
        if (!queueMaster.get_queueDecoderIn().isEmpty()){
            String payload = queueMaster.get_queueDecoderIn().poll();
            log.info("DecoderUnit: " + payload);

            decoder.setMessage(payload);

            decoder.run();

            packet packet = decoder.getPackets();

            if (packet != null){
                queueMaster.get_queuePUIn().add(packet);
                log.info("DecoderUnit added packet to queuePUIn");
            }
        }
    }
}
