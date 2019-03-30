package com.czce4013.marshaller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Marshallable {

    int id;
    public byte[] marshall() {
        return Marshaller.marshall(this);
    }

    public static Marshallable unmarshall(byte[] raw) {
        try {
            return (Marshallable) Marshaller.unmarshall(raw);
        } catch (ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends Marshallable> T unmarshall(byte[] raw, Class<T> clazz) {
        return clazz.cast(unmarshall(raw));
    }
}
