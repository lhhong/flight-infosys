package com.czce4013.client;

import com.czce4013.entity.ClientQuery;
import com.czce4013.entity.FlightInfo;
import com.czce4013.entity.ServerResponse;

import java.util.HashMap;
import java.util.Scanner;

public class ClientTextUI {

    public static Scanner keyboardScanner = new Scanner(System.in);

    public static void printMenu(){
        System.out.println("\n========== CE 4013 Flight Management System ==========");
        System.out.println("Choose item:");
        System.out.println("[1] Find Flight Number");
        System.out.println("[2] Query Flight Details");
        System.out.println("[3] Make Flight Reservation");
        System.out.println("[4] Monitor Flight");
        System.out.println("[5] ?????");
        System.out.println("[6] ??????");
        System.out.println("[7] Exit Application");
    }

    public static int getUserOption(){
        int userOption;
        try{
            userOption = keyboardScanner.nextInt();
        }
        catch(Exception e){
            userOption = 0;
        }
        return userOption;
    }

    public static String[] getSourceNDest() {
        keyboardScanner.nextLine();
        String[] sourceNDest = new String[2];
        System.out.println("\n========== [1] Find Flight Number ==========");
        System.out.println("Enter Source Location: ");
        sourceNDest[0] = keyboardScanner.nextLine();

        System.out.println("Enter Destination Location: ");
        sourceNDest[1] = keyboardScanner.nextLine();

        return sourceNDest;
    }

    public static void printFlightID(ClientQuery query, ServerResponse response) {
        System.out.println("\n=================================================");
        System.out.println("================ Server Response ================" );
        System.out.println("=================================================");
        System.out.println("QUERY:");
        String format = "%-40s%s%n";
        System.out.printf(format, "Source:", query.getSource());
        System.out.printf(format, "Destination:", query.getDest());
        System.out.println("=================================================");
        System.out.println("FLIGHT IDs:");
        for (int i = 0; i<response.getInfos().size();i++){
            System.out.println("["+i+"] "+response.getInfos().get(i).getId());
        }
        System.out.println("=================================================");
    }

    public static int getFlightNo() {
        System.out.println("\n========== [2] Query Flight Details ==========");
        System.out.println("Enter Flight Number: ");
        return keyboardScanner.nextInt();
    }

    public static int[] getReservationDetails() {
        int[] reservationDetails = new int[2];
        System.out.println("\n========== [3] Make Flight Reservation ==========");
        System.out.println("Enter Flight Number: ");
        reservationDetails[0] =  keyboardScanner.nextInt();
        System.out.println("Enter number of seats to reserve: ");
        reservationDetails[1] =  keyboardScanner.nextInt();
        return reservationDetails;
    }

    public static void printErrorMessage(ServerResponse response) {
        System.out.println("\n=================================================");
        System.out.println("============== ERROR CODE "+ response.getStatus()+ " ==============" );
        System.out.println("\n=================================================");
        switch (response.getStatus()){
            case 404:
                System.out.println("Server Error");
                System.out.println("\n=================================================");
                break;
            default:
                System.out.println("Unknown Error");
                System.out.println("\n=================================================");
                break;
        }

    }

}
