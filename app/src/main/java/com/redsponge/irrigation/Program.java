package com.redsponge.irrigation;

public class Program {

    private int dayOfWeek, startMin, startHour, duration, valve;
    private boolean isActive;

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
}
