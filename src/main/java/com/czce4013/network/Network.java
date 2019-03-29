package com.czce4013.network;

import com.czce4013.entity.Response;
import com.czce4013.marshaller.Marshallable;
import lombok.AllArgsConstructor;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

public abstract class Network {
    UDPCommunicator communicator;

    public Network(UDPCommunicator communicator) {
        this.communicator = communicator;
    }

    public abstract void receive(Consumer<Response> callback, int timeout);
    public abstract Response receive();

    public abstract void send(Marshallable data, InetSocketAddress dest);

    public void send(Marshallable data) {
        send(data, communicator.getSocketAddress());
    }

}
