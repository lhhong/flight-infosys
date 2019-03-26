package com.czce4013.client;

import java.util.Scanner;

public class Client {
    public Client(){

    }

    public void run() {
        printMenu();

        Scanner keyboardScanner = new Scanner(System.in);
        int userOption;
        try{
             userOption = keyboardScanner.nextInt();
        }
        catch(Exception e){
            userOption = 0;
        }

        switch (userOption) {
            case 1:
                findFlightNo();
                break;
            case 2:
                queryFlightDetails();
                break;
            case 3:
                makeReservation();
                break;
            case 4:
                monitorFlight();
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

    public static void main(String[] args){
        Client s = new Client();
        while (true) {
            s.run();
        }
    }

    private void printMenu(){
        System.out.println("========== CE 4013 Flight Management System ==========");
        System.out.println("Choose item:");
        System.out.println("[1] Find Flight Number");
        System.out.println("[2] Query Flight Details");
        System.out.println("[3] Make Flight Reservation");
        System.out.println("[4] Monitor Flight");
        System.out.println("[5] ?????");
        System.out.println("[6] ??????");
        System.out.println("[7] Exit Application");
    }


    private void findFlightNo() {
        //TODO
    }

    private void queryFlightDetails() {
        //TODO
    }

    private void makeReservation() {
        //TODO
    }

    private void monitorFlight() {
        //TODO
    }

    private void unknown1() {
        //TODO
    }

    private void unknown2() {
        //TODO
    }
}
