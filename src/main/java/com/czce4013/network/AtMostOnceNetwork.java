package com.czce4013.network;

import com.czce4013.entity.Response;

public class AtMostOnceNetwork extends Network {
    public AtMostOnceNetwork(UDPCommunicator communicator) {
        super(communicator);
    }

    @Override
    protected boolean continueResponse(Response data) {
        // TODO check duplicates
        return true;
    }

}
