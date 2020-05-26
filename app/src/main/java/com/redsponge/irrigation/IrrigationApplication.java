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
    }

    public ArrayList<Program> getPrograms() {
        return programs;
    }
}
