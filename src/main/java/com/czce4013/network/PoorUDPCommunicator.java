package com.czce4013.network;

import com.czce4013.marshaller.Marshallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Random;

public class PoorUDPCommunicator extends UDPCommunicator {

    private float failProb;
    private static final Logger logger = LoggerFactory.getLogger(PoorUDPCommunicator.class);

    private Random random = new Random(System.currentTimeMillis());

    public PoorUDPCommunicator(InetSocketAddress socketAddress, float failProb) {
        super(socketAddress);
        this.failProb = failProb;
    }

    public PoorUDPCommunicator(int selfPortNumber, float failProb) {
        super(selfPortNumber);
        this.failProb = failProb;
    }

    @Override
    public void send(Marshallable data, InetSocketAddress dest) {

        float limit = random.nextFloat();
        if (limit >= failProb) {
            super.send(data, dest);
        }
        else {
            //logger.info("Sending failure simulated for {}", data);
        }
    }
}
