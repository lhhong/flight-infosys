package com.czce4013.server;

import com.czce4013.entity.ClientInfo;
import com.czce4013.entity.FlightInfo;
import com.czce4013.entity.ServerResponse;
import com.czce4013.network.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Server {
    private Network network;

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

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
                    //response = new ServerResponse();
                    response = new ServerResponse(query.getId(), 200, flightID);
                    network.send(response, origin);
                    System.out.println(response.toString());
                    break;
                case 2:
                    flightID = database.getFlightDetails(query.getFlight().getFlightId());
                    response = new ServerResponse(query.getId(), 200, flightID);
                    network.send(response, origin);
                    System.out.println(response.toString());
                    break;
                case 3:
                    flightID = database.makeReservation(query.getFlight().getFlightId(), query.getFlight().getSeatsAvailable());
                    response = new ServerResponse(query.getId(), 200, flightID);
                    network.send(response, origin);
                    reservationCallback(response);
                    System.out.println(response.toString());
                    break;
                case 4:
                    ClientInfo clientInfo = new ClientInfo(query.getId(), origin, query.getTimeout());
                    database.observeFlight(query.getFlight().getFlightId(), clientInfo);
                    break;
                case 5:
                    flightID = database.getFlightsFrom(query.getFlight().getSource());
                    response = new ServerResponse(query.getId(), 200, flightID);
                    network.send(response, origin);
                    System.out.println(response.toString());
                    break;
                case 6:
                    database.applySurcharge(query.getFlight().getFare());
                    response = new ServerResponse(query.getId(), 200, new ArrayList<>());
                    network.send(response, origin);
                    System.out.println(response.toString());
                    break;
                default:
                    response = new ServerResponse(query.getId(), 404, null);
                    network.send(response, origin);
                    break;
            }
        });
    }

    private void reservationCallback(ServerResponse response) {
        int flightID = response.getInfos().get(0).getFlightId();
        ServerDB.filterCallBackAddresses();
        List<ClientInfo> clientInfos = ServerDB.getCallBackAddresses(flightID);
        if (clientInfos == null) { return; }
        for (ClientInfo clientInfo: clientInfos) {
            ServerResponse dupResponse = response.clone();
            dupResponse.setQueryId(clientInfo.getQueryId());
            network.send(dupResponse, (InetSocketAddress) clientInfo.getSocket());
        }
    }

    public static void main(String[] args){
        Server s;
        float failProb = 0.1F;
        if (args.length > 1) {
            failProb =  Float.parseFloat(args[1]);
        }
        UDPCommunicator communicator =  new PoorUDPCommunicator(2222, failProb);
        //UDPCommunicator communicator =  new UDPCommunicator(2222);
        if (args.length > 0 && args[0].equals("AtMostOnce")) {
            s = new Server(new AtMostOnceNetwork(communicator));
            logger.info("Initialized AtMostOnce network with fail probability of {}.", failProb);
        }
        else {
            s = new Server(new AtLeastOnceNetwork(communicator));
            logger.info("Initialized AtLeastOnce network with fail probability of {}.", failProb);
        }
        s.run();
    }
}

