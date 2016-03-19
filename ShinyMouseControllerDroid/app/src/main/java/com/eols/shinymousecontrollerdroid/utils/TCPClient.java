package com.eols.shinymousecontrollerdroid.utils;

import android.util.Log;

import com.eols.shinymousecontrollerdroid.interfaces.TCPListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by emiols on 2016-03-19.
 */
public class TCPClient {

    private String serverAddress;
    private int serverPort;

    private TCPListener listener;
    private boolean runClient = false;

    private PrintWriter out;
    private BufferedReader in;
    private String serverMessage;

    public TCPClient(TCPListener listener, String serverAddress, int serverPort){
        this.listener = listener;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void run(){
        this.runClient = true;

        try{
            InetAddress serverAddr = InetAddress.getByName(serverAddress);

            Log.e("TCP Client", "C: Connecting...");

            Socket socket = new Socket(serverAddr, serverPort);

            try{

                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Log.e("TCP Client", "C: Sent.");
                Log.e("TCP Client", "C: Done.");

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (runClient){

                    this.serverMessage = in.readLine();

                    if(this.serverMessage != null && this.listener != null){
                        this.listener.onMessageReceived(this.serverMessage);
                    }

                    this.serverMessage = null;
                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");

            } catch (Exception e){

                Log.e("TCP", "S: Error", e);

            } finally {
                socket.close();
            }

        } catch (Exception e){
            Log.e("TCP", "C: Error", e);
        }
    }

    public void sendMessage(String message){
        if(out != null && !out.checkError()){
            out.println(message);
            out.flush();
        }
    }

    public void stopClient(){
        this.runClient = false;
    }
}
