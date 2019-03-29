package com.czce4013.client;

import com.czce4013.entity.ClientQuery;
import com.czce4013.entity.FlightInfo;
import com.czce4013.entity.ServerResponse;
import com.czce4013.network.UDPcommunicator;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.HashMap;

public class Client {
    private UDPcommunicator communicator;

    public static void main(String[] args){
        Client s = new Client();
        String LocalIPAddress = UDPcommunicator.getIPaddress();
        InetSocketAddress socketAddress = new InetSocketAddress(LocalIPAddress,2222);
        s.connect(socketAddress);
        while (true) {
            s.run();
        }
    }

    public void run() {
        ClientTextUI.printMenu();

        int userOption = ClientTextUI.getUserOption();

        ClientQuery query = null;
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
                query.getFlight().setId(flightNo);
                queryFlightDetails(query);
                break;
            case 3:
                query = new ClientQuery();
                int[] reservationDetails = ClientTextUI.getReservationDetails();
                query.setType(3);
                query.getFlight().setId(reservationDetails[0]);
                query.getFlight().setSeatsAvailable(reservationDetails[1]);
                makeReservation(query);
                break;
            case 4:
                query = new ClientQuery();
                int flightNum = ClientTextUI.monitorFlight();
                query.setType(4);
                query.getFlight().setId(flightNum);
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
        communicator.send(query);
        ServerResponse response = (ServerResponse)communicator.receive();
        if (response.getStatus() == 200){
            ClientTextUI.printFlightID(query,response);
        }
        else{
            ClientTextUI.printErrorMessage(response);
        }
    }

    private void queryFlightDetails(ClientQuery query) {
        communicator.send(query);
        ServerResponse response = (ServerResponse)communicator.receive();
        if (response.getStatus() == 200){
            ClientTextUI.printFlightDetails(query,response);
        }
        else{
            ClientTextUI.printErrorMessage(response);
        }
    }

    private void makeReservation(ClientQuery query) {
        communicator.send(query);
        ServerResponse response = (ServerResponse)communicator.receive();
        if (response.getStatus() == 200){
            ClientTextUI.printReservationConfirmation(query,response);
        }
        else{
            ClientTextUI.printErrorMessage(response);
        }
    }

    private void monitorFlight(ClientQuery query) {
        communicator.send(query);
        ServerResponse response = (ServerResponse)communicator.receive();
        if (response.getStatus() == 200){
            ClientTextUI.printFlightUpdate(query,response);
        }
        else{
            ClientTextUI.printErrorMessage(response);
        }
    }

    private void unknown1() {
        //TODO
    }

    private void unknown2() {
        //TODO
    }

    private void connect (InetSocketAddress socketAddress){
        communicator = new UDPcommunicator(socketAddress);
    }
}
