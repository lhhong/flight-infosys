package com.czce4013.entity;

import com.czce4013.marshaller.Marshallable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.InetSocketAddress;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Response {
    InetSocketAddress origin;
    Marshallable data;
}
