package com.czce4013.entity;

import com.czce4013.marshaller.Marshallable;

import java.util.ArrayList;
import java.util.List;

public class ServerResponse extends Marshallable {
    int status;
    List<FlightInfo> infos;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setInfos(List<FlightInfo> infos) {
        this.infos = infos;
    }

    public int getStatus() {
        return status;
    }

    public List<FlightInfo> getInfos() {
        return infos;
    }

    public ServerResponse(){
        status = -1;
        infos = new ArrayList<>();
    }

    public ServerResponse(int status, List arr){
        this.status=status;
        infos = arr;
    }

    public String toString(){
        return "ServerResponse{" +
                "status=" + status +
                "infos=" + infos +
                '}';
    }
}
