package org.esir.backend.IO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
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
public class DecoderUnit {
    private static final Logger log = LoggerFactory.getLogger(DecoderUnit.class);
    private int numthreads = 5;
    ExecutorService executorService = Executors.newFixedThreadPool(numthreads);
    List<decoder> decoders;

    private volatile boolean running = true;


    @PostConstruct
    public void init() {
        Thread loopThread = new Thread(this::runLoop);
        loopThread.start();
    }

    @PreDestroy
    public void destroy() {
        running = false;
    }

    private void runLoop() {
        while (running) {
            run();
        }
    }

    public DecoderUnit() {
        decoders = new ArrayList<decoder>();
        for (int i = 0; i < numthreads; i++){
            decoders.add(new decoder(new JSONFormat("default")));
        }
    }


    public void run(){
        if (!QueueMaster.getInstance().get_queueDecoderIn().isEmpty()){

            if (QueueMaster.getInstance().get_queueDecoderIn().size() >= 20){
                log.warn("EncoderUnit: queueDecodeIN is growing too fast");
                log.warn("EncoderUnit: queueDecodeIN size: " + QueueMaster.getInstance().get_queueDecoderIn().size());
            }

            List<String> payload = new ArrayList<String>();
            for (int i = 0; i < numthreads; i++){
                if (!QueueMaster.getInstance().get_queueDecoderIn().isEmpty()){
                    String message = QueueMaster.getInstance().get_queueDecoderIn().poll();
                    if (message != null) payload.add(message);
                    else i--;
                }
                else break;
            }

            AtomicInteger IdOnProcess = new AtomicInteger(0);

            for (int i = 0; i < payload.size(); i++){
                final int idThread = i;
                Thread thread = new Thread(() -> runDecoder(decoders.get(idThread), payload.get(idThread), IdOnProcess, idThread));
                executorService.execute(thread);
            }
        }
    }

    private void runDecoder(decoder decoder, String payload, AtomicInteger IdOnProcess, int idThread){
        decoder.setMessage(payload);
        decoder.run();
        packet packet = decoder.getPackets();

        while (IdOnProcess.get() != idThread);
        if (packet != null) QueueMaster.getInstance().get_queuePUIn().add(packet);
        IdOnProcess.incrementAndGet();
    }
}
