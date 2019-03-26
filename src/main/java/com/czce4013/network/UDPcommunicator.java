package com.czce4013.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UDPcommunicator {
    private DatagramSocket dSocket;
    private InetAddress destAddress;
    private int destPortNumber;

    public UDPcommunicator(String destIPAddress, int destPortNumber){
        try {
            dSocket = new DatagramSocket();
            destAddress = InetAddress.getByName(destIPAddress);
            this.destPortNumber = destPortNumber;
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

    public void send (String text){
        //TODO: add in marshalling before sending
        byte[] byteArray = text.getBytes();
        DatagramPacket packet = new DatagramPacket(byteArray,byteArray.length,this.destAddress,this.destPortNumber);

        try {
            this.dSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receive (){
        byte[] inputBuffer = new byte[1024];

        DatagramPacket p = new DatagramPacket(inputBuffer,inputBuffer.length);

        try {
            dSocket.receive(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

        destAddress = p.getAddress();
        destPortNumber = p.getPort();

        //TODO: add in marshalling after receiving
        String receivedStr = new String(p.getData(),0,p.getLength());
        return receivedStr;
    }


    public void setDestAddress(InetAddress destAddress) {
        this.destAddress = destAddress;
    }

    public void setDestPortNumber(int destPortNumber) {
        this.destPortNumber = destPortNumber;
    }
}
