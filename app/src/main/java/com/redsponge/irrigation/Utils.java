package com.redsponge.irrigation;

import android.os.Handler;
import android.os.Looper;

public class Utils {

    public static void sendToServer(String ip, int port, CommunicationCallback cb, String... data) {
        new Thread(() -> {
            Connection conn = new Connection(ip, port);
            String[] out = new String[data.length];
            for (int i = 0; i < data.length; i++) {
                conn.send(data[i]);
                out[i] = conn.recv();
            }
            conn.close();

            if(cb != null) {
                runOnMainThread(() -> {cb.handle(out);});
            }
        }).start();
    }

    public static byte[] toByteArr(int num) {
        byte[] out = new byte[4];
        for (int i = 0; i < 4; i++) {
            out[i] = (byte) (num & 0xFF);
            num >>= 8;
        }
        return out;
    }

    public static void runOnMainThread(Runnable toRun) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(toRun);
    }

}
