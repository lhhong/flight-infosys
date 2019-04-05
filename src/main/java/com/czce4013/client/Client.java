package com.czce4013.client;

import com.czce4013.entity.ClientQuery;
import com.czce4013.entity.FlightInfo;
import com.czce4013.network.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

@AllArgsConstructor
public class Client {
    private Network network;

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args){
        Client c;
        String host = "localhost";
        if (args.length > 0) {
            host = args[0];
        }
        logger.info("Connecting to: {}", host);
        InetSocketAddress socketAddress = new InetSocketAddress(host,2222);
        float failProb = 0.1F;
        if (args.length > 2) {
            failProb =  Float.parseFloat(args[2]);
        }
        UDPCommunicator communicator =  new PoorUDPCommunicator(socketAddress, failProb);
        //UDPCommunicator communicator =  new UDPCommunicator(socketAddress);
        if (args.length > 1 && args[1].equals("AtMostOnce")) {
            c = new Client(new AtMostOnceNetwork(communicator));
            logger.info("Initialized AtMostOnce network with fail probability of {}.", failProb);
        }
        else {
            c = new Client(new AtLeastOnceNetwork(communicator));
            logger.info("Initialized AtLeastOnce network with fail probability of {}.", failProb);
        }
        //noinspection InfiniteLoopStatement
        while (true) {
            c.run();
        }
    }

    private void run() {
        ClientTextUI.printMenu();

        int userOption = ClientTextUI.getUserOption();

        ClientQuery query;
        switch (userOption) {
            case 1:
                query = new ClientQuery();
                String[] sourceNDest = ClientTextUI.getSourceNDest();
                query.setType(1);
                query.getFlight().setSource(sourceNDest[0]);
                query.getFlight().setDest(sourceNDest[1]);
                findFlightNo(query);
                break;
            case 2:
                query = new ClientQuery();
                int flightNo = ClientTextUI.getFlightNo();
                query.setType(2);
                query.getFlight().setFlightId((short) flightNo);
                queryFlightDetails(query);
                break;
            case 3:
                query = new ClientQuery();
                int[] reservationDetails = ClientTextUI.getReservationDetails();
                query.setType(3);
                query.getFlight().setFlightId((short) reservationDetails[0]);
                query.getFlight().setSeatsAvailable((short) reservationDetails[1]);
                makeReservation(query);
                break;
            case 4:
                query = new ClientQuery();
                int[] monitoringDetails = ClientTextUI.monitorFlight();
                query.setType(4);
                query.setTimeout(monitoringDetails[1]);
                query.getFlight().setFlightId((short) monitoringDetails[0]);
                monitorFlight(query);
                break;
            case 5:
                query = new ClientQuery();
                String source = ClientTextUI.getSource();
                query.setType(5);
                query.getFlight().setSource(source);
                findFlights(query);
                break;
            case 6:
                query = new ClientQuery();
                float surchargePercentage = ClientTextUI.getSurchargePercentage();
                query.setType(6);
                FlightInfo f = new FlightInfo();
                f.setFare(surchargePercentage);
                query.setFlight(f);
                applySurcharge(query);
                break;
            case 7:
                System.exit(0);
            default:
                System.out.println("##### Invalid input #####");
        }
    }

    private void findFlightNo(ClientQuery query) {
        int id = network.send(query);
        network.receive(id, (response) -> {
            if (response.getStatus() == 200){
                ClientTextUI.printFlightID(query,response);
            }
            else{
                ClientTextUI.printErrorMessage(response);
            }
        }, false, 5);
    }

    private void queryFlightDetails(ClientQuery query) {
        int id = network.send(query);
        network.receive(id, (response) -> {
            if (response.getStatus() == 200) {
                ClientTextUI.printFlightDetails(query, response);
            } else {
                ClientTextUI.printErrorMessage(response);
            }
        }, false, 5);
    }

    private void makeReservation(ClientQuery query) {
        int id = network.send(query);
        network.receive(id, (response) -> {
            if (response.getStatus() == 200) {
                ClientTextUI.printReservationConfirmation(query, response);
            } else {
                ClientTextUI.printErrorMessage(response);
            }
        }, false, 5);
    }

    private void monitorFlight(ClientQuery query) {
        int id = network.send(query);
        network.receive(id, (response) -> {
            if (response.getStatus() == 200){
                ClientTextUI.printFlightUpdate(query,response);
            }
            else{
                ClientTextUI.printErrorMessage(response);
            }
        }, true, query.getTimeout());
    }
    //idempotent - List flights from location according to price
    private void findFlights(ClientQuery query) {
        int id = network.send(query);
        network.receive(id, (response) -> {
            if (response.getStatus() == 200) {
                ClientTextUI.printFlightsFromSource(query, response);
            } else {
                ClientTextUI.printErrorMessage(response);
            }
        }, false, 5);
    }

    //non-idempotent
    private void applySurcharge(ClientQuery query) {
        int id = network.send(query);
        network.receive(id, (response) -> {
            if (response.getStatus() == 200) {
                ClientTextUI.printServerResponse();
            } else {
                ClientTextUI.printErrorMessage(response);
            }
        }, false, 5);
    }

    public void testClient(int num){
        ClientQuery query = new ClientQuery();
        query.setType(1);
        query.getFlight().setSource("SINGAPORE");
        query.getFlight().setDest("BANGKOK");
        for (int i=0;i<num;i++){
            int id = network.send(query);
            network.receive(id, (response) -> {
//                if (response.getStatus() == 200) {
//                    ClientTextUI.printServerResponse();
//                } else {
//                    ClientTextUI.printErrorMessage(response);
//                }
            }, false, 5);
        }
    }

    public void testClient2(int num){
        ClientQuery query = new ClientQuery();

        query = new ClientQuery();
        float surchargePercentage = 10.0f;
        query.setType(6);
        FlightInfo f = new FlightInfo();
        f.setFare(surchargePercentage);
        query.setFlight(f);

        for (int i=0;i<num;i++){
            int id = network.send(query);
            network.receive(id, (response) -> {
//                if (response.getStatus() == 200) {
//                    ClientTextUI.printServerResponse();
//                } else {
//                    ClientTextUI.printErrorMessage(response);
//                }
            }, false, 5);
        }

        ClientQuery query2 = new ClientQuery();
        query2.setType(2);
        query2.getFlight().setFlightId((short) 1001);
        queryFlightDetails(query2);

    }

}
