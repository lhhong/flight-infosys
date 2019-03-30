package com.czce4013.entity;

import com.czce4013.marshaller.Marshallable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class FlightInfo {
    short flightId;
    String source;
    String dest;
    DateTime dateTime;
    float fare;
    short seatsAvailable;

    public FlightInfo(){
        this.flightId = -1;
        this.source = "";
        this.dest = "";
        this.dateTime = new DateTime();
        this.fare = -1.0F;
        this.seatsAvailable = -1;
    }
}
