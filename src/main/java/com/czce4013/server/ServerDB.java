package com.czce4013.server;

import com.czce4013.entity.ClientQuery;
import com.czce4013.entity.DateTime;
import com.czce4013.entity.FlightInfo;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerDB {
    private static ArrayList<FlightInfo> flightData= new ArrayList<FlightInfo>();;
    private static HashMap<Integer, ArrayList<SocketAddress>> callbackList = new HashMap<Integer, ArrayList<SocketAddress>>();

    public ServerDB(){
        seed();
    }

    public static void seed(){
        DateTime dateTime = new DateTime(2019,3,28,23,5);
        flightData.add(new FlightInfo(1001,"SINGAPORE","BANGKOK",dateTime,1.23,5));
        dateTime = new DateTime(2019,3,28,23,10);
        flightData.add(new FlightInfo(2012,"KL","BANGKOK",dateTime,2.23,15));
        dateTime = new DateTime(2019,3,28,23,15);
        flightData.add(new FlightInfo(3197,"SINGAPORE","KL",dateTime,3.23,2));
        dateTime = new DateTime(2019,3,28,23,20);
        flightData.add(new FlightInfo(4456,"BANGKOK","SINGAPORE",dateTime,4.23,9));
        dateTime = new DateTime(2019,3,28,23,25);
        flightData.add(new FlightInfo(5232,"SINGAPORE","BANGKOK",dateTime,5.23,7));
    }

    public List findFlightID(String source, String dest){
        ArrayList<FlightInfo> returnArray = new ArrayList<FlightInfo>();
        for (FlightInfo flight:flightData){
            if (flight.getSource().equals(source) && flight.getDest().equals(dest)){
                returnArray.add(flight);
            }
        }
        return returnArray;
    }

    public List getFlightDetails(int id){
        ArrayList<FlightInfo> returnArray = new ArrayList<FlightInfo>();
        for (FlightInfo flight:flightData){
            if (flight.getId() == id){
                returnArray.add(flight);
            }
        }
        return returnArray;
    }

    public List makeReservation(int id, int seatsReserve) {
        ArrayList<FlightInfo> returnArray = new ArrayList<FlightInfo>();
        for (FlightInfo flight:flightData){
            if (flight.getId() == id){
                flight.setSeatsAvailable(flight.getSeatsAvailable()-seatsReserve);
                returnArray.add(flight);
            }
        }
        return returnArray;
    }

    public void observeFlight(int flightId,SocketAddress socketAddress) {
        if (callbackList.get(flightId) == null){
            ArrayList<SocketAddress> addresses = new ArrayList<SocketAddress>();
            addresses.add(socketAddress);
            callbackList.put(flightId,addresses);
        }
        else{
            ArrayList<SocketAddress> addresses = callbackList.get(flightId);
            addresses.add(socketAddress);
            callbackList.put(flightId,addresses);
        }
    }

    public static ArrayList<SocketAddress> getCallBackAddresses(int flightID){
        return callbackList.get(flightID);
    }
}
