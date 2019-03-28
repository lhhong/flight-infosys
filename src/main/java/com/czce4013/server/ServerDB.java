package com.czce4013.server;

import com.czce4013.entity.DateTime;
import com.czce4013.entity.FlightInfo;

import java.util.ArrayList;
import java.util.List;

public class ServerDB {
    private static FlightInfo[] flightData;
    public ServerDB(){
        seed();
    }

    public static void seed(){
        flightData = new FlightInfo[5];
        DateTime dateTime = new DateTime(2019,3,28,23,5);
        flightData[0] = new FlightInfo(1,"Singapore","Bangkok",dateTime,1.23,5);
        dateTime = new DateTime(2019,3,28,23,10);
        flightData[1] = new FlightInfo(2,"KL","Bangkok",dateTime,2.23,15);
        dateTime = new DateTime(2019,3,28,23,15);
        flightData[2] = new FlightInfo(3,"Singapore","KL",dateTime,3.23,2);
        dateTime = new DateTime(2019,3,28,23,20);
        flightData[3] = new FlightInfo(4,"Bangkok","Singapore",dateTime,4.23,9);
        dateTime = new DateTime(2019,3,28,23,25);
        flightData[4] = new FlightInfo(5,"Singapore","Bangkok",dateTime,5.23,7);
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
}
