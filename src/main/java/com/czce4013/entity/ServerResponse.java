package com.czce4013.entity;

import com.czce4013.marshaller.Marshallable;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class ServerResponse extends Marshallable implements Cloneable {
    int queryId;
    int status;
    List<FlightInfo> infos;

    public ServerResponse clone() {
        try {
            return (ServerResponse) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
