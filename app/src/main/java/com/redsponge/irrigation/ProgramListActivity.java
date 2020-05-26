package com.redsponge.irrigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ProgramListActivity extends Activity implements IApplicationRetriever {

    private ListView programList;
    private String arduinoIp;
    private int arduinoPort;
    private TextView tvDateDisplay;
    private Date usedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arduinoIp = ((InetAddress)getIntent().getSerializableExtra("address")).getHostAddress();
        System.out.println(arduinoIp);
        arduinoPort = 32806;

        setContentView(R.layout.activity_program_list);
        programList = findViewById(R.id.programList);
        programList.setAdapter(new ProgramAdapter(this));
        tvDateDisplay = findViewById(R.id.tvDateDisplay);
        syncTime();
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hi");
                if(usedDate != null) {
                    usedDate.setTime(usedDate.getTime() + 1000);
                    displayDate(usedDate);
                }
                handler.postDelayed(this, 1000);
            }
        });

        getDataFromServer(null);
        showList();
    }

    private void syncTime() {
        Utils.sendToServer(arduinoIp, arduinoPort, (data) -> {
            System.out.println(data[0]);
            try {
                Date serverTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK).parse(data[0]);
                Date selfTime = Calendar.getInstance().getTime();
                long secDiff = Math.abs(selfTime.getTime() - Objects.requireNonNull(serverTime).getTime()) / 1000;
//                displayDate(serverTime);
                usedDate = serverTime;
                if(secDiff > Constants.MAX_SEC_DIFF) {
                    new AlertDialog.Builder(this)
                            .setTitle("Time Mismatch")
                            .setMessage(String.format(Locale.UK, "It appears the clock's time is incorrect by %d seconds! Fix it?", secDiff))
                            .setPositiveButton("Yes", (d, w) -> {
                                // settimeYEAR,MONTH,DAY,HOUR,MIN,SEC
                                Calendar cal = Calendar.getInstance();
                                String command = String.format(Locale.UK, "settime%d,%d,%d,%d,%d,%d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
                                Utils.sendToServer(arduinoIp, arduinoPort, (dt) -> {
                                    System.out.println(Arrays.toString(dt));
                                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                                    d.dismiss();
                                    usedDate = selfTime;
//                                    displayDate(selfTime);
                                }, command);
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                System.out.println(secDiff);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }, "gettime");
    }

    private void displayDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
        tvDateDisplay.setText(sdf.format(date));
    }

    private void showList() {
        ((ArrayAdapter<Program>)programList.getAdapter()).clear();
        ((ArrayAdapter<Program>)programList.getAdapter()).addAll(getApp().getPrograms());
    }

    public void addProgram(View view) {
        Toast.makeText(this, "Adding a program :D", Toast.LENGTH_SHORT).show();
        Program newProg = new Program();
        ((IrrigationApplication)getApplication()).getPrograms().add(newProg);
        Intent editScreen = new Intent(this, ProgramEditActivity.class);
        editScreen.putExtra("program", ((IrrigationApplication)getApplication()).getPrograms().size() - 1);

        startActivity(editScreen);
    }

    public void sendUpdatesToServer(View view) {
        Toast.makeText(this, "Sending data so cool", Toast.LENGTH_SHORT).show();
        String[] commands = new String[2 + ((IrrigationApplication)getApplication()).getPrograms().size()];
        commands[0] = "clrallp";
        for (int i = 0; i < ((IrrigationApplication) getApplication()).getPrograms().size(); i++) {
            commands[i + 1] = "addprog" + ((IrrigationApplication) getApplication()).getPrograms().get(i).encode();
        }
        commands[((IrrigationApplication) getApplication()).getPrograms().size() + 1]="savprog";

        Thread t = new Thread(() -> {
            Connection conn = new Connection(arduinoIp, arduinoPort);
            for (String command : commands) {
                System.out.println("Sent " + command);
                conn.send(command);
                System.out.println("Received " + conn.recv());
            }
        });
        t.start();
    }

    public void getDataFromServer(View view) {
        Toast.makeText(this, "Getting data so cool", Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            Connection conn = new Connection(arduinoIp, arduinoPort);
            conn.send("getprogall");
            String data = conn.recv().trim();
            System.out.println(data);
            if(!data.equals("NO REPLY!")) {
                getApp().getPrograms().clear();
                for (String entry : data.split(";")) {
                    Program decoded = Program.decode(entry);
                    getApp().getPrograms().add(decoded);
                }
            }
            Utils.runOnMainThread(this::showList);
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showList();
    }

    @Override
    public IrrigationApplication getApp() {
        return (IrrigationApplication) getApplication();
    }

    public void openManualValveControl(View view) {
        Intent i = new Intent(this, ValveControlActivity.class);
        i.putExtra("connIp", arduinoIp);
        i.putExtra("connPort", arduinoPort);
        startActivity(i);
    }
}
