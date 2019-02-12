package com.czce4013.marshaller;

public class Marshallable {

    public byte[] marshall() {
        return Marshaller.marshall(this);
    }

    public void unmarshall(byte[] raw) {
        Marshaller.unmarshall(this, raw);
    }
}
