package com.czce4013.server;

public class Server {
    public Server(){

    }

    public void run() {
        System.out.println("running server");
    }

    public static void main(String[] args){
        Server s = new Server();
        s.run();
    }
}

