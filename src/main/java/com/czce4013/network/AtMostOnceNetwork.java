package com.czce4013.network;

import com.czce4013.entity.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AtMostOnceNetwork extends Network {
    public AtMostOnceNetwork(UDPCommunicator communicator) {
        super(communicator);
    }

    private ConcurrentMap<Integer, Long> received = new ConcurrentHashMap<>();
    private final static Logger logger = LoggerFactory.getLogger(AtMostOnceNetwork.class);

    @Override
    protected boolean continueResponse(Response data) {
        if (!received.containsKey(data.getData().getId())) {
            received.put(data.getData().getId(), System.currentTimeMillis());
            return true;
        }
        else {
            long lastReceived = received.get(data.getData().getId());

            if (lastReceived < System.currentTimeMillis() - (5000 + SEND_TIMEOUT * MAX_TRY)) {
                // This is a different message, id have cycled
                received.put(data.getData().getId(), System.currentTimeMillis());
                return true;
            }
            logger.info("Duplicate found: {}", data);
            return false;
        }
    }

}
