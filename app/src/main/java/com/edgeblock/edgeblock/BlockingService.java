package com.edgeblock.edgeblock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BlockingService extends Service {
    private static final int NOTI_CODE = 39;

    private NotificationManager nm;
    private NotificationCompat.Builder mBuilder;
    private Intent intent;
    private PendingIntent pIntent;

    private WindowManager windowManager;

    private LinearLayout leftLayout, rightLayout;
    private Notification noti;
    public BlockingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int areaWidth = (int)(((double)dm.widthPixels)*0.04);

        final WindowManager.LayoutParams mParamsLeft = new WindowManager.LayoutParams(
                areaWidth, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        mParamsLeft.gravity = Gravity.LEFT;

        final WindowManager.LayoutParams mParamsRight = new WindowManager.LayoutParams(
                areaWidth, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        mParamsRight.gravity = Gravity.RIGHT;


        leftLayout = new LinearLayout(this);
        rightLayout = new LinearLayout(this);

        leftLayout.setBackgroundColor(Color.TRANSPARENT);
        rightLayout.setBackgroundColor(Color.TRANSPARENT);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(leftLayout, mParamsLeft);
        windowManager.addView(rightLayout, mParamsRight);
        

        Toast.makeText(getApplicationContext(), "On", Toast.LENGTH_SHORT).show();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        turnOnNotification();
        startForeground(NOTI_CODE, noti);

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        Log.d("TAG", "onDestroy Service...");
        windowManager.removeViewImmediate(leftLayout);
        windowManager.removeViewImmediate(rightLayout);
        windowManager = null;
        stopForeground(true);
        Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    private void turnOnNotification(){

        intent = new Intent(getApplicationContext(), MainActivity.class);
        pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap iconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(iconBitmap)
                .setContentTitle("EdgeBlock")
                .setContentText("Now Blocking...")
                .setContentIntent(pIntent);
        noti = mBuilder.build();
        noti.flags = noti.FLAG_NO_CLEAR;

    }


}
