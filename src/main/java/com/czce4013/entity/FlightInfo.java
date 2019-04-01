package com.czce4013.entity;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FlightInfo implements Cloneable{
    short flightId;
    String source;
    String dest;
    DateTime dateTime;
    float fare;
    short seatsAvailable;

    public FlightInfo clone(){
        try {
            return (FlightInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
