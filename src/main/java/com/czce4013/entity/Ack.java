package com.czce4013.entity;

import com.czce4013.marshaller.Marshallable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class Ack extends Marshallable {
    int ackId;
}
