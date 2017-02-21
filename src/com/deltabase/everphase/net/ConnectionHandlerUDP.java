package com.deltabase.everphase.net;

import com.deltabase.everphase.api.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectionHandlerUDP {

    private static DatagramSocket socket;
    private static Thread receiveThread;

    private static volatile boolean connected = false;
    private static InetAddress ipAddress;
    private static int port;

    public static void connectToServer() {
        connected = true;

        try {
            socket = new DatagramSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }

        receiveThread = new Thread(ConnectionHandlerUDP::receive);
        receiveThread.start();

        try {
            ipAddress = InetAddress.getByName(NetworkUtils.getServerIP());
            port = NetworkUtils.getServerPort();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    private static void receive() {
        while (connected) {
            byte[] data = new byte[2048];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i("UDP", new String(packet.getData()));
        }
    }

    public static void sendMessageToServer(String msg) {
        byte[] bytes = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, ipAddress, port);

        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static synchronized void quitConnection() {
        try {
            connected = false;
            receiveThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!socket.isClosed())
            socket.close();
    }

}
