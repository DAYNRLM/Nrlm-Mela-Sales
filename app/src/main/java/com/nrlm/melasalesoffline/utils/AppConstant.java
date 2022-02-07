 package com.nrlm.melasalesoffline.utils;

import android.os.Environment;
import android.provider.ContactsContract;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

public class AppConstant {
    public static final String MELA_ID = "8";
    //public static final String myAppDir = Environment.getExternalStorageDirectory().toString() + "/MelaSales/";
    public static final String myAppDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getPath() + "/MelaSales/";
    public static final String commoUnsyncFile = "melaBillData.txt";
    public static final String myAppDirCommon = Environment.getExternalStorageState().toString() + "/MelaSales/commonSales/";

    /****for demo****/
   /* public static final String HTTP_TYPE = "https";
    public static final String IP_ADDRESS = "nrlm.gov.in";
    public static final String NRLM_STATUS = "nrlmwebservicedemo";
    public static final String BILL_DOWNLOAD_STATUS = "nrlm.gov.in/nrlmdemo";*/

    /*****for local*****/
   /* public static final String HTTP_TYPE = "http"; 
    public static final String IP_ADDRESS = "10.197.183.105:8080";
    public static final String NRLM_STATUS = "nrlmwebservice";
    public static final String BILL_DOWNLOAD_STATUS = "10.197.183.105:8080/nrlm_local";*/

    /*** for live*/
    public static final String HTTP_TYPE = "https";
    public static final String IP_ADDRESS = "nrlm.gov.in";
    public static final String NRLM_STATUS = "nrlmwebservice";
    public static final String BILL_DOWNLOAD_STATUS = "nrlm.gov.in";

    public static final String LOGIN_URL = HTTP_TYPE + "://" + IP_ADDRESS + "/" + NRLM_STATUS + "/services/mela/sales/login";
    public static final String SHG_PRODUCT_URL = HTTP_TYPE + "://" + IP_ADDRESS + "/" + NRLM_STATUS + "/services/mela/sales/product/detail";
    public static final String SYNC_URL = HTTP_TYPE+"://"+IP_ADDRESS+"/"+NRLM_STATUS+"/services/melaoffline/bill";
    public static final String BILL_HISTORY_URL = HTTP_TYPE+"://"+IP_ADDRESS+"/"+NRLM_STATUS+"/services/melaSales/bill_history";
    public static final String BILL_SEND_URL = "https://nrlm.gov.in/nrlmwebservice/services/invoice/url";
    public static final String CUSTOM_URL = HTTP_TYPE+"://"+BILL_DOWNLOAD_STATUS+"/"+"MelaAction.do?methodName=generateMelaBill&invoice_no=";
   // public static final String CUSTOM_URL = "http://10.197.183.112:8080/nrlmdev_audit_5_2018"+"/"+"MelaAction.do?methodName=generateMelaBill&invoice_no=";
    public static final String RESET_PASSWORD = HTTP_TYPE+"://"+IP_ADDRESS+"/"+NRLM_STATUS+"/services/mela/offline/resetPassword";
    public static final String DUPLICATE_BILL_URL = HTTP_TYPE+"://"+IP_ADDRESS+"/"+NRLM_STATUS+"/services/duplicatebill/melasales";
    public static final String SEND_OTP_URL = HTTP_TYPE+"://"+IP_ADDRESS+"/"+NRLM_STATUS+"/services/login/otpmelasales/message";
    public static final String GET_ALL_SHG_BILL_DATA_URL = HTTP_TYPE+"://"+IP_ADDRESS+"/"+NRLM_STATUS+"/services/melaSales/shgbillsales";

    public static final String NOT_AVAILABLE = "Not Available";
    public static final String CONTROLLER_WIFI = "WIFI";
    public static final String CONTROLLER_PRINTER = "ControlerPrinter";
    public static final String CONTROLLER_MOBILE = "MOBILE";
    public static final String CONTROLLER_RX_PDF_FOLDER = "PrintFolder";
    public static final int  REQUEST_CODE_PRINTER = 3;
    public static final int  REQUEST_CODE_WIFI = 2;
    public static final int  RESULT_CODE_PRINTER = 1;
    public static final int  PRINTER_STATUS_CANCELLED = 0;
    public static final int  PRINTER_STATUS_COMPLETED = 1;
    public static final int NO_DATA=0;


    /**** fire base crash analytics key*****/
    public static final String pppp = "nrlm.gov.in/nrlmdemo";


    //http://10.197.183.114:8080/nrlmdev_audit_5_2018/MelaAction.do?methodName=generateMelaBill&invoice_no=
    //http://10.197.183.114:8080/nrlmdev_audit_5_2018/MelaAction.do?methodName=generateMelaBill&invoice_no=23601-INM&mobile=9129821243";
    //http://10.197.183.105:8080/nrlmwebservice/services/mela/sales/login
    //http://10.197.183.105:8080/nrlmwebservice/services/mela/sales/product/detail
    //http://10.197.183.105:8080/nrlmwebservice/services/duplicatebill/melasales
    //https://nrlm.gov.in/nrlmwebservice/services/invoice/url
    //http://10.197.183.105:8080/nrlmwebservice/services/login/message
    //http://10.197.183.105:8080/nrlmwebservice/services/melaSales/shgbillsales

}
