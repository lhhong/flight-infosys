package com.czce4013.network;

public class IdContainer {
    private int id = 0;
    synchronized int get() {
        id++;
        return id;
    }
}
