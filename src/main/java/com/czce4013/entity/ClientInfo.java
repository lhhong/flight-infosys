package com.czce4013.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.SocketAddress;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ClientInfo {
    int queryId;
    SocketAddress socket;
    long expire;

    public ClientInfo(int queryId, SocketAddress socket, int timeout) {
        this.queryId = queryId;
        this.socket = socket;
        this.expire = System.currentTimeMillis() + timeout * 1000;
    }
}
