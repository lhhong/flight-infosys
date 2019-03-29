package com.czce4013.entity;

import com.czce4013.marshaller.Marshallable;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ClientQuery extends Marshallable {
    int type;
    int timeout;
    FlightInfo flight;

    public ClientQuery(){
        type = -1;
        flight = new FlightInfo();
    }

    public ClientQuery(int type, FlightInfo flight) {
        this.type = type;
        this.flight = flight;
    }

}
