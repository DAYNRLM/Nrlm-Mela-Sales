package com.nrlm.melasalesoffline.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import com.nrlm.melasalesoffline.BuildConfig;

public class DeviceInfoutils {
    Context context;
    private TelephonyManager telephonyManager;
    AppUtility appUtility;

    public static DeviceInfoutils deviceInfoutils = null;
    public static DeviceInfoutils getInstance(Context context) {
        if (deviceInfoutils == null)
            deviceInfoutils = new DeviceInfoutils(context);
        return deviceInfoutils;
    }

    public DeviceInfoutils(Context context) {
        this.context = context;
        appUtility =AppUtility.getInstance();
    }

    public String getIMEINo1() {
        String imeiNo1 = "";
        try {
            if (getSIMSlotCount() > 0) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                }
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imeiNo1 = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

                } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imeiNo1 = telephonyManager.getDeviceId(0);

                }else if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    imeiNo1 ="dummy_123456789";
                }

            } else imeiNo1 = telephonyManager.getDeviceId();
        }catch (Exception e){
            appUtility.showLog("IMEI Exception:- "+e,DeviceInfoutils.class);
        }
        return imeiNo1;
    }
    private int getSIMSlotCount() {
        int getPhoneCount = 0;
        try {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPhoneCount = telephonyManager.getPhoneCount();
            }
        }catch (Exception e){
            appUtility.showLog("IMEI Sim Slot Exception:- "+e,DeviceInfoutils.class);
        }
        return getPhoneCount;
    }

    public String getDeviceInfo() {
        String deviceInfo = "";
        try{
            deviceInfo = Build.MANUFACTURER + "-" + Build.DEVICE + "-" + Build.MODEL;
        }catch (Exception e){
            appUtility.showLog("Device info Exception:- "+e,DeviceInfoutils.class);
        }

        if (deviceInfo.equalsIgnoreCase("")|| deviceInfo==null)
            return "";
        return deviceInfo;
    }

    public String getAppVersionFromLocal() {

        String appVersion = "";

        try {
            appVersion =  BuildConfig.VERSION_NAME;;
        }catch (Exception e){
            appUtility.showLog("App Version Exception:- "+e,DeviceInfoutils.class);
        }
        return appVersion;
    }
}
