package com.vimax.weatheronmap;

public class Weather {
    private String dtTxt;
    private Double tempMin;
    private Double tempMax;
    private Double thisTemp;
    private String mainWeather;
    private Double clouds;
    private Double windSpeed;
    private Double windDeg;

    public Weather (
            String dtTxt,
            Double tempMin,
            Double tempMax,
            String mainWeather,
            Double clouds,
            Double windSpeed,
            Double windDeg,
            Double thisTemp
    ){
        this.dtTxt = dtTxt;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.mainWeather = mainWeather;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.windDeg = windDeg;
        this.thisTemp = thisTemp;
    }


    public Double getThisTemp() {
        return thisTemp;
    }

    public void setThisTemp(Double thisTemp) {
        this.thisTemp = thisTemp;
    }

    public String getDtTxt() {
        return dtTxt;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public String getMainWeather() {
        return mainWeather;
    }

    public Double getClouds() {
        return clouds;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public Double getWindDeg() {
        return windDeg;
    }
}
