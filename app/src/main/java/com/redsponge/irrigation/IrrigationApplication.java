package com.redsponge.irrigation;

import android.app.Application;

import java.util.ArrayList;
import java.util.Random;

public class IrrigationApplication extends Application {

    private ArrayList<Program> programs;

    @Override
    public void onCreate() {
        super.onCreate();
        programs = new ArrayList<>(20);

        Random rnd = new Random();
        for (int i = 0; i < 20; i++) {
            programs.add(new Program(rnd.nextInt(7) + 1, rnd.nextInt(60), rnd.nextInt(24), rnd.nextInt(90 * 60), rnd.nextInt(4) + 1, rnd.nextBoolean()));
        }
    }

    public ArrayList<Program> getPrograms() {
        return programs;
    }
}
