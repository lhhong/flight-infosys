package com.czce4013.entity;

import com.czce4013.marshaller.Marshallable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ServerResponse extends Marshallable {
    int status;
    List<FlightInfo> infos;

    public ServerResponse(){
        status = -1;
        infos = new ArrayList<>();
    }

}
