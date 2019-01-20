package com.vimax.weatheronmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

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

public class Map extends View {
    public static final String TAG = "mytag";
    public ImageView imageView;

    Bitmap cityMap;
    Bitmap cityPoint;

    public ArrayList<City> mCities;

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

    public Map(Context context, AttributeSet attrs) {
        super(context, attrs);

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

                /* ************** запрос к погоде *************** */
                URL url = createURL("Kiev");
                GetWeatherTask getWeatherTask = new GetWeatherTask();
                getWeatherTask.execute(url);

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
                    String.valueOf(city.getDayTemp()) + " / " + String.valueOf(city.getNightTemp()) + " C°",
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
        }
    }

    private void convertJSONtoArrayList(JSONObject forecast) {
        try {
            JSONObject JSONCityObj = forecast.getJSONObject("city");

            JSONArray list = forecast.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject day = list.getJSONObject(i);
                JSONObject main = day.getJSONObject("main");
                for (City city : mCities) {
                    if (city.getName().equals(JSONCityObj.getString("name"))) {
                        city.setDayTemp((float) main.getDouble("temp_max"));
                        city.setNightTemp((float) main.getDouble("temp_min"));
                        break;
                    }
                }
                break;
            }
            invalidate();
        }
        catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}
