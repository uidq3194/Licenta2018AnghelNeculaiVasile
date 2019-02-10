package com.sda.ambulanta.apk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BroadcastReceiver implements Runnable
{
    private Socket client;

    public BroadcastReceiver(Socket m)
    {
        this.client = m;
    }

    @Override
    public void run()
    {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch(IOException ignored) {
        }

        while (true) {
            String line;
            try {
                while ((line = in.readLine()) != null)
                    out.println(line);
            } catch (IOException e) {
                System.out.println("Read Failed");
                System.exit(-1);
            }
        }
    }
}
