package com.deltabase.everphase.net;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.api.Log;
import com.deltabase.everphase.api.event.mp.ServerMessageReceivedEvent;

import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class ConnectionHandlerTCP {

    private static Socket clientSocket;
    private static Thread sendThread, receiveThread;

    private static volatile String msgToSend = null;

    private static boolean connected = false;

    private static volatile Queue<String> messages = new LinkedBlockingQueue<>();

    public static void connectToServer() {
        try {
            clientSocket = new Socket(NetworkUtils.getServerIP(), 8192);

            connected = true;

            sendThread = new Thread(ConnectionHandlerTCP::send);
            receiveThread = new Thread(ConnectionHandlerTCP::receive);

            sendThread.start();
            receiveThread.start();
        } catch (IOException e) {
            Log.wtf("SERVER", "Connection Failed!!");
        }
    }

    private static void send() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            while (connected) {
                msgToSend = messages.poll();
                if (msgToSend == null) continue;
                writer.write(msgToSend + "\n\r");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                writer.close();
                clientSocket.close();
                connected = false;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static void receive() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;
            while (connected) {
                line = reader.readLine();
                if (line == null || line.trim().length() == 0) continue;
                EverPhaseApi.EVENT_BUS.postEvent(new ServerMessageReceivedEvent(line));
            }

        } catch (IOException e) {
            e.printStackTrace();
            try {
                reader.close();
                connected = false;
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static synchronized void sendMessageToServer(String message) {
        messages.add(message);
    }

    public static synchronized void quitConnection() {
        sendMessageToServer("QUIT");
        connected = false;
        try {
            Thread.sleep(50);
            sendThread.join();
            receiveThread.join();
            if (!clientSocket.isClosed())
                clientSocket.close();
        } catch (InterruptedException | IOException e) {
        }
    }

}
