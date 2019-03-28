package com.czce4013.entity;

import com.czce4013.marshaller.Marshallable;

import java.util.HashMap;

public class ClientQuery extends Marshallable {
    int type;
    String source;
    String dest;
    DateTime dateTime;
    double airfare;
    int reserveSeats;

    public ClientQuery(){
        type = -1;
        source = "";
        dest = "";
        dateTime= new DateTime();
        airfare = -1.0;
        reserveSeats = -1;
    }

    public ClientQuery(int queryType, String source, String dest,DateTime dateTime, double airfare, int reserveSeats){
        type = queryType;
        this.source = source;
        this.dest = dest;
        this.dateTime = dateTime;
        this.airfare = airfare;
        this.reserveSeats = reserveSeats;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setAirfare(double airfare) {
        this.airfare = airfare;
    }

    public void setReserveSeats(int reserveSeats) {
        this.reserveSeats = reserveSeats;
    }

    public int getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getDest() {
        return dest;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public double getAirfare() {
        return airfare;
    }

    public int getReserveSeats() {
        return reserveSeats;
    }

    public String toString(){
        return "ClientQuery{" +
                "type=" + type +
                ", source='" + source +"\'"+
                ", dest='" + dest +"\'"+
                ", dateTime=" + dateTime +
                ", airfare=" + airfare +
                ", reserveSeats=" + reserveSeats +
                '}';
    }

}
