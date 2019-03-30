package com.czce4013.server;

import com.czce4013.entity.ClientInfo;
import com.czce4013.entity.FlightInfo;
import com.czce4013.entity.ServerResponse;
import com.czce4013.network.AtLeastOnceNetwork;
import com.czce4013.network.Network;
import com.czce4013.network.PoorUDPCommunicator;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
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


        network.receive((origin, query) -> {

            System.out.println(query.toString());

            ServerResponse response= null;
            List<FlightInfo> flightID = null;

            switch(query.getType()) {
                case 1:
                    flightID = database.findFlightID(query.getFlight().getSource(), query.getFlight().getDest());
                    response = new ServerResponse();
                    response.setQueryId(query.getId());
                    response.setStatus(200);
                    response.setInfos(flightID);
                    network.send(response, origin);
                    System.out.println(response.toString());
                    break;
                case 2:
                    flightID = database.getFlightDetails(query.getFlight().getFlightId());
                    response = new ServerResponse();
                    response.setQueryId(query.getId());
                    response.setStatus(200);
                    response.setInfos(flightID);
                    network.send(response, origin);
                    System.out.println(response.toString());
                    break;
                case 3:
                    flightID = database.makeReservation(query.getFlight().getFlightId(), query.getFlight().getSeatsAvailable());
                    response = new ServerResponse();
                    response.setQueryId(query.getId());
                    response.setStatus(200);
                    response.setInfos(flightID);
                    network.send(response, origin);
                    reservationCallback(response.clone());
                    System.out.println(response.toString());
                    break;
                case 4:
                    ClientInfo clientInfo = new ClientInfo(query.getId(), origin, query.getTimeout());
                    database.observeFlight(query.getFlight().getFlightId(), clientInfo);
                    break;
                default:
                    response = new ServerResponse();
                    response.setQueryId(query.getId());
                    response.setStatus(404);
                    break;
            }
        });
    }

    private void reservationCallback(ServerResponse response) {
        int flightID = response.getInfos().get(0).getFlightId();
        ServerDB.filterCallBackAddresses();
        List<ClientInfo> clientInfos = ServerDB.getCallBackAddresses(flightID);
        for (ClientInfo clientInfo: clientInfos) {
            response.setQueryId(clientInfo.getQueryId());
            network.send(response, (InetSocketAddress) clientInfo.getSocket());
        }
    }

    public static void main(String[] args){
        Server s = new Server();
        s.run();
    }
}

