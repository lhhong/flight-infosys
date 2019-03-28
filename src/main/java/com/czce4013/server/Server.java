package com.czce4013.server;

import com.czce4013.entity.ClientQuery;
import com.czce4013.entity.ServerResponse;
import com.czce4013.marshaller.Marshallable;
import com.czce4013.network.UDPcommunicator;

public class Server {
    private UDPcommunicator communicator;

    public Server(){
        communicator = new UDPcommunicator(2222);
    }

    public void run() {
        System.out.println("Running Server...");

        while (true){

            ClientQuery query = (ClientQuery)communicator.receive();

            System.out.println(query.toString());

            ServerResponse response= null;

            switch(query.getType()){
                case 1:
                    response = new ServerResponse();
                    response.setStatus(200);
                    break;
                default:
                    response = new ServerResponse();
                    response.setStatus(404);
                    break;
            }

            communicator.send(response);
        }
    }

    public static void main(String[] args){
        Server s = new Server();
        s.run();
    }
}

