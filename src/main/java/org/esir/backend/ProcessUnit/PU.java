package org.esir.backend.ProcessUnit;

import org.esir.backend.Requests.packet;
import org.esir.backend.Transport.QueueMaster;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class PU{

    private static final Logger log = LoggerFactory.getLogger(PU.class);

    private final QueueMaster queueMaster;

    public PU(){
        queueMaster = QueueMaster.getInstance();
    }

    @Scheduled(fixedRateString = "${pu.fixedRate}")
    public void run(){
        if(!queueMaster.get_queuePUIn().isEmpty()){
            packet packet = queueMaster.get_queuePUIn().poll();
            queueMaster.get_queuePUOut().add(packet);
        }
    }
}
