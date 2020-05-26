package com.redsponge.irrigation;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Arrays;

public class ValveControlActivity extends Activity {

    private TableLayout tlValveLayout;
    private String ip;
    private int port;
    private int valveCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ip = getIntent().getStringExtra("connIp");
        port = getIntent().getIntExtra("connPort", -1);
        valveCount = getIntent().getIntExtra("valveCount", 2);

        if(port == -1) throw new RuntimeException("Port wasn't passed");
        overrideAndGetValveStatus();
    }

    private void overrideAndGetValveStatus() {
        String[] commands = new String[valveCount+1];
        commands[0] = "setover1";
        for (int i = 1; i <= valveCount; i++) {
            commands[i] = "getvalv" + i;
        }
        Utils.sendToServer(ip, port, (data) -> {
            System.out.println(Arrays.toString(data));
            setContentView(R.layout.activity_valve_control);
            tlValveLayout = findViewById(R.id.tlValveTable);
            for (int i = 1; i <= valveCount; i++) {
                addValve(i, !data[i].equals("0"));
            }
        }, commands);

    }

    public void addValve(int id, boolean status) {
        TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.item_valve_edit_row, null);
        TextView tv = row.findViewById(R.id.tvValveName);
        tv.setText("Valve " + id);
        CheckBox cb = row.findViewById(R.id.cbValveControl);
        cb.setChecked(status);
        cb.setOnClickListener((v) -> {
            sendCheckboxData(id, cb.isChecked());
        });
        tlValveLayout.addView(row);
    }

    private void sendCheckboxData(int id, boolean checked) {
        Utils.sendToServer(ip, port, (data) -> {
            System.out.println("GOT FROM sendCheckboxData: " + Arrays.toString(data));
        }, "setover1", "setvalv" + id + "," + (checked ? 1 : 0));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.sendToServer(ip, port, null, "setover0");
    }

    @Override
    protected void onResume() {
        super.onResume();
        overrideAndGetValveStatus();
    }
}
