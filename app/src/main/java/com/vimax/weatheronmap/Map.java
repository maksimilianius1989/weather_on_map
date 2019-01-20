package com.vimax.weatheronmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Map extends View {
    Paint mPaint;
    Bitmap map;

    public Map(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Map(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        map = BitmapFactory.decodeResource(context.getResources(), R.drawable.sun);

        Log.v("RES", "TESTT");
    }

    public Map(Context context) {
        super(context);
    }

    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.scale(2, 2);

        mPaint.setColor(Color.RED);
        canvas.drawCircle(100, 100, 20, mPaint);
        canvas.drawLine(2000, 2000, 5000, 5000, mPaint);

        canvas.drawBitmap(map, 0, 0, paint);
    }
}
