package com.redsponge.irrigation;

import android.content.Intent;

import java.util.Locale;

public class Program {

    private int dayOfWeek, startMin, startHour, duration, valve;
    private boolean isActive;


    public Program() {
        this(1, 0, 10, 60, 1, true);
    }

    public Program(int dayOfWeek, int startMin, int startHour, int duration, int valve, boolean isActive) {
        this.dayOfWeek = dayOfWeek;
        this.startMin = startMin;
        this.startHour = startHour;
        this.duration = duration;
        this.valve = valve;
        this.isActive = isActive;
    }


    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getStartMin() {
        return startMin;
    }

    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getValve() {
        return valve;
    }

    public void setValve(int valve) {
        this.valve = valve;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String encode() {
        return String.format(Locale.UK, "%d,%d,%d,%d,%d,%d", dayOfWeek, startHour, startMin, duration, valve, isActive ? 1 : 0);
    }

    @Override
    public String toString() {
        return "Program{" +
                "dayOfWeek=" + dayOfWeek +
                ", startMin=" + startMin +
                ", startHour=" + startHour +
                ", duration=" + duration +
                ", valve=" + valve +
                ", isActive=" + isActive +
                '}';
    }

    public static Program decode(String data) {
        String[] split = data.split(",");
        return new Program(
                Integer.parseInt(split[0+1]),
                Integer.parseInt(split[2+1]),
                Integer.parseInt(split[1+1]),
                Integer.parseInt(split[3+1]),
                Integer.parseInt(split[4+1]),
                Integer.parseInt(split[5+1]) == 1
        );
    }
}
