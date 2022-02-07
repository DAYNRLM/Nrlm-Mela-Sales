package com.nrlm.melasalesoffline.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreferences {
    private static AppSharedPreferences appSharedPrefrences;
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private Context context;


    public AppSharedPreferences(Context context) {
        this.appSharedPrefs = context.getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public static AppSharedPreferences getsharedprefInstances(Context con) {
        if (appSharedPrefrences == null)
            appSharedPrefrences = new AppSharedPreferences(con);
        return appSharedPrefrences;
    }

    public void clearallSharedPrefernce() {
        prefsEditor.clear();
        prefsEditor.commit();
    }

    public void removeSharedPrefKey(String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }

    public String getApi() {
        return appSharedPrefs.getString("Api", "");
    }

    public void setApi(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Api", path);
        prefsEditor.apply();
    }
    public String getFinalBillNo() {
        return appSharedPrefs.getString("finalbillno", "");
    }


    public void setFinalBillNo(String finalBillNo) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("finalbillno", finalBillNo);
        prefsEditor.apply();
    }

    public String getMelaFrom() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyMelaFrom(), "");
    }

    public void setMelaFrom(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyMelaFrom(), path);
        prefsEditor.apply();
    }

    public String getMelaTo() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyMelaTo(), "");
    }

    public void setMelaTo(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyMelaTo(), path);
        prefsEditor.apply();
    }


    public String getVisibleStatus() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyLoginVisibility(), "");
    }

    public void setVisibleStatus(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyLoginVisibility(), path);
        prefsEditor.apply();
    }

    public String getImei() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyImei(), "");
    }

    public void setImei(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyImei(), path);
        prefsEditor.apply();
    }

    public String getMobile() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyMobileNumber(), "");
    }

    public void setMobile(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyMobileNumber(), path);
        prefsEditor.apply();
    }

    public String getDeviceInfo() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyDeviceInfo(), "");
    }

    public void setDeviceInfo(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyDeviceInfo(), path);
        prefsEditor.apply();
    }

    public String getLoginStatus() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyLoginStatus(), "");
    }

    public void setLoginStatus(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyLoginStatus(), path);
        prefsEditor.apply();
    }

    public String getMpinStatus() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyCreateMpinStatus(), "");
    }

    public void setMpinStatus(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyCreateMpinStatus(), path);
        prefsEditor.apply();
    }

    public String getStateShortName() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyStateShortName(), "");
    }

    public void setStateShortName(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyStateShortName(), path);
        prefsEditor.apply();
    }

    public String getShgFolderPath() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyShgFolderPath(), "");
    }

    public void setShgFolderPath(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyShgFolderPath(), path);
        prefsEditor.apply();
    }

    public String getShgInvoiceNumber() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyShgInvoiceNumber(), "");
    }

    public void setShgInvoiceNumber(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyShgInvoiceNumber(), path);
        prefsEditor.apply();
    }

    public String getShgRegId() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyShgRegId(), "");
    }

    public void setShgRegId(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyShgRegId(), path);
        prefsEditor.apply();
    }

    public String getShgCode() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyShgCode(), "");
    }

    public void setShgCode(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyShgCode(), path);
        prefsEditor.apply();
    }

    public void setScanRegID(String path)
    {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyScanShgRegId(), path);
        prefsEditor.apply();
    }
    public String getScanRegId()
    {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyScanShgRegId(),"");
    }

    public void setScanProductId(String path)
    {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyScanProductId(), path);
        prefsEditor.apply();
    }

    public String getScanProductId()
    {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyScanProductId(),"");

    }
    public void setScanDescriptionId(String path)
    {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyScanDescriptionId(), path);
        prefsEditor.apply();
    }

    public String getScanDescriptionId()
    {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyScanDescriptionId(),"NA");
    }

    public void setPrinterConfiguration(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefControllerPrinterConfiguration(), path);
        prefsEditor.apply();
    }

    public String getPrinterConfiguration() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefControllerPrinterConfiguration(), "");
    }

    public void setWifiConfig(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefControllerWifiConfig(), path);
        prefsEditor.apply();
    }

    public String getWifiConfig() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefControllerWifiConfig(), "");
    }


    public void setPdfPath(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefPdfPath(), path);
        prefsEditor.apply();
    }

    public String getPdfPath() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefPdfPath(), "");
    }

    public void setPdfFileName(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefPdfFile(), path);
        prefsEditor.apply();
    }

    public String getPdfFileName() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefPdfFile(), "");
    }

    public void setSendUrlStatus(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeySendUrlStatus(), path);
        prefsEditor.apply();
    }

    public String getSendUrlStatus() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeySendUrlStatus(), "");
    }

    public void setInvoiceNumber(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyInvoiceNumber(), path);
        prefsEditor.apply();
    }

    public String getInvoiceNumber() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyInvoiceNumber(), "");
    }

    public void setOtp(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefGeneratedOtp(), path);
        prefsEditor.apply();
    }

    public String getOtp() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefGeneratedOtp(), "");
    }

    public void setStallNumber(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyStallNo(), path);
        prefsEditor.apply();
    }

    public String getStallNumber() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyStallNo(), "");
    }

    public void setVillageCode(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyVillageCode(), path);
        prefsEditor.apply();
    }

    public String getVillageCode() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyVillageCode(), "");
    }

    /********this key is not removed used to maintain logout session after 1 day *************/
    public void setSessionDate(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyLoginSessionDate(), path);
        prefsEditor.apply();
    }

    public String getSessionDate() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyLoginSessionDate(), "");
    }

    public void setLogoutTime(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyLogoutTime(), path);
        prefsEditor.apply();
    }

    public String getMpinCount() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyMpinCounter(), "3");
    }

    public void setMpinCount(String value)
    {
        this.prefsEditor=appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyMpinCounter(),value);
        prefsEditor.apply();
    }

    public String getCountDownTime() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyCountdownTime(), "");
    }

    public void setCountDownTime(String value)
    {
        this.prefsEditor=appSharedPrefs.edit();
        prefsEditor.putString(PrefrenceManager.getPrefKeyCountdownTime(),value);
        prefsEditor.apply();
    }

    public String getLogoutTime() {
        return appSharedPrefs.getString(PrefrenceManager.getPrefKeyLogoutTime(), "");
    }
    /*******************************************************************************************/

    public void clearAllKeyPref(){

        removeSharedPrefKey(PrefrenceManager.getPrefKeyMelaFrom()); //mela from
        removeSharedPrefKey(PrefrenceManager.getPrefKeyMelaTo()); // mela to
        removeSharedPrefKey(PrefrenceManager.getPrefKeyLoginVisibility());// login view visibilty status
        removeSharedPrefKey(PrefrenceManager.getPrefKeyImei());// imei number
        removeSharedPrefKey(PrefrenceManager.getPrefKeyMobileNumber());// user mobile number
        removeSharedPrefKey(PrefrenceManager.getPrefKeyDeviceInfo());// device info

        removeSharedPrefKey((PrefrenceManager.getPrefKeyLoginStatus())); // login status
        removeSharedPrefKey((PrefrenceManager.getPrefKeyShgFolderPath()));// shg folder path
        removeSharedPrefKey((PrefrenceManager.getPrefKeyShgRegId()));// shg reg id
        removeSharedPrefKey((PrefrenceManager.getPrefKeyShgCode()));// shg code
        removeSharedPrefKey((PrefrenceManager.getPrefKeyStallNo()));// stall number
        removeSharedPrefKey((PrefrenceManager.getPrefKeyVillageCode()));// village code


        removeSharedPrefKey((PrefrenceManager.getPrefKeyStateShortName()));// state short name



        removeSharedPrefKey((PrefrenceManager.getPrefKeyInvoiceNumber()));//invoice number
        removeSharedPrefKey((PrefrenceManager.getPrefKeyShgCode()));//send url status
        removeSharedPrefKey((PrefrenceManager.getPrefKeyScanShgRegId())); //scan shg reg id
        removeSharedPrefKey((PrefrenceManager.getPrefKeyScanProductId()));//scan product id
        removeSharedPrefKey((PrefrenceManager.getPrefKeyScanDescriptionId()));// scan description id
        removeSharedPrefKey((PrefrenceManager.getPrefPdfPath()));//pdf folde create path
        removeSharedPrefKey((PrefrenceManager.getPrefPdfFile()));//pdf file  path

    }

}
