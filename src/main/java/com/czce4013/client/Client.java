package com.czce4013.client;

import com.czce4013.network.UDPcommunicator;

import java.util.Arrays;

public class Client {
    private UDPcommunicator communicator;

    public static void main(String[] args){
        Client s = new Client();
        s.connect("192.168.1.71",2222);
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
                findFlightNo(sourceNDest);
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

    private void findFlightNo(String[] SourceNDest) {
        //TODO
        //System.out.println(Arrays.toString(SourceNDest));
        //communicator.send(SourceNDest[0]);
        //String receivedStr = communicator.receive();
        //System.out.println(receivedStr);
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
