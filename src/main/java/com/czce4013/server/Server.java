package com.czce4013.server;

import com.czce4013.entity.ClientQuery;
import com.czce4013.entity.Response;
import com.czce4013.entity.ServerResponse;
import com.czce4013.network.AtLeastOnceNetwork;
import com.czce4013.network.Network;
import com.czce4013.network.PoorUDPCommunicator;
import com.czce4013.network.UDPCommunicator;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private Network network;

    public Server(){
        network = new AtLeastOnceNetwork(new PoorUDPCommunicator(2222));
    }

    public void run() {
        System.out.println("Running Server...");
        ServerDB database= new ServerDB();
        System.out.println("Server Seeded...");

        while (true){

            Response resp = network.receive();
            ClientQuery query = (ClientQuery) resp.getData();

            System.out.println(query.toString());

            ServerResponse response= null;
            List flightID = null;

            switch(query.getType()) {
                case 1:
                    flightID = database.findFlightID(query.getFlight().getSource(), query.getFlight().getDest());
                    response = new ServerResponse();
                    response.setStatus(200);
                    response.setInfos(flightID);
                    network.send(response, resp.getOrigin());
                    System.out.println(response.toString());
                    break;
                case 2:
                    flightID = database.getFlightDetails(query.getFlight().getId());
                    response = new ServerResponse();
                    response.setStatus(200);
                    response.setInfos(flightID);
                    network.send(response, resp.getOrigin());
                    System.out.println(response.toString());
                    break;
                case 3:
                    flightID = database.makeReservation(query.getFlight().getId(), query.getFlight().getSeatsAvailable());
                    response = new ServerResponse();
                    response.setStatus(200);
                    response.setInfos(flightID);
                    network.send(response, resp.getOrigin());
                    reservationCallback(response);
                    System.out.println(response.toString());
                    break;
                case 4:
                    database.observeFlight(query.getFlight().getId(), resp.getOrigin(), query.getTimeout());
                    break;
                default:
                    response = new ServerResponse();
                    response.setStatus(404);
                    break;
            }
        }
    }

    private void reservationCallback(ServerResponse response) {
        int flightID = response.getInfos().get(0).getId();
        ServerDB.filterCallBackAddresses();
        List<SocketAddress> addresses = ServerDB.getCallBackAddresses(flightID);
        for (SocketAddress address: addresses){
            network.send(response, (InetSocketAddress) address);
        }
    }

    public static void main(String[] args){
        Server s = new Server();
        s.run();
    }
}

