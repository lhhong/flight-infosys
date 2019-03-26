package com.czce4013.client;

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
        String[] SourceNDestination = new String[2];
        System.out.println("\n========== [1] Find Flight Number ==========");
        System.out.println("Enter Source Location: ");
        SourceNDestination[0] = keyboardScanner.nextLine();

        System.out.println("Enter Destination Location: ");
        SourceNDestination[1] = keyboardScanner.nextLine();

        return SourceNDestination;
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
}
