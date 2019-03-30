package com.czce4013.network;

import com.czce4013.entity.Response;
import com.czce4013.marshaller.Marshallable;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

public class AtMostOnceNetwork extends Network {
    public AtMostOnceNetwork(UDPCommunicator communicator) {
        super(communicator);
    }

    @Override
    protected void runReceiver() {

    }

}
