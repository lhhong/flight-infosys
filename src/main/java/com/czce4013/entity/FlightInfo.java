package com.czce4013.entity;

import com.czce4013.marshaller.Marshallable;

public class FlightInfo extends Marshallable {
    int id;
    String source;
    String dest;
    DateTime dateTime;
    double fare;
    int seatsAvailable;

    public FlightInfo(){
        this.id = -1;
        this.source = "";
        this.dest = "";
        this.dateTime = new DateTime();
        this.fare = -1.0;
        this.seatsAvailable = -1;
    }

    public FlightInfo(int id, String source, String dest,DateTime dateTime, double airfare, int seats){
        this.id = id;
        this.source = source;
        this.dest = dest;
        this.dateTime = dateTime;
        this.fare = airfare;
        this.seatsAvailable = seats;
    }

    public String toString(){
        return "FlightInfo{" +
                "id=" + id +
                ", source='" + source +"\'"+
                ", dest='" + dest +"\'"+
                ", dateTime=" + dateTime +
                ", airfare=" + fare +
                ", reserveSeats=" + seatsAvailable +
                '}';
    }

    public void setId(int id) {
        this.id = id;
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

    public void setFare(double fare) {
        this.fare = fare;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public int getId() {
        return id;
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

    public double getFare() {
        return fare;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }
}
