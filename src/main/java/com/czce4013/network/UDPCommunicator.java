package com.czce4013.network;

import com.czce4013.marshaller.Marshallable;
import com.google.common.util.concurrent.FutureCallback;

import java.io.IOException;
import java.net.*;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.function.Consumer;


public class UDPCommunicator {
    private DatagramSocket dSocket;
    private InetSocketAddress socketAddress;

    public UDPCommunicator(InetSocketAddress socketAddress){
        try {
            dSocket = new DatagramSocket();
            this.socketAddress = socketAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UDPCommunicator(int selfPortNumber){
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

    public Marshallable receive() {
        final CompletableFuture<Marshallable> future = new CompletableFuture<>();
        final CompletableFuture<Boolean> timeoutFuture = new CompletableFuture<>();
        this.receiveAsync(future::complete, timeoutFuture::complete,5, false);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void receive(Consumer<Marshallable> callback, int timeout) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.receiveAsync(callback, future::complete ,timeout, true);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void receiveAsync (Consumer<Marshallable> callback, Consumer<Boolean> timeoutCall, int timeout, boolean listen){


        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        final Future handler = executor.submit(() -> {
            try {
                do {
                    byte[] inputBuffer = new byte[1024];
                    DatagramPacket p = new DatagramPacket(inputBuffer, inputBuffer.length);
                    dSocket.receive(p);
                    socketAddress = (InetSocketAddress) p.getSocketAddress();
                    callback.accept(Marshallable.unmarshall(p.getData()));
                } while (listen);
            } catch (IOException e) {
                callback.accept(null);
            }
            return false;
        });
        executor.schedule(() -> {
            handler.cancel(true);
            timeoutCall.accept(true);
        }, timeout, TimeUnit.SECONDS);

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
