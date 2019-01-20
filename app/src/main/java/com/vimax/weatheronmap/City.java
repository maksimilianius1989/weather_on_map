package com.vimax.weatheronmap;

public class City {
    public String name;
    private int x;
    private int y;
    private int dayTemp;
    private int nightTemp;

    public City(String name, int x, int y, int dayTemp, int nightTemp) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.dayTemp = dayTemp;
        this.nightTemp = nightTemp;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDayTemp() {
        return dayTemp;
    }

    public int getNightTemp() {
        return nightTemp;
    }

    public String getName() { return name; }

    public void setDayTemp (float dayTemp) {
        this.dayTemp = Math.round(dayTemp);
    }

    public void setNightTemp (float nightTemp) {
        this.nightTemp = Math.round(nightTemp);
    }
}
