package com.czce4013.network;

import com.czce4013.entity.Response;
import com.czce4013.marshaller.Marshallable;
import lombok.AllArgsConstructor;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

public class AtLeastOnceNetwork extends Network {

    public AtLeastOnceNetwork(UDPCommunicator communicator) {
        super(communicator);
    }

    @Override
    public void receive(Consumer<Response> callback, int timeout) {
        communicator.receive(callback, timeout);
    }

    @Override
    public Response receive() {
        return communicator.receive();
    }

    @Override
    public void send(Marshallable data, InetSocketAddress dest) {
        communicator.send(data, dest);
    }
}
