package com.czce4013.server;

import com.czce4013.entity.ClientQuery;
import com.czce4013.entity.ServerResponse;
import com.czce4013.marshaller.Marshallable;
import com.czce4013.network.UDPcommunicator;

import java.util.List;

public class Server {
    private UDPcommunicator communicator;

    public Server(){
        communicator = new UDPcommunicator(2222);
    }

    public void run() {
        System.out.println("Running Server...");
        ServerDB database= new ServerDB();
        System.out.println("Server Seeded...");

        while (true){

            ClientQuery query = (ClientQuery)communicator.receive();

            System.out.println(query.toString());

            ServerResponse response= null;

            switch(query.getType()){
                case 1:
                    List flightID = database.findFlightID(query.getSource(),query.getDest());
                    response = new ServerResponse();
                    response.setStatus(200);
                    response.setInfos(flightID);
                    break;
                default:
                    response = new ServerResponse();
                    response.setStatus(404);
                    break;
            }

            communicator.send(response);
            System.out.println(response.toString());
        }
    }

    public static void main(String[] args){
        Server s = new Server();
        s.run();
    }
}

