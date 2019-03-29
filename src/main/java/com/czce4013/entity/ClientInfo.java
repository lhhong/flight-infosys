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
    SocketAddress socket;
    long expire;

    public ClientInfo(SocketAddress socket, int timeout) {
        this.socket = socket;
        this.expire = System.currentTimeMillis() + timeout * 1000;
    }
}
