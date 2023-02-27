package com.example.autoclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.autoclicker.service.ForegroundService;
import com.example.autoclicker.service.MyService;
import com.example.autoclicker.service.Window;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    private final String TAG = MyService.class.getSimpleName();
    private Button start;
    private Button close;
    private TextView mainText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.start);
        close = findViewById(R.id.close);
        mainText = findViewById(R.id.text);
        Log.d(TAG, "Method: onCreate");

        Window window = new Window(this);

        checkOverlayPermission();


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService();
                window.open();
                MyService mm = new MyService();
                mm.doAction();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.close();
            }
        });
        mainText.setOnTouchListener(this);



        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 0.0f;
        float y = 0.0f;
// List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
        int metaState = 0;
        @SuppressLint("Recycle") MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );

//        view.dispatchTouchEvent(motionEvent);


    }

    private void startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(this, ForegroundService.class));
                } else
                    startService(new Intent(this, ForegroundService.class));
            }
        } else
            startService(new Intent(this, ForegroundService.class));
    }

    private void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        startService();
    }

    private int _xDelta;
    private int _yDelta;

    private float downX;
    private float downY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent mv) {
        switch (mv.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = mv.getX();
                downY = mv.getY();
//                Log.i(TAG, "Action Down: " + mv.getRawX() + "," + mv.getRawY());
                Log.i(TAG, "Action Move V: " + v.getX() + "," + mv.getY());

            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG, "Action Move: " + mv.getRawX() + "," + mv.getRawY());
                Log.i(TAG, "Action Move V: " + v.getX() + "," + mv.getY());
                float dx, dy;
                dx = mv.getX() - downX;
                dy = mv.getY() - downY;

                v.setX(v.getX() + dx);
                v.setY(v.getY() + dy);
        }
        return true;
    }

}
