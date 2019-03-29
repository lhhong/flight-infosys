package com.czce4013.entity;

import com.czce4013.marshaller.Marshallable;

import java.util.HashMap;

public class ClientQuery extends Marshallable {
    int type;
    FlightInfo flight;

    public ClientQuery(){
        type = -1;
        flight = new FlightInfo();
    }

    public ClientQuery(int queryType, FlightInfo flight){
        type = queryType;
        this.flight = flight;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setFlight(FlightInfo flight) {
        this.flight = flight;
    }

    public FlightInfo getFlight() {
        return flight;
    }

    public int getType() {
        return type;
    }

    public String toString(){
        return "ClientQuery{" +
                "type=" + type +
                ", flight=" + flight+
                '}';
    }

}
