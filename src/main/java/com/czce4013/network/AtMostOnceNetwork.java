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
    public void receive(Consumer<Response> callback, int timeout) {

    }

    @Override
    public Response receive() {
        return null;
    }

    @Override
    public void send(Marshallable data, InetSocketAddress dest) {

    }
}
