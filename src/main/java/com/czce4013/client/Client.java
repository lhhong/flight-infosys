package com.czce4013.client;

import com.czce4013.entity.ClientQuery;
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
        String LocalIPAddress = UDPCommunicator.getIPaddress();
        InetSocketAddress socketAddress = new InetSocketAddress(LocalIPAddress,2222);
        float failProb = 0.1F;
        if (args.length > 1) {
            failProb =  Float.parseFloat(args[1]);
        }
        UDPCommunicator communicator =  new PoorUDPCommunicator(socketAddress, failProb);
        if (args.length > 0 && args[0].equals("AtMostOnce")) {
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
                unknown1();
                break;
            case 6:
                unknown2();
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

    private void unknown1() {
        //TODO
    }

    private void unknown2() {
        //TODO
    }

}
