package com.czce4013.entity;

import com.czce4013.marshaller.Marshallable;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ClientQuery extends Marshallable {
    int type;
    int timeout;
    FlightInfo flight = new FlightInfo();

    public ClientQuery(int type, FlightInfo flight) {
        this.type = type;
        this.flight = flight;
    }

}
