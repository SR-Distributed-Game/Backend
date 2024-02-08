package org.esir.backend.IO;

import org.esir.backend.IOFormat.JSONFormat;
import org.esir.backend.Requests.packet;
import org.esir.backend.Transport.QueueMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EncoderUnit {

    private static final Logger log = LoggerFactory.getLogger(EncoderUnit.class);
    private int numthreads = 20;
    ExecutorService executorService = Executors.newFixedThreadPool(numthreads);

    List<encoder> encoders;

    public EncoderUnit() {
        encoders = new ArrayList<encoder>();
        for (int i = 0; i < numthreads; i++){
            encoders.add(new encoder(new JSONFormat("default")));
        }
    }

    @Scheduled(fixedRateString = "${EncoderUnit.fixedRate}")
    public void run(){
        if (!QueueMaster.getInstance().get_queuePUOut().isEmpty()){
            if (QueueMaster.getInstance().get_queuePUOut().size() >= 20){
                log.warn("EncoderUnit: queuePUOut is growing too fast");
                log.warn("EncoderUnit: queuePUOut size: " + QueueMaster.getInstance().get_queuePUOut().size());
            }

            List<packet> payload = new ArrayList<packet>();

            for (int i = 0; i < numthreads; i++){
                if (!QueueMaster.getInstance().get_queuePUOut().isEmpty()){
                    packet message = QueueMaster.getInstance().get_queuePUOut().poll();
                    if (message != null) payload.add(message);
                    else i--;
                }
                else break;
            }

            AtomicInteger IdOnProcess = new AtomicInteger(0);

            for (int i = 0; i < payload.size(); i++){
                final int idThread = i;
                Thread thread = new Thread(() -> runEncoder(encoders.get(idThread), payload.get(idThread), IdOnProcess, idThread));
                executorService.execute(thread);
            }

        }
    }

    private void runEncoder(encoder encoder, packet packet, AtomicInteger IdOnProcess, int idThread){
        encoder.setPacket(packet);
        encoder.run();
        String message = encoder.getMessage();

        while (IdOnProcess.get() != idThread);
        if (message != null && !message.isEmpty()) QueueMaster.getInstance().get_queueEncoderOut().add(message);
        IdOnProcess.incrementAndGet();
    }


}
