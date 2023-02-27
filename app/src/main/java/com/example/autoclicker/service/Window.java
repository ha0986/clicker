package com.example.autoclicker.service;

import static android.content.Context.WINDOW_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.autoclicker.MainActivity;
import com.example.autoclicker.R;

public class Window extends AppCompatActivity {
    private final String TAG = MyService.class.getSimpleName();
    private Context context;
    private WindowManager.LayoutParams mParams;
    private LayoutInflater layoutInflater;
    private View mView;
    private int[] coords = new int[2];
    WindowManager mWindowManager;
    private WindowManager.LayoutParams params;

    public Window(Context context) {
        this.context = context;

//        final DisplayMetrics metric = new DisplayMetrics();
//        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        }
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.pop_up, null);


        mParams.gravity = Gravity.CENTER;
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        Button enableClicking = mView.findViewById(R.id.enable_clicking);
        enableClicking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                long downTime = SystemClock.uptimeMillis();
//                long eventTime = SystemClock.uptimeMillis() + 100;
//                float x = 0.0f;
//                float y = 0.0f;
//// List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
//                int metaState = 0;
//                @SuppressLint("Recycle") MotionEvent motionEvent = MotionEvent.obtain(
//                        downTime,
//                        eventTime,
//                        MotionEvent.ACTION_UP,
//                        coords[0],
//                        coords[1],
//                        metaState
//                );
//
//                mView.dispatchTouchEvent(motionEvent);



            }
        });

        mView.setOnTouchListener(new View.OnTouchListener() {
            final WindowManager.LayoutParams mParamsUpdated = mParams;
            double x;
            double y;
            double px;
            double py;
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = mParamsUpdated.x;
                        y = mParamsUpdated.y;

                        px = event.getRawX();

                        py = event.getRawY();
//                        Log.i(TAG, "getRawX: " + event.getRawX() + ", getRawY: " + event.getRawY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mView.getLocationOnScreen(coords);
                        Log.i(TAG, "x: " +  coords[0] + ", y: " + coords[1]);

                        mParamsUpdated.x = (int) (x + (event.getRawX() - px));
                        mParamsUpdated.y = (int) (y + (event.getRawY() - py));
//                        Log.i(TAG, "getRawX: " + event.getRawX() + ", getRawY: " + event.getRawY());
                        mWindowManager.updateViewLayout(v, mParamsUpdated);
                        break;
                }
                return false;
            }
            });

    }

    public void open() {
        try {
            if (mView.getWindowToken() == null /*&& mView.getParent() == null*/)
                mWindowManager.addView(mView, mParams);

        } catch (Exception e) {
            Log.d("Error1",e.toString());
        }
    }

    public void close() {
        try {
            if (mView.getWindowToken() != null) {
                mWindowManager.removeView(mView);
//            ((WindowManager) context.getSystemService(WINDOW_SERVICE)).removeView(mView);
                mView.invalidate();
                Log.i("AOBA", "Window: " + new Intent(context, ForegroundService.class).toString());
//            stopService(new Intent(context, ForegroundService.class));
                context.stopService(new Intent(context, ForegroundService.class));
//            ((ViewGroup)mView.getParent()).removeAllViews();
            }

        } catch (Exception e) {
            Log.d("Error2",e.toString());
        }
    }

}
