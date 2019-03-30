package com.czce4013.network;

import com.czce4013.entity.Ack;
import com.czce4013.entity.ClientQuery;
import com.czce4013.entity.Response;
import com.czce4013.entity.ServerResponse;

import java.util.function.Consumer;

public class AtLeastOnceNetwork extends Network {


    public AtLeastOnceNetwork(UDPCommunicator communicator) {
        super(communicator);
    }

    protected void runReceiver() {
        Thread t = new Thread(() -> {
            while (true) {
                Response resp = communicator.receive();
                if (resp.getData() instanceof Ack) {
                    Ack ack = (Ack) resp.getData();
                    Consumer<Ack> c = acks.get(ack.getAckId());
                    if (c == null) continue; // Already acked
                    c.accept(ack);
                }
                else if (resp.getData() instanceof ServerResponse) {
                    ServerResponse serverResponse = (ServerResponse) resp.getData();
                    Consumer<ServerResponse> c = callbacks.get(serverResponse.getQueryId());
                    if (c == null) {
                        sendAck(serverResponse.getId(), resp.getOrigin());
                        continue; //Results were already displayed
                    }
                    c.accept(serverResponse);
                    if (threadsToBreak.containsKey(serverResponse.getQueryId())) {
                        threadsToBreak.get(serverResponse.getQueryId()).interrupt();
                    }
                    sendAck(serverResponse.getId(), resp.getOrigin());
                }
                else if (resp.getData() instanceof ClientQuery) {
                    ClientQuery clientQuery = (ClientQuery) resp.getData();
                    serverAction.accept(resp.getOrigin(), clientQuery);
                    sendAck(clientQuery.getId(), resp.getOrigin());
                }
            }
        });
        t.start();
    }
}
