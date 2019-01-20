package com.vimax.weatheronmap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Map extends View {
    public static final String TAG = "mytag";

    public SeekBar mSeekBar;
    public TextView tvTisDay;

    Bitmap cityMap;
    Bitmap cityPoint;

    public ArrayList<City> mCities;
    public ArrayList<Weather> mWeathers;
    public ArrayMap<String, ArrayList> citiesData;

    int resBeginMapX = 0;
    int resBeginMapY = 0;

    int x = 0;
    int y = 0;
    int startTouchX = 0;
    int startTouchY = 0;
    int touchEndX = 0;
    int touchEndY = 0;
    int touchResoultX = 0;
    int touchResoultY = 0;

    int bmpWidth = 0;
    int bmpHeight = 0;
    int bmpEndX = 0;
    int bmpEndY = 0;


    Paint fontPaint;
    String text2 = "Test width text";
    int fontSize = 20;
    float[] widths;
    float width2;

    String apiKey = "";
    String webServiceUrl = "";

    boolean setFirstTempInfo = false;

    public Map(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.citiesData = new ArrayMap<>();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        cityMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.map_ua, options);
        cityPoint = BitmapFactory.decodeResource(context.getResources(), R.drawable.city_point, options);

        apiKey = context.getResources().getString(R.string.api_key);
        webServiceUrl = context.getResources().getString(R.string.web_service_url);

        mCities = new ArrayList<>();
        mCities.add(new City("Vinnytsia", 425, 345, 0, 0));
        mCities.add(new City("Lutsk", 217, 122,  0,  0));
        mCities.add(new City("Dnipro", 800, 370,  0,  0));
        mCities.add(new City("Donetsk", 980, 400,  0,  0));
        mCities.add(new City("Zhytomyr", 410, 170,  0,  0));
        mCities.add(new City("Ivano-Frankivsk", 170, 350, 0, 0));
        mCities.add(new City("Kiev", 520, 210, 0, 0));
        mCities.add(new City("Kropyvnytskyi", 656, 390, 0, 0));
        mCities.add(new City("Lviv", 140, 240, 0, 0));
        mCities.add(new City("Bilhorod-Dnistrovskyi", 500, 580, 0, 0)); // не работает Одесса
        mCities.add(new City("Poltava", 730, 260, 0,  0));
        mCities.add(new City("Rivne", 310, 120, 0, 0));
        mCities.add(new City("Sumy", 745, 150, 0, 0));
        mCities.add(new City("Ternopil", 226, 270, 0, 0));
        mCities.add(new City("Kharkiv", 900, 260, 0, 0));
        mCities.add(new City("Kherson", 735, 545, 0, 0));
        mCities.add(new City("Khmelnytskyi", 320, 250, 0, 0));
        mCities.add(new City("Cherkasy", 567, 322, 0, 0));
        mCities.add(new City("Chernihiv", 630, 105, 0, 0));
        mCities.add(new City("Chernivtsi", 226, 382, 0, 0));
        mCities.add(new City("Uzhhorod", 95, 356, 0, 0));
        mCities.add(new City("Zaporizhzhia", 860, 500, 0, 0));
        mCities.add(new City("Luhansk", 1050, 305, 0, 0));
        mCities.add(new City("Mykolaiv", 635, 485,  0, 0));
        mCities.add(new City("Simferopol", 750, 670,  0, 0));


        fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setTextSize(fontSize);
        fontPaint.setStyle(Paint.Style.STROKE);

        // ширина текста
        width2 = fontPaint.measureText(text2);

        // посимвольная ширина
        widths = new float[text2.length()];
        fontPaint.getTextWidths(text2, widths);

        /* ************** запрос к погоде *************** */
        for (City city : mCities) {
            URL url = createURL(city.getName());
            GetWeatherTask getWeatherTask = new GetWeatherTask();
            getWeatherTask.execute(url);
        }
    }

    private URL createURL(String city) {
        String apiKey = this.apiKey;
        String baseUrl = this.webServiceUrl;

        try {
            // create URL for specified city and imperial units (Fahrenheit)
            String urlString = baseUrl + URLEncoder.encode(city, "UTF-8") +
                    "&units=metric&lang=ua&APPID=" + apiKey;
            Log.d(TAG, urlString);
            return new URL(urlString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null; // URL was malformed
    }


    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                startTouchX = (int) event.getX();
                startTouchY = (int) event.getY();
                bmpWidth = cityMap.getWidth();
                bmpHeight = cityMap.getHeight();
                bmpEndX = x + bmpWidth;
                bmpEndY = y + bmpHeight;

                break;
            case MotionEvent.ACTION_MOVE:
                touchEndX =(int)event.getX();
                touchEndY =(int)event.getY();

                if (touchEndX != startTouchX || touchEndY != startTouchY) {
                    touchResoultX = touchEndX - startTouchX;
                    touchResoultY = touchEndY - startTouchY;

                    resBeginMapX = x + touchResoultX;
                    resBeginMapY = y + touchResoultY;

                    if (resBeginMapX > 0) {// если карта начинается вылазит слева направо
                        x = 0;
                    } else {
                        x += touchResoultX;
                    }

                    if (resBeginMapY > 0) { // если катра вылазит сверху в низ
                        y = 0;
                    } else {
                        y += touchResoultY;
                    }

                    startTouchX = touchEndX;
                    startTouchY = touchEndY;

                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.v("ssss", "x " + (startTouchX + Math.abs(resBeginMapX)) + " - y " + (startTouchY + Math.abs(resBeginMapY)));
                Log.v("ssss", "bmpWidth " + bmpWidth + " - bmpHeight " + bmpHeight);
                break;
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.scale(2, 2);

        canvas.drawBitmap(cityMap, x, y, paint);

        for (City city : this.mCities) {
            canvas.drawBitmap(cityPoint, x + city.getX(), y + city.getY(), paint);
            canvas.drawText(String.valueOf(
                    city.getName()),
                    x + city.getX() - 10,
                    y + city.getY() - 10,
                    fontPaint
            );
            canvas.drawText(
                    String.valueOf(city.getNightTemp()) + " / " + String.valueOf(city.getDayTemp()) + " C°",
                    x + city.getX() - 20,
                    y + city.getY() + 40,
                    fontPaint
            );
        }
    }

    private class GetWeatherTask
            extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))) {

                        String line;

                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    return new JSONObject(builder.toString());
                }
                else {
                    Log.v(TAG, "My error");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                connection.disconnect(); // close the HttpURLConnection
            }

            return null;
        }

        // process JSON response and update ListView
        @Override
        protected void onPostExecute(JSONObject weather) {
            convertJSONtoArrayList(weather); // repopulate weatherList
            setTextInfo();
        }
    }

    private void setTextInfo () {
        if (!setFirstTempInfo) {
            setFirstTempInfo = true;
            Log.v(TAG, "WOrking set info");
            for (Weather weather : mWeathers) {
                tvTisDay.setText(weather.getDtTxt().toString());
                break;
            }
            mSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        }
    }

    private void convertJSONtoArrayList(JSONObject forecast) {
        try {
            boolean isRenderFirst = false;
            this.mWeathers = new ArrayList<>();
            JSONObject JSONCityObj = forecast.getJSONObject("city");
            JSONArray list = forecast.getJSONArray("list");

            for (int i = 0; i < list.length(); i++) {
                JSONObject item = list.getJSONObject(i);
                JSONObject main = item.getJSONObject("main");
                JSONArray weatherMain = item.getJSONArray("weather");
                JSONObject clouds = item.getJSONObject("clouds");
                JSONObject wind = item.getJSONObject("wind");

                JSONObject weatherMain1 = weatherMain.getJSONObject(0);

                String dtTxt = item.getString("dt_txt");
                String weatherMainSt = weatherMain1.getString("main");
                Double tempMin = main.getDouble("temp_min");
                Double tempMax = main.getDouble("temp_max");
                Double cloudsAll = clouds.getDouble("all");
                Double windSpeed = wind.getDouble("speed");
                Double windDeg = wind.getDouble("deg");

                this.mWeathers.add(new Weather(dtTxt, tempMin, tempMax, weatherMainSt, cloudsAll, windSpeed, windDeg));

                if (!isRenderFirst) {
                    isRenderFirst = true;
                    for (City city : mCities) {
                        if (city.getName().equals(JSONCityObj.getString("name"))) {
                            city.setDayTemp((float) main.getDouble("temp_max"));
                            city.setNightTemp((float) main.getDouble("temp_min"));
                        }
                    }
                }
            }

            citiesData.put(JSONCityObj.getString("name"), this.mWeathers);
            invalidate();

            mSeekBar.setMax(list.length() - 1);
        }
        catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    private void setNewTempInCity(int numberTime) {
        Log.v(TAG, "*****************START**********");
        for (String cityN : citiesData.keySet()) {
            Log.v(TAG, "City name => " + cityN);
            ArrayList cities = citiesData.get(cityN);
            Weather w = (Weather) cities.get(numberTime);
            Log.v(TAG, "getDayTemp => " + w.getDtTxt());

            for (City city : mCities) {
                if (city.getName().equals(cityN)) {
                    float max = Float.valueOf(String.valueOf(w.getTempMax()));
                    float min = Float.valueOf(String.valueOf(w.getTempMin()));
                    city.setDayTemp(max);
                    city.setNightTemp(min);
                }
                tvTisDay.setText(w.getDtTxt());
            }
        }
        Log.v(TAG, "*****************END**********");
        invalidate();
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.v(TAG, String.valueOf(seekBar.getProgress()));
            setNewTempInCity(seekBar.getProgress());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.v(TAG, "333");
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.v(TAG, "444");
        }
    };
}
