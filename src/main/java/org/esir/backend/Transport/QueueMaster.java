package org.esir.backend.Transport;

import lombok.Getter;
import lombok.Setter;
import org.esir.backend.Requests.packet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Getter
public class QueueMaster {

    private static QueueMaster instance = null;

    private final Queue<String> _queueDecoderIn = new ConcurrentLinkedQueue<String>();

    private final Queue<packet> _queuePUIn = new ConcurrentLinkedQueue<packet>();

    private final Queue<packet> _queuePUOut = new ConcurrentLinkedQueue<packet>();

    private final Queue<String> _queueEncoderOut = new ConcurrentLinkedQueue<String>();

    private QueueMaster() {
    }

    public static QueueMaster getInstance() {
        if (instance == null) {
            instance = new QueueMaster();
        }
        return instance;
    }
}
