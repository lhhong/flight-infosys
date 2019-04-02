package com.czce4013;

import com.czce4013.network.AtLeastOnceNetwork;
import com.czce4013.network.AtMostOnceNetwork;
import com.czce4013.network.PoorUDPCommunicator;
import com.czce4013.network.UDPCommunicator;
import com.czce4013.server.*;
import com.czce4013.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
	// write your code here
        Client c;
        String LocalIPAddress = UDPCommunicator.getIPaddress();
        InetSocketAddress socketAddress = new InetSocketAddress(LocalIPAddress,2222);
        float failProb = 0.1F;
        if (args.length > 1) {
            failProb =  Float.parseFloat(args[1]);
        }
        UDPCommunicator communicator =  new PoorUDPCommunicator(socketAddress, failProb);
        //UDPCommunicator communicator =  new UDPCommunicator(socketAddress);
        if (args.length > 0 && args[0].equals("AtMostOnce")) {
            c = new Client(new AtMostOnceNetwork(communicator));
            logger.info("Initialized AtMostOnce network with fail probability of {}.", failProb);
        }
        else {
            c = new Client(new AtLeastOnceNetwork(communicator));
            logger.info("Initialized AtLeastOnce network with fail probability of {}.", failProb);
        }
        long time = System.nanoTime();
        c.testClient(1000);
        time = System.nanoTime()-time;

        System.out.println("time = " +time/1e9);
        return;
    }
}
