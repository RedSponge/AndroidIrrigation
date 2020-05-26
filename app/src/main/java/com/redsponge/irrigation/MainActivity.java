package com.redsponge.irrigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import java.net.InetAddress;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeToList(View view) {
        Intent i = new Intent(this, ProgramListActivity.class);
        startActivity(i);
    }

    public void moveToList(InetAddress addr) {
        Intent i = new Intent(this, ProgramListActivity.class);
        i.putExtra("address", addr);
        finish();
        startActivity(i);
    }

    public void tryConnect(View view) {
        new Thread(this::doEstablish).start();
    }

    private void doEstablish() {
        Looper.prepare();
        final WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectionEstablisher establisher = new ConnectionEstablisher();
        InetAddress addr = establisher.tryEstablish(wifi.getDhcpInfo().gateway, wifi.getDhcpInfo().netmask);
        if(addr == null) {
            Toast.makeText(this, "Couldn't Establish Connection!", Toast.LENGTH_SHORT).show();
        } else {
            moveToList(addr);
        }
    }
}
