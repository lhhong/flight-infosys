package com.czce4013.network;

import com.czce4013.entity.Response;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AtMostOnceNetwork extends Network {

    @AllArgsConstructor
    @EqualsAndHashCode
    static class PacketInfo {
        int id;
        InetSocketAddress origin;
    }

    public AtMostOnceNetwork(UDPCommunicator communicator) {
        super(communicator);
    }

    private ConcurrentMap<PacketInfo, Long> received = new ConcurrentHashMap<>();
    private final static Logger logger = LoggerFactory.getLogger(AtMostOnceNetwork.class);

    @Override
    protected boolean continueResponse(Response data) {
        PacketInfo key = new PacketInfo(data.getData().getId(), data.getOrigin());
        if (!received.containsKey(key)) {
            received.put(key, System.currentTimeMillis());
            return true;
        }
        else {
            long lastReceived = received.get(key);

            if (lastReceived < System.currentTimeMillis() - (3000 + SEND_TIMEOUT * MAX_TRY)) {
                // Given ping uncertainty of 3 sec, this is a different message, id have cycled
                received.put(key, System.currentTimeMillis());
                return true;
            }
            logger.info("Duplicate found: {}", data);
            return false;
        }
    }

}
