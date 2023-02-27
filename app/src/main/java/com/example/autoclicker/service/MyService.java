package com.example.autoclicker.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyService extends AccessibilityService {
    private final String TAG = MyService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
//        Log.d(TAG, "Method: onServiceConnected");
//        startActivity(new Intent(this, MainActivity.class)
//                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    public void doAction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            performGlobalAction(GLOBAL_ACTION_RECENTS);
        }
    }

    private void doRightThenDownDrag() {
        Path dragRightPath = new Path();
        dragRightPath.moveTo(200, 200);
        dragRightPath.lineTo(400, 200);
        long dragRightDuration = 500L; // 0.5 second

        // The starting point of the second path must match
        // the ending point of the first path.
        Path dragDownPath = new Path();
        dragDownPath.moveTo(400, 200);
        dragDownPath.lineTo(400, 400);
        long dragDownDuration = 500L;
        GestureDescription.StrokeDescription rightThenDownDrag =
                new GestureDescription.StrokeDescription(dragRightPath, 0L,
                        dragRightDuration, true);
        rightThenDownDrag.continueStroke(dragDownPath, dragRightDuration,
                dragDownDuration, false);
    }
}
