package com.czce4013.client;

import com.czce4013.entity.ClientQuery;
import com.czce4013.entity.FlightInfo;
import com.czce4013.entity.ServerResponse;
import com.czce4013.network.UDPcommunicator;

import java.util.Arrays;
import java.util.HashMap;

public class Client {
    private UDPcommunicator communicator;

    public static void main(String[] args){
        Client s = new Client();
        s.connect("172.20.106.85",2222);
        while (true) {
            s.run();
        }
    }

    public void run() {
        ClientTextUI.printMenu();

        int userOption = ClientTextUI.getUserOption();

        switch (userOption) {
            case 1:
                String[] sourceNDest = ClientTextUI.getSourceNDest();
                ClientQuery query = new ClientQuery();
                query.setType(1);
                query.setSource(sourceNDest[0]);
                query.setDest(sourceNDest[1]);
                findFlightNo(query);
                break;
            case 2:
                int flightNo = ClientTextUI.getFlightNo();
                queryFlightDetails(flightNo);
                break;
            case 3:
                int[] reservationDetails = ClientTextUI.getReservationDetails();
                makeReservation(reservationDetails);
                break;
            case 4:
                int flightNum = ClientTextUI.getFlightNo();
                monitorFlight(flightNum);
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
        //TODO
        communicator.send(query);
        ServerResponse response = (ServerResponse)communicator.receive();
        if (response.getStatus() == 200){
            ClientTextUI.printFlightID(query,response);
        }
        else{
            ClientTextUI.printErrorMessage(response);
        }

    }

    private void queryFlightDetails(int flightNo) {
        //TODO
        System.out.println(flightNo);
    }

    private void makeReservation(int[] reservationDetails) {
        //TODO
        System.out.println(Arrays.toString(reservationDetails));
    }

    private void monitorFlight(int flightNo) {
        //TODO
        System.out.println(flightNo);
    }

    private void unknown1() {
        //TODO
    }

    private void unknown2() {
        //TODO
    }

    private void connect (String ipAddress,int portNumber){
        communicator = new UDPcommunicator(ipAddress,portNumber);
    }
}
