package com.czce4013.network;

import com.czce4013.marshaller.Marshallable;

import java.net.InetSocketAddress;
import java.util.Random;

public class PoorUDPCommunicator extends UDPCommunicator {

    private static final float FAIL_PROB = 0.0F;
    Random random = new Random(System.currentTimeMillis());

    public PoorUDPCommunicator(InetSocketAddress socketAddress) {
        super(socketAddress);
    }

    public PoorUDPCommunicator(int selfPortNumber) {
        super(selfPortNumber);
    }

    @Override
    public void send(Marshallable data) {

        float limit = random.nextFloat();
        if (limit >= FAIL_PROB) {
            super.send(data);
        }
    }
}
