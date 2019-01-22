package com.vimax.weatheronmap;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "mytag";

    Map mMap;
    SeekBar mSeekBar;
    RelativeLayout insertMap;
    TextView tvThisDay;

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-9091567331130676~1007335856");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if(this.isInternetConnect()) {
            mSeekBar = (SeekBar) findViewById(R.id.sbTime);
            tvThisDay = (TextView) findViewById(R.id.tvThisDay);
            tvThisDay.setTypeface(null, Typeface.BOLD);

            mMap = new Map(this, null);
            mMap.mSeekBar = mSeekBar;
            mMap.tvTisDay = tvThisDay;
            insertMap = findViewById(R.id.insertMap);
            insertMap.addView(mMap);
            Log.v(TAG, "111");
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Потрібен доступ до Інтернету!",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private boolean isInternetConnect() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {
            return true; // есть соединение
        }
        else {
            return false; // нет соединения
        }
    }
}
