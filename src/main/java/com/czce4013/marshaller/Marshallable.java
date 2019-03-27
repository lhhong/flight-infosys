package com.czce4013.marshaller;

public class Marshallable {

    public byte[] marshall() {
        return Marshaller.marshall(this);
    }

    public static <T extends Marshallable> Marshallable unmarshall(byte[] raw, Class<T> clazz) {
        return Marshaller.unmarshall(raw, clazz);
    }
}
