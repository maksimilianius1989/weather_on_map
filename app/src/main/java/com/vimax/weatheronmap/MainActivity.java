package com.vimax.weatheronmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "mytag";

    Map mMap;
    SeekBar mSeekBar;
    RelativeLayout insertMap;
    TextView tvThisDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSeekBar = (SeekBar) findViewById(R.id.sbTime);

        tvThisDay = (TextView) findViewById(R.id.tvThisDay);

        mMap = new Map(this, null);
        mMap.mSeekBar = mSeekBar;
        mMap.tvTisDay = tvThisDay;
        insertMap = findViewById(R.id.insertMap);
        insertMap.addView(mMap);
        Log.v(TAG, "111");
    }


}
