package com.example.autoclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autoclicker.service.ForegroundService;
import com.example.autoclicker.service.MyService;
import com.example.autoclicker.service.Window;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    private static final String TAG = "MainActivity";
    private Button start;
    private Button close;
    private TextView mainText;
    private Button access_perm;
    private Window window;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.start);
        close = findViewById(R.id.close);
        mainText = findViewById(R.id.text);
        access_perm = findViewById(R.id.acces_perm);
        Log.d(TAG, "onCreate: ");

        window = new Window(this);

        checkOverlayPermission();

        access_perm.setOnClickListener(view -> {

        });
        start.setOnClickListener(view -> {

            if (!checkAccessibilityPermission()){
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }else{
                startService();
                window.open();
                MyService mm = new MyService();
                mm.doAction();
            }
        });

        close.setOnClickListener(view -> window.close());
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

    public boolean checkAccessibilityPermission () {
        int accessEnabled = 0;
        try {
            accessEnabled = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessEnabled == 0) {
            // if not construct intent to request permission
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // request permission via start activity for result
            startActivity(intent);
            return false;
        } else {
            return true;
        }
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
        Log.i(TAG, "onResume: ");
        super.onResume();
        startService();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        window.close();
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "onRestart: ");
        super.onRestart();
    }

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
