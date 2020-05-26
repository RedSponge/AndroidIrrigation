package com.redsponge.irrigation;

import android.net.DhcpInfo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class ConnectionEstablisher {

    private DatagramSocket socket;

    public ConnectionEstablisher() {
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public InetAddress tryEstablish(int gateway, int mask) {
        try {
            byte[] buff = Constants.ESTABLISH_REQUEST.getBytes();
            InetAddress addr = InetAddress.getByName("255.255.255.255");
            System.out.println(addr);
            DatagramChannel channel = DatagramChannel.open();
            DatagramSocket sock = channel.socket();
            sock.setBroadcast(true);
            sock.send(new DatagramPacket(buff, 0, buff.length, addr, 31311));
            byte[] buf = new byte[20];
            DatagramPacket in = new DatagramPacket(buf, buf.length);
            in.setPort(Constants.ESTABLISH_PORT);
            sock.receive(in);
            System.out.println("Sent!");
            return in.getAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
