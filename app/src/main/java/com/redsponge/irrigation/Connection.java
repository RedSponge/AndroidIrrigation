package com.redsponge.irrigation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Connection {

    private Socket conn;
    private BufferedReader in;
    private BufferedWriter out;

    private String connIp;
    private int connPort;

    public Connection(String connIp, int connPort) {
        this.connIp = connIp;
        this.connPort = connPort;

        try {
            conn = new Socket();

            conn.connect(new InetSocketAddress(connIp, connPort));

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String recv() {
        try {
            String received = in.readLine();
            return received;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String data) {
        try {
            out.write(data);
            out.write('\n');
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            out.close();
            in.close();
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
