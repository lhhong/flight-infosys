package com.czce4013.client;

import com.czce4013.entity.ClientQuery;
import com.czce4013.entity.FlightInfo;
import com.czce4013.entity.ServerResponse;

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
        System.out.println("[5] Find Flights From Location");
        System.out.println("[6] Apply Surcharge to Flights");
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
        sourceNDest[0] = keyboardScanner.nextLine().toUpperCase().trim();

        System.out.println("Enter Destination Location: ");
        sourceNDest[1] = keyboardScanner.nextLine().toUpperCase().trim();

        return sourceNDest;
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

    public static int[] monitorFlight() {
        int[] monitoringDetails = new int[2];
        System.out.println("\n========== [4] Monitor Flight ==========");
        System.out.println("Enter Flight Number: ");
        monitoringDetails[0] =  keyboardScanner.nextInt();
        System.out.println("Enter time to monitor (seconds): ");
        monitoringDetails[1] =  keyboardScanner.nextInt();
        return monitoringDetails;
    }

    public static String getSource() {
        keyboardScanner.nextLine();
        System.out.println("\n========== [5] Find Flights From Location ==========");
        System.out.println("Enter Source Location: ");
        return keyboardScanner.nextLine().toUpperCase().trim();
    }

    public static float getSurchargePercentage() {
        System.out.println("\n========== [6] Apply Surcharge ==========");
        System.out.println("Surcharge percentage: ");
        return keyboardScanner.nextFloat();
    }

    public static void printFlightID(ClientQuery query, ServerResponse response) {
        printServerResponse();
        System.out.println("QUERY:");
        String format = "%-40s%s%n";
        System.out.printf(format, "Source:", query.getFlight().getSource());
        System.out.printf(format, "Destination:", query.getFlight().getDest());
        System.out.println("=================================================");
        System.out.println("FLIGHT IDs:");
        for (int i = 0; i<response.getInfos().size();i++){
            System.out.println("["+i+"] "+response.getInfos().get(i).getFlightId());
        }
        System.out.println("=================================================");
    }

    public static void printFlightDetails(ClientQuery query, ServerResponse response) {
        printServerResponse();
        System.out.println("QUERY:");
        String format = "%-40s%s%n";
        System.out.printf(format, "Flight ID:", query.getFlight().getFlightId());
        printFlightDetails(response);
    }


    public static void printReservationConfirmation(ClientQuery query, ServerResponse response) {
        printServerResponse();
        System.out.println("QUERY:");
        String format = "%-40s%s%n";
        System.out.printf(format, "Flight ID:", query.getFlight().getFlightId());
        System.out.printf(format, "Seats to Reserve:", query.getFlight().getSeatsAvailable());
        System.out.println("RESERVATION SUCCESSFUL!");
        printFlightDetails(response);
    }

    public static void printFlightUpdate(ClientQuery query, ServerResponse response) {
        printServerResponse();
        System.out.println("QUERY:");
        String format = "%-40s%s%n";
        System.out.printf(format, "Flight ID:", query.getFlight().getFlightId());
        System.out.println("FLIGHT HAS BEEN UPDATED!");
        printFlightDetails(response);
    }

    public static void printFlightDetails(ServerResponse response){
        String format = "%-40s%s%n";
        for (FlightInfo flight : response.getInfos()){
            System.out.println("=================================================");
            System.out.println("FLIGHT DETAILS:");
            System.out.printf(format, "Flight ID:", flight.getFlightId());
            System.out.printf(format, "Source:", flight.getSource());
            System.out.printf(format, "Destination:", flight.getDest());
            System.out.printf(format, "Date & Time:", flight.getDateTime().toNiceString());
            System.out.printf("%-40s%s%.2f%n", "AirFare:","$", flight.getFare());
            System.out.printf(format, "Seats Available:", flight.getSeatsAvailable());
            System.out.println("=================================================");
        }
    }

    public static void printFlightsFromSource(ClientQuery query, ServerResponse response) {
        printServerResponse();
        String format = "%-40s%s%n";
        System.out.println("QUERY:");
        System.out.printf(format, "Source:", query.getFlight().getSource());
        printFlightDetails(response);
    }

    public static void printErrorMessage(ServerResponse response) {
        System.out.println("\n=================================================");
        System.out.println("============== ERROR CODE "+ response.getStatus()+ " ==============" );
        System.out.println("\n=================================================");
        switch (response.getStatus()){
            case 500:
                System.out.println("Internal Server Error");
                System.out.println("\n=================================================");
                break;
            case 404:
                System.out.println("Page Not Found");
                System.out.println("\n=================================================");
                break;
            default:
                System.out.println("Unknown Error");
                System.out.println("\n=================================================");
                break;
        }

    }

    public static void printServerResponse() {
        System.out.println("\n=================================================");
        System.out.println("================ Server Response ================" );
        System.out.println("=================================================");
        System.out.println("================= Status 200 OK =================" );
        System.out.println("=================================================");
    }

}
