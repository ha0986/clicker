package com.example.autoclicker.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends AccessibilityService {
    private static final String TAG = "MyService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pressLocation(500, 500);
            }
        }, 0, 1000);//put here time 1000 milliseconds=1 second



//        new Thread(() -> {
//            Looper.prepare();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            pressLocation(500, 500);
//            click(3, 0);
//            doRightThenDownDrag();
//            Log.i(TAG, "onAccessibilityEvent: ");
//        }).start();

    }








    private void click(int x, int y) {
        Log.i(TAG, String.format("click %d %d", x, y));
        Path path = new Path();
        path.moveTo(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.StrokeDescription(path, 10, 1000))
                .build();
        dispatchGesture(gestureDescription, null, null);
    }


    @Override
    public void onInterrupt() {
        Log.i(TAG, "onInterrupt: ");
    }

    @Override
    public void onServiceConnected() { // TODO Протестировать accessibility_service_config без задания параметров в этом методе
        super.onServiceConnected();

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
                AccessibilityEvent.TYPE_VIEW_FOCUSED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        this.setServiceInfo(info);

        Log.i(TAG, "onServiceConnected: " + this.getServiceInfo());

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

    // (x, y) in screen coordinates
    private static GestureDescription createClick(float x, float y) {
        // for a single tap a duration of 1 ms is enough
        final int DURATION = 1;

        Path clickPath = new Path();
        clickPath.moveTo(x, y);
        GestureDescription.StrokeDescription clickStroke =
                new GestureDescription.StrokeDescription(clickPath, 0, DURATION);
        GestureDescription.Builder clickBuilder = new GestureDescription.Builder();
        clickBuilder.addStroke(clickStroke);
        return clickBuilder.build();
    }

    public void doSomething() {
        // callback invoked either when the gesture has been completed or cancelled
        AccessibilityService.GestureResultCallback callback = new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, "gesture completed");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, "gesture cancelled");
            }
        };

        boolean result = dispatchGesture(createClick(5, 200), callback, null);
        Log.d(TAG, "Gesture dispatched? " + result);
    }


    public void pressLocation(int x, int y) {
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path p = new Path();
        p.moveTo(x, y);
        p.lineTo(x + 10, y + 10);
        builder.addStroke(new GestureDescription.StrokeDescription(p, 10L, 500L));
        GestureDescription gesture = builder.build();
        boolean isDispatched = dispatchGesture(gesture, null, null);

        Toast.makeText(this, "Was it dispatched? " + isDispatched, Toast.LENGTH_SHORT).show();
    }

}
