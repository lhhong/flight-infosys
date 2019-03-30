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
@ToString(callSuper = true)
public class ServerResponse extends Marshallable implements Cloneable {
    int queryId;
    int status;
    List<FlightInfo> infos;

    public ServerResponse(){
        status = -1;
        infos = new ArrayList<>();
    }

    public ServerResponse clone() {
        try {
            return (ServerResponse) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
