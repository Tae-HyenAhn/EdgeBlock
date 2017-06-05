package com.edgeblock.edgeblock;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final String SHARED_PREF_KEY = "EDGEBLOCK";
    private static final String KEY_SWITCH = "key_switch";



    private Switch notiSwitch;

    private boolean initSwitchValue;

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_ment), Toast.LENGTH_SHORT).show();
            finish();
        }

    };


    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){

                blockingStart();
            }else{

                blockingStop();
            }
            savePref(isChecked);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();
        init();
    }

    private void init(){
        initSwitchValue = getPref();
        notiSwitch = (Switch) findViewById(R.id.main_switch);
        notiSwitch.setChecked(initSwitchValue);
        notiSwitch.setOnCheckedChangeListener(checkedChangeListener);
    }

    private void getPermission(){
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .check();
    }

    private void blockingStart(){
        startService(new Intent(this, BlockingService.class));
    }

    private void blockingStop(){
        stopService(new Intent(this, BlockingService.class));

    }

    private void savePref(boolean state){
        SharedPreferences pref = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_SWITCH, state);
        editor.commit();
    }

    private boolean getPref(){
        SharedPreferences pref = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        return pref.getBoolean(KEY_SWITCH, false);
    }
}
