package com.edgeblock.edgeblock;

import android.Manifest;

import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * MainActivity.java
 * @author Taehyen
 *
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String SHARED_PREF_KEY = "EDGEBLOCK";
    private static final String KEY_SWITCH = "key_switch";

    /**
     * View Init..
     */
    @BindView(R.id.main_switch) Switch notiSwitch;
    @BindView(R.id.main_edge_left) LinearLayout leftEdge;
    @BindView(R.id.main_edge_right) LinearLayout rightEdge;

    private boolean initSwitchValue;

    /**
     * Permission Listener (using TedPermission)
     */
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

    @OnCheckedChanged(R.id.main_switch)
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        Log.d(TAG, "Check Changed..: "+isChecked);
        if(isChecked){
            blockingStart();
        }else{
            blockingStop();
        }
        updateUIEdgeBox(isChecked);
        savePref(isChecked);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getPermission();
        init();
    }

    /**
     * Init
     */
    private void init(){
        initSwitchValue = getPref();

        notiSwitch.setChecked(initSwitchValue);
        updateUIEdgeBox(initSwitchValue);
    }


    /**
     * Activate 됬을 때 양쪽 모서리에 실행중인 느낌을 주기위해 박스 생성
     * @param isActivated is Switch ON??
     */
    private void updateUIEdgeBox(boolean isActivated){
        if(isActivated){
            leftEdge.setVisibility(View.VISIBLE);
            rightEdge.setVisibility(View.VISIBLE);
        }else{
            leftEdge.setVisibility(View.GONE);
            rightEdge.setVisibility(View.GONE);
        }
    }

    /**
     * Get Permission
     */
    private void getPermission(){
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .check();
    }

    /**
     * 서비스 실행
     */
    private void blockingStart(){
        startService(new Intent(this, BlockingService.class));
    }

    /**
     * 서비스 종료
     */
    private void blockingStop(){
        stopService(new Intent(this, BlockingService.class));

    }

    /**
     * 상태 저장 ( 다시 실행했을 때 상태에 맞게 UI 갱신하기 위해 )
     * @param state
     */
    private void savePref(boolean state){
        SharedPreferences pref = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_SWITCH, state);
        editor.commit();
    }

    /**
     * 상태 가져오기 ( 다시 실행했을 때 상태에 맞게 UI 갱신하기 위해 )
     * @return 상태 bool
     */
    private boolean getPref(){
        SharedPreferences pref = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        return pref.getBoolean(KEY_SWITCH, false);
    }
}
