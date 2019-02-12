package com.czce4013.entity;

import com.czce4013.marshaller.FieldId;
import com.czce4013.marshaller.Marshallable;

public class FlightInfo extends Marshallable {
    @FieldId(0)
    String id;
    int a;

}
