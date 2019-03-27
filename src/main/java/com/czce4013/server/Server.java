package com.czce4013.server;

import com.czce4013.network.UDPcommunicator;

public class Server {
    private UDPcommunicator communicator;

    public Server(){
        communicator = new UDPcommunicator(2222);
    }

    public void run() {
        System.out.println("Running Server...");

        while (true){

            //String st = communicator.receive();

            //System.out.println("received String: " + st);

            //communicator.send("World!");
        }
    }

    public static void main(String[] args){
        Server s = new Server();
        s.run();
    }
}

