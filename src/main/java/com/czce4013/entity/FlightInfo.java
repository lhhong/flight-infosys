package com.czce4013.entity;

import com.czce4013.marshaller.Marshallable;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FlightInfo {
    short flightId;
    String source;
    String dest;
    DateTime dateTime;
    float fare;
    short seatsAvailable;

}
