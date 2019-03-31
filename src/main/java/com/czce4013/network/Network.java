package com.czce4013.network;

import com.czce4013.entity.Ack;
import com.czce4013.entity.ClientQuery;
import com.czce4013.entity.ServerResponse;
import com.czce4013.marshaller.Marshallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Network {
    UDPCommunicator communicator;
    private final IdContainer idGen = new IdContainer();
    private static final long SEND_TIMEOUT = 1000;
    private static final int MAX_TRY = 5;
    private static final Logger logger = LoggerFactory.getLogger(Network.class);

    ConcurrentMap<Integer, Consumer<ServerResponse>> callbacks = new ConcurrentHashMap<>();
    ConcurrentMap<Integer, Thread> threadsToBreak = new ConcurrentHashMap<>();
    ConcurrentMap<Integer, Consumer<Ack>> acks = new ConcurrentHashMap<>();
    BiConsumer<InetSocketAddress, ClientQuery> serverAction;

    Network(UDPCommunicator communicator) {
        this.communicator = communicator;
        runReceiver();
    }

    protected abstract void runReceiver();

    void sendAck(int ackId, InetSocketAddress dest) {
        Ack ack = new Ack(ackId);
        int id = idGen.get();
        ack.setId(id);
        communicator.send(ack, dest);
    }

    public void receive(int id, Consumer<ServerResponse> callback, boolean continuous, int blockTime) {
        callbacks.put(id, callback);
        if (!continuous) {
            threadsToBreak.put(id, Thread.currentThread());
        }
        try {
            Thread.sleep(blockTime*1000);
        } catch (InterruptedException ignored) {}
        callbacks.remove(id);
        if (!continuous) {
            threadsToBreak.remove(id);
        }
    }

    public void receive(BiConsumer<InetSocketAddress, ClientQuery> serverOps) {
        this.serverAction = serverOps;
        Thread.yield();
    }

    public int send(Marshallable data, InetSocketAddress dest) {
        int id = idGen.get();
        data.setId(id);
        Thread t = new Thread(() -> {
            try {
                 for (int i = 0; i < MAX_TRY; i++) {
                    communicator.send(data, dest);
                    Thread.sleep(SEND_TIMEOUT);
                 }
                 logger.error("Failed to send after {} tries: {}", MAX_TRY, data);
            } catch (InterruptedException ignored) {}
        });

        acks.put(id, ack -> {
            t.interrupt();
            acks.remove(id);
        });
        t.start();

        return id;
    }

    public int send(Marshallable data) {
        return send(data, communicator.getServerSocket());
    }

}
