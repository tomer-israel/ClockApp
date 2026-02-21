package com.example.clockapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int OVERLAY_PERMISSION_REQUEST = 1001;

    private TextView clockTextView;
    private TextView dateTextView;
    private Handler handler;
    private Runnable updateTimeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clockTextView = findViewById(R.id.clockTextView);
        dateTextView = findViewById(R.id.dateTextView);

        handler = new Handler(Looper.getMainLooper());
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                handler.postDelayed(this, 1000);
            }
        };

        requestOverlayPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(updateTimeRunnable);
        stopService(new Intent(this, ClockOverlayService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTimeRunnable);
        if (Settings.canDrawOverlays(this)) {
            Intent serviceIntent = new Intent(this, ClockOverlayService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        }
    }

    private void requestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Please enable 'Draw over other apps' for the floating clock", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST);
        }
    }

    private void updateTime() {
        java.util.Date adjustedTime = TimeUtils.getAdjustedTime();
        clockTextView.setText(TimeUtils.formatTime(adjustedTime));
        dateTextView.setText(TimeUtils.formatDate(adjustedTime));
    }
}
