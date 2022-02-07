package com.nrlm.melasalesoffline.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.activity.BillGenerateActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Type;

public class PrintUtil {
    static Context context=null;
    private static Context getContext(){

       if (context==null){
           context= BillGenerateActivity.context;
       }
        return context ;
    }

    public static String connectionInfo(Activity mActivity) {
        String result = "Not Connected";

        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        AppUtility.getInstance().showLog("NetworkInfo[] netInfo"+netInfo, com.nrlm.melasalesoffline.utils.PrintUtil.class);
        if (netInfo!=null) {
            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                if (netInfo.isConnected()) {
                    result = AppConstant.CONTROLLER_WIFI;

                }
            } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                if (netInfo.isConnected()) {
                    result = AppConstant.CONTROLLER_MOBILE;
                }
            }
        }else Toast.makeText(getContext(),context.getString(R.string.please_connect_to_the_internet), Toast.LENGTH_SHORT);
            AppUtility.getInstance().showLog("resultofinternet"+result, com.nrlm.melasalesoffline.utils.PrintUtil.class);
        return result;
    }

    public static void savePrinterConfiguration(Activity mActivity, WifiConfiguration mPrinterConfiguration) {
        Gson mGson = new Gson();
        Type mType = new TypeToken<WifiConfiguration>() {
        }.getType();
        String sJson = mGson.toJson(mPrinterConfiguration, mType);
        AppSharedPreferences.getsharedprefInstances(context).setPrinterConfiguration(sJson);
    }

    public static WifiConfiguration getWifiConfiguration(Activity mActivity, String configurationType) {
        WifiConfiguration mWifiConfiguration = new WifiConfiguration();
        try {
            Gson mGson = new Gson();
            Type mWifiConfigurationType = new TypeToken<WifiConfiguration>() {
            }.getType();
            String mWifiJson = "";
            AppUtility.getInstance().showLog("mWifiConfigurationType"+mWifiConfigurationType, com.nrlm.melasalesoffline.utils.PrintUtil.class);
            if (configurationType.equalsIgnoreCase(AppConstant.CONTROLLER_WIFI)) {
                mWifiJson=AppSharedPreferences.getsharedprefInstances(context).getWifiConfig();
            } else if (configurationType.equalsIgnoreCase(AppConstant.CONTROLLER_PRINTER)) {
                mWifiJson=AppSharedPreferences.getsharedprefInstances(context).getPrinterConfiguration();
            }
            if (!mWifiJson.isEmpty()) {
                mWifiConfiguration = mGson.fromJson(mWifiJson, mWifiConfigurationType);
            } else {
                mWifiConfiguration = null;
            }
        }catch (Exception e){
            AppUtility.getInstance().showLog("mWifiConfigurationExp"+e,PrintUtil.class);
        }

        return mWifiConfiguration;
    }

    public static void storeCurrentWiFiConfiguration(Activity mActivity) {
        try {
            WifiManager wifiManager = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                WifiConfiguration mWifiConfiguration = new WifiConfiguration();
                mWifiConfiguration.networkId = connectionInfo.getNetworkId();
                mWifiConfiguration.BSSID = connectionInfo.getBSSID();
                mWifiConfiguration.hiddenSSID = connectionInfo.getHiddenSSID();
                mWifiConfiguration.SSID = connectionInfo.getSSID();

                // store it for future use -> after print is complete you need to reconnect wifi to this network.
                saveWifiConfiguration(mActivity, mWifiConfiguration);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveWifiConfiguration(Activity mActivity, WifiConfiguration mWifiConfiguration) {
        Gson mGson = new Gson();
        Type mType = new TypeToken<WifiConfiguration>() {
        }.getType();
        String sJson = mGson.toJson(mWifiConfiguration, mType);
        AppUtility.getInstance().showLog("sJson"+sJson, com.nrlm.melasalesoffline.utils.PrintUtil.class);
        AppSharedPreferences.getsharedprefInstances(context).setWifiConfig(sJson);

    }

    public static int computePDFPageCount(File file) {
        RandomAccessFile raf = null;
        int pages = 1;
        try {
            raf = new RandomAccessFile(file, "r");

            RandomAccessFileOrArray pdfFile = new RandomAccessFileOrArray(
                    new RandomAccessSourceFactory().createSource(raf));
            PdfReader reader = new PdfReader(pdfFile, new byte[0]);
            pages = reader.getNumberOfPages();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pages;
    }

}
