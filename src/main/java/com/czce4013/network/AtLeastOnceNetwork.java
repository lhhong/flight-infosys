package com.czce4013.network;

import com.czce4013.entity.Ack;
import com.czce4013.entity.ClientQuery;
import com.czce4013.entity.Response;
import com.czce4013.entity.ServerResponse;

import java.util.function.Consumer;

public class AtLeastOnceNetwork extends Network {


    public AtLeastOnceNetwork(UDPCommunicator communicator) {
        super(communicator);
    }

    @Override
    protected boolean continueResponse(Response data) {
        //Do nothing for at least once
        return true;
    }

}
