package com.czce4013.network;

import com.czce4013.marshaller.Marshallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Random;

public class PoorUDPCommunicator extends UDPCommunicator {

    private static final float FAIL_PROB = 0.3F;
    private static final Logger logger = LoggerFactory.getLogger(PoorUDPCommunicator.class);

    private Random random = new Random(System.currentTimeMillis());

    public PoorUDPCommunicator(InetSocketAddress socketAddress) {
        super(socketAddress);
    }

    public PoorUDPCommunicator(int selfPortNumber) {
        super(selfPortNumber);
    }

    @Override
    public void send(Marshallable data, InetSocketAddress dest) {

        float limit = random.nextFloat();
        if (limit >= FAIL_PROB) {
            super.send(data, dest);
        }
        else {
            logger.info("Sending failure simulated for {}", data);
        }
    }
}
