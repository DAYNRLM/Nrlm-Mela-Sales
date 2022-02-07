package com.nrlm.melasalesoffline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.utils.AppSharedPreferences;
import com.nrlm.melasalesoffline.utils.AppUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyMpinActivity extends AppCompatActivity {
    int counter;
    @BindView(R.id.indicator_dots)
    IndicatorDots indicator_dots;
    @BindView(R.id.pin_lock_view)
    PinLockView pin_lock_view;
    @BindView(R.id.tv_mpie_error)
    TextView tv_mpie_error;
    AppSharedPreferences appSharedPreferences;
    AppUtility appUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mpin);
        ButterKnife.bind(this);
        appSharedPreferences = AppSharedPreferences.getsharedprefInstances(VerifyMpinActivity.this);
        appUtils = AppUtility.getInstance();
        pin_lock_view.attachIndicatorDots(indicator_dots);
        pin_lock_view.setPinLength(4);
        String getMpin = appSharedPreferences.getMpinStatus();
        counter = Integer.parseInt(appSharedPreferences.getMpinCount());
        pin_lock_view.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                if (isTimeValidForLogin()) {
                    tv_mpie_error.setVisibility(View.GONE);
                }
                if (counter < 1) {
                    if (appSharedPreferences.getCountDownTime().equalsIgnoreCase("")) {
                        checkTime();
                        tv_mpie_error.setVisibility(View.VISIBLE);
                        tv_mpie_error.setText("Wrong PIN limit reached. Try after 1 minutes.");
                        Toast.makeText(VerifyMpinActivity.this, "not allowed", Toast.LENGTH_SHORT).show();
                    } else {

                        tv_mpie_error.setVisibility(View.VISIBLE);
                        tv_mpie_error.setText("Wrong PIN limit reached. Try after 1 minutes.");
                        Toast.makeText(VerifyMpinActivity.this, "not allowed", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (pin.equalsIgnoreCase(getMpin)) {
                        Intent intent = new Intent(VerifyMpinActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(VerifyMpinActivity.this, "Mpin is Wrong please enter Right Mpin.", Toast.LENGTH_SHORT).show();
                        pin_lock_view.resetPinLockView();
                        tv_mpie_error.setVisibility(View.VISIBLE);
                        tv_mpie_error.setText("Wrong PIN " + counter + " attempt remaining.");
                        counter = counter - 1;
                        appSharedPreferences.setMpinCount("" + counter);
                        Toast.makeText(VerifyMpinActivity.this, "" + counter, Toast.LENGTH_SHORT).show();
                    }
                }
/*
                if(pin.equalsIgnoreCase(getMpin)){
                    Intent intent =new Intent(VerifyMpinActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    pin_lock_view.resetPinLockView();
                    Toast.makeText(VerifyMpinActivity.this,getString(R.string.entered_mpin_is_wrong),Toast.LENGTH_SHORT).show();
                }*/
            }
            @Override
            public void onEmpty() {
            }
            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });
    }
    public boolean isTimeValidForLogin() {
        boolean status = false;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (appSharedPreferences.getCountDownTime().equalsIgnoreCase("")) {
            status = false;
        } else {
            Date savedDateTime = getDateFormate(appSharedPreferences.getCountDownTime());
            Date currentDateTime = getDateFormate(df.format(calendar.getTime()));
            appUtils.showLog("savedDateTime   ::" + savedDateTime, VerifyMpinActivity.class);
            appUtils.showLog("currentDateTime   ::" + currentDateTime, VerifyMpinActivity.class);
            if (currentDateTime.compareTo(savedDateTime) >= 0) {
                status = true;
                appSharedPreferences.setCountDownTime("");
                appSharedPreferences.setMpinCount("3");
            }
        }
        return status;
    }
    public Date getDateFormate(String date) {
        Date convertedDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            convertedDate = sdf.parse(date);
            sdf.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
    private void checkTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        appUtils.showLog("diff time   ::" + df.format(calendar.getTime()), VerifyMpinActivity.class);
        // Add 10 minutes to current date
        calendar.add(Calendar.MINUTE, 1);
        appUtils.showLog("time after 10  min.  ::" + df.format(calendar.getTime()), VerifyMpinActivity.class);
        appSharedPreferences.setCountDownTime("" + df.format(calendar.getTime()));
        df.format(calendar.getTime());
    }
}