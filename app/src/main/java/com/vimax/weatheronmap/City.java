package com.vimax.weatheronmap;

public class City {
    public static final String WEATHER_CLEAR = "Clear";
    public static final String WEATHER_SNOW = "Snow";
    public static final String WEATHER_CLOUDS = "Clouds";
    public static final String WEATHER_RAIN = "Rain";

    public String name;
    private int x;
    private int y;
    private int tempMax;
    private int tempMin;
    private int thisTemp;
    private String region;

    private String mainWeather;
    private int windSpeed;

    public City(
            String name,
            int x,
            int y,
            int tempMax,
            int tempMin,
            int thisTemp,
            String region
    ) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.thisTemp = thisTemp;
        this.region = region;
    }

    public void setMainWeather(String name) {
        mainWeather = name;
    }

    public void setWindSpeed(int windCount) {
        windSpeed = windCount;
    }

    public String getMainWeather() {
        return mainWeather;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public String getRegion() {
        return region;
    }

    public int getThisTemp() {
        return thisTemp;
    }

    public void setThisTemp(float temp) {
        this.thisTemp = Math.round(temp);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTempMax() {
        return tempMax;
    }

    public int getTempMin() {
        return tempMin;
    }

    public String getName() { return name; }

    public void setTempMax(float tempMax) {
        this.tempMax = Math.round(tempMax);
    }

    public void setTempMin(float tempMin) {
        this.tempMin = Math.round(tempMin);
    }
}
