package com.czce4013.network;

import com.czce4013.marshaller.Marshallable;

import java.io.IOException;
import java.net.*;


public class UDPcommunicator {
    private DatagramSocket dSocket;
    private InetSocketAddress socketAddress;

    public UDPcommunicator(InetSocketAddress socketAddress){
        try {
            dSocket = new DatagramSocket();
            this.socketAddress = socketAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UDPcommunicator(int selfPortNumber){
        try {
            dSocket = new DatagramSocket(selfPortNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send (Marshallable data){
        byte[] byteArray = data.marshall();
        DatagramPacket packet = new DatagramPacket(byteArray,byteArray.length,this.socketAddress);

        try {
            this.dSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Marshallable receive (){
        byte[] inputBuffer = new byte[1024];

        DatagramPacket p = new DatagramPacket(inputBuffer,inputBuffer.length);

        try {
            dSocket.receive(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

        socketAddress = (InetSocketAddress)p.getSocketAddress();

        return Marshallable.unmarshall(p.getData());
    }

    public static String getIPaddress(){
        String ret= null;
        try{
            ret = InetAddress.getLocalHost().getHostAddress();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ret;

    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }
}
