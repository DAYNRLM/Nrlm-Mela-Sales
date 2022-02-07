package com.nrlm.melasalesoffline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.database.entity.BillSyncEntity;
import com.nrlm.melasalesoffline.database.entity.MainHelperEntity;
import com.nrlm.melasalesoffline.database.entity.ProductDescriptionEntity;
import com.nrlm.melasalesoffline.database.entity.ProductEntity;
import com.nrlm.melasalesoffline.database.entity.ShgDetailEntity;
import com.nrlm.melasalesoffline.database.repository.CommonRepository;
import com.nrlm.melasalesoffline.fragment.DashBoardFragment;
import com.nrlm.melasalesoffline.utils.AppConstant;
import com.nrlm.melasalesoffline.utils.AppSharedPreferences;
import com.nrlm.melasalesoffline.utils.AppUtility;
import com.nrlm.melasalesoffline.utils.Cryptography;
import com.nrlm.melasalesoffline.utils.DateFactory;
import com.nrlm.melasalesoffline.utils.DeviceInfoutils;
import com.nrlm.melasalesoffline.utils.DialogFactory;
import com.nrlm.melasalesoffline.utils.FilesUtils;
import com.nrlm.melasalesoffline.utils.LocationMasterUtils;
import com.nrlm.melasalesoffline.utils.NetworkFactory;
import com.nrlm.melasalesoffline.webService.customvolley.T;
import com.nrlm.melasalesoffline.webService.volley.VolleyResult;
import com.nrlm.melasalesoffline.webService.volley.VolleyService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.mobilNumberEt)
    TextInputEditText mobilNumberEt;

    @BindView(R.id.mobulInputLayout)
    TextInputLayout mobulInputLayout;

    @BindView(R.id.spinnerTextLayout)
    TextInputLayout spinnerTextLayout;

    @BindView(R.id.passwordET)
    TextInputEditText passwordET;

    @BindView(R.id.passwordInputLayout)
    TextInputLayout passwordInputLayout;

    @BindView(R.id.shgNameSpinner)
    AutoCompleteTextView shgNameSpinner;

    @BindView(R.id.loginBtn)
    MaterialButton loginBtn;

    @BindView(R.id.gotoHomeBtn)
    MaterialButton gotoHomeBtn;

    @BindView(R.id.loginLinearLayout)
    LinearLayout loginLinearLayout;

    @BindView(R.id.goToHomeLayout)
    LinearLayout goToHomeLayout;

    @BindView(R.id.forgetPassworLL)
    LinearLayout forgetPassworLL;

    ArrayAdapter<String> shgDetailAdapter;

    List<ShgDetailEntity> shgDataItemList;

    DeviceInfoutils deviceInfoutils;
    LocationMasterUtils locationMasterUtils;
    DateFactory dateFactory;
    AppUtility appUtility;
    CommonRepository commonRepository;
    AppSharedPreferences appSharedPreferences;
    FilesUtils filesUtils;

    VolleyResult mResultCallBack = null;
    VolleyService volleyService;
    Runnable runnable;
    Handler handler;


    String imeiNumber = "";
    String deviceInfo = "";
    String appVersion = "";
    String location = "00.00";
    String shgRegId = "";
    String shgCode = "";
    String villageCode = "";
    String stallNo = "";
    String stateShortName = "";
    String mobileNumber = "";
    String password = "";
    String otpMobileNumber = "";
    String generatedOTP = "";
    String firebaseToken = "";

    /* FirebaseCrashlytics.getInstance().setCustomKey("test","test-key");
        FirebaseCrashlytics.getInstance().log("Higgs-Boson detected! Bailing out");
        FirebaseCrashlytics.getInstance().setUserId("12345");
        throw new RuntimeException("Test Crash");*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        deviceInfoutils = DeviceInfoutils.getInstance(LoginActivity.this);
        locationMasterUtils = LocationMasterUtils.getInstance(LoginActivity.this);

       /* imeiNumber = "e80de5c319468045";// deviceInfoutils.getIMEINo1();                 //IMEI and device info has been change temp
        deviceInfo = "Xiaomi-laurel_sprout-Mi A3"; //deviceInfoutils.getDeviceInfo();*/

        /*imeiNumber = "1234";// deviceInfoutils.getIMEINo1();                 //IMEI and device info has been change temp
        deviceInfo = "1234"; //deviceInfoutils.getDeviceInfo();*/


        imeiNumber = deviceInfoutils.getIMEINo1();
        deviceInfo = deviceInfoutils.getDeviceInfo();

        appVersion = deviceInfoutils.getAppVersionFromLocal();
        location = locationMasterUtils.getLocation();
        dateFactory = DateFactory.getInstance();
        appUtility = AppUtility.getInstance();

        volleyService = VolleyService.getInstance(LoginActivity.this);
        commonRepository = CommonRepository.getInstance(LoginActivity.this);
        appSharedPreferences = AppSharedPreferences.getsharedprefInstances(LoginActivity.this);
        filesUtils = FilesUtils.getInstance(LoginActivity.this);
        mobilNumberEt.setLongClickable(false);
        passwordET.setLongClickable(false);
        avoidCopyAtMobileNumber();
        avoidCopyAtPasswordEditText();


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    appUtility.showLog("Fetching FCM registration token failed   " + task.getException(), LoginActivity.class);
                    return;
                }
                appUtility.showLog("Fetching FCM Token  " + task.getResult(), LoginActivity.class);
                firebaseToken = "" + task.getResult();
                appUtility.showLog("check verify   " + firebaseToken, LoginActivity.class);
            }
        });

        appUtility.showLog("****token***  " + firebaseToken, LoginActivity.class);


        String lm = getIntent().getStringExtra("testttt");
        AppUtility.getInstance().showLog("TRY---CATCH****STRING----" + lm, SplashScreen.class);

        // AppUtility.getInstance().showLog("TRY---CATCH****STRING----" +lm , SplashScreen.class);
    /*    mobilNumberEt.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });*/
        mobilNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mobulInputLayout.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordInputLayout.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        /*******create folder if not exist***********/
        if (!filesUtils.isFolderExist(AppConstant.myAppDir)) {
            filesUtils.createNewFolder(AppConstant.myAppDir);
        }

        /***********get selected id from spinner*************/
        shgNameSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spinnerTextLayout.setError(null);

                shgRegId = shgDataItemList.get(position).getShg_reg_id();
                shgCode = shgDataItemList.get(position).getShg_code();
                stallNo = shgDataItemList.get(position).getStall_no();
                villageCode = shgDataItemList.get(position).getVillage_code();
                stateShortName = shgDataItemList.get(position).getState_short_name();
                String folderName = AppConstant.myAppDir + "shg_" + AppConstant.MELA_ID + shgRegId;
                appSharedPreferences.setShgFolderPath(folderName);
                appSharedPreferences.setStateShortName(stateShortName);
                appSharedPreferences.setShgCode(shgCode);
                appSharedPreferences.setShgRegId(shgRegId);
                appSharedPreferences.setVillageCode(villageCode);
                appSharedPreferences.setStallNumber(stallNo);

                if (!filesUtils.isFolderExist(folderName)) {
                    filesUtils.createNewFolder(folderName);
                }
            }
        });

       /* DialogFactory.getInstance(LoginActivity.this).showMaterialDialog("test", 0, false
                , "hooo"
                , "ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }, "hhhh", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });*/

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        };
        startHandler();
    }

    private void avoidCopyAtMobileNumber() {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            mobilNumberEt.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

                @Override
                public void onCreateContextMenu(ContextMenu menu, View v,
                                                ContextMenu.ContextMenuInfo menuInfo) {
                    // TODO Auto-generated method stub
                    menu.clear();
                }
            });
        } else {
            mobilNumberEt.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {
                    // TODO Auto-generated method stub

                }

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }

                public boolean onActionItemClicked(ActionMode mode,
                                                   MenuItem item) {
                    // TODO Auto-generated method stub
                    return false;
                }
            });
        }
    }


    private void avoidCopyAtPasswordEditText() {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            passwordET.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

                @Override
                public void onCreateContextMenu(ContextMenu menu, View v,
                                                ContextMenu.ContextMenuInfo menuInfo) {
                    // TODO Auto-generated method stub
                    menu.clear();
                }
            });
        } else {
            passwordET.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {
                    // TODO Auto-generated method stub

                }

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }

                public boolean onActionItemClicked(ActionMode mode,
                                                   MenuItem item) {
                    // TODO Auto-generated method stub
                    return false;
                }
            });
        }
    }


    @OnClick(R.id.gotoHomeBtn)
    public void goToHome() {
        /*appSharedPreferences.setVisibleStatus("");
        appSharedPreferences.setLoginStatus("ok");*/

        if (NetworkFactory.isInternetOn(LoginActivity.this)) {
            if (!shgRegId.equalsIgnoreCase("")) {
                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("loading.....");
                progressDialog.setCancelable(false);
                progressDialog.show();
                JSONObject masteProductObject = new JSONObject();
                try {
                    masteProductObject.accumulate("melaId", AppConstant.MELA_ID);
                    masteProductObject.accumulate("mobile", appSharedPreferences.getMobile());  //appSharedPreferences.getMobile()
                    masteProductObject.accumulate("imei_no", appSharedPreferences.getImei()); //appSharedPreferences.getImei()
                    masteProductObject.accumulate("device_name", appSharedPreferences.getDeviceInfo());//imeiNumber
                    masteProductObject.accumulate("location_coordinate", "00.00");//deviceInfo
                    masteProductObject.accumulate("state_short_name", stateShortName);
                    masteProductObject.accumulate("shg_reg_id", shgRegId);
                } catch (JSONException e) {
                    e.printStackTrace();
                    appUtility.showLog("json making exception:- " + e, LoginActivity.class);
                }
                /***********Make json object encrypted*********************/

                JSONObject masterProductObjectEncrypted = new JSONObject();
                try {
                    Cryptography cryptography = new Cryptography();
                    String encrypted = cryptography.encrypt(masteProductObject.toString());
                    masterProductObjectEncrypted.accumulate("data", encrypted);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                mResultCallBack = new VolleyResult() {
                    @Override
                    public void notifySuccess(String requestType, JSONObject response) {
                        // progressDialog.dismiss();
                        appUtility.showLog("get response:-" + response, LoginActivity.class);
                        Cryptography cryptography = null;
                        JSONObject jsonObj = null;
                        String objectResponse = "";
                        if (response.has("data")) {
                            try {
                                objectResponse = response.getString("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            return;
                        }
                        try {
                            cryptography = new Cryptography();
                            jsonObj = new JSONObject(cryptography.decrypt(objectResponse));
                            //  AppUtility.getInstance().showLog("LoginResponse"+jsonArr,LoginActivity.class);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseShgProductData(progressDialog, jsonObj);
                    }

                    @Override
                    public void notifyError(String requestType, VolleyError error) {
                        progressDialog.dismiss();
                        appUtility.showLog("volley error:-" + error, LoginActivity.class);
                        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                        materialAlertDialogBuilder.setCancelable(false);
                        materialAlertDialogBuilder.setMessage("Server Error Try Again later...");
                        materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        //show error dialog
                    }
                };
                volleyService.postDataVolley("shg_product_request", AppConstant.SHG_PRODUCT_URL, masterProductObjectEncrypted, mResultCallBack);

            } else {
                spinnerTextLayout.setError("Select SHG first");
            }

        } else {

            //dialog for
        }

       /* if(appSharedPreferences.getMpinStatus().equalsIgnoreCase("")){
            Intent intent =new Intent(LoginActivity.this,CreateMpinActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent =new Intent(LoginActivity.this,VerifyMpinActivity.class);
            startActivity(intent);
            finish();
        }*/


    }

    @OnClick(R.id.forgetPassworLL)
    public void forgetPassword() {
        MaterialButton cancelBtn, sendOTPBtn;
        TextInputEditText otpMobileEt;
        TextInputLayout mobInputLayout;
        if (NetworkFactory.isInternetOn(LoginActivity.this)) {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
            View customLayout = getLayoutInflater().inflate(R.layout.forget_password_number_custom_dialog, null);
            materialAlertDialogBuilder.setView(customLayout);
            materialAlertDialogBuilder.setCancelable(false);
            AlertDialog cusDialog = materialAlertDialogBuilder.show();
            cancelBtn = customLayout.findViewById(R.id.cancelBtn);
            sendOTPBtn = customLayout.findViewById(R.id.sendOTPBtn);
            mobInputLayout = customLayout.findViewById(R.id.mobInputLayout);
            otpMobileEt = customLayout.findViewById(R.id.otpMobileEt);
            otpMobileEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mobInputLayout.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cusDialog.dismiss();
                }
            });
            sendOTPBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    otpMobileNumber = otpMobileEt.getText().toString().trim();
                    if (otpMobileNumber.isEmpty() || otpMobileNumber.length() < 10) {
                        mobInputLayout.setError("Please enter valid mobile number");
                        otpMobileEt.setText("");
                    } else {

                        callOTPApi();
                        cusDialog.dismiss();

                        /*cusDialog.dismiss();
                        appSharedPreferences.setMobile(otpMobileNumber);
                        appSharedPreferences.setOtp(generatedOTP);
                        Intent intent =new Intent(LoginActivity.this,OtpVerificationActivity.class);
                        startActivity(intent);*/

                    }
                }
            });

        } else {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
            materialAlertDialogBuilder.setCancelable(false);
            materialAlertDialogBuilder.setMessage("Please enable your internet...");
            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            materialAlertDialogBuilder.show();
        }

    }

    private void parseShgProductData(ProgressDialog progressDialog, JSONObject response) {
        /*{"data":{"status":"NO data found","Productdetail":[{"status":"No product available !!!"}]},"message":"success","status":0}*/
        try {
            if (response.has("status")) {
                int status = response.getInt("status");
                if (status == 0) {
                    JSONObject jsonData = response.getJSONObject("data");
                    if (jsonData.has("status")) {
                        String getStatus = jsonData.getString("status");
                        if (getStatus.equalsIgnoreCase("NO data found!")) {
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                            materialAlertDialogBuilder.setCancelable(false);
                            materialAlertDialogBuilder.setMessage("NO data found...");
                            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        } else if (getStatus.equalsIgnoreCase("")) {

                        } else {

                        }
                    } else {
                        insetSHGDataInDb(jsonData, progressDialog);
                    }

                } else {
                    // show error msg
                }
            } else {
                Toast.makeText(LoginActivity.this, "Data Not Found...", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            appUtility.showLog("parse Product json exception:- " + e, LoginActivity.class);
            //show dialog for error
        }
    }

    private void insetSHGDataInDb(JSONObject jsonData, ProgressDialog progressDialog) {
        MainHelperEntity mainHelperEntity = new MainHelperEntity();
        String main_code = null;
        try {
            main_code = jsonData.getString("main_code");
            mainHelperEntity.setMain_code(main_code);

            String ass_name = jsonData.getString("ass_name");
            mainHelperEntity.setAss_name(ass_name);

            String ass_mobile = jsonData.getString("ass_mobile");
            mainHelperEntity.setAss_mobile(ass_mobile);

            String main_name = jsonData.getString("main_name");
            mainHelperEntity.setMain_name(main_name);

            String main_participant_mobile = jsonData.getString("main_participant_mobile");
            mainHelperEntity.setMain_participant_mobile(main_participant_mobile);

            String invoice_no = String.valueOf(jsonData.getInt("invoice_no"));
            mainHelperEntity.setInvoice_no(invoice_no);

            mainHelperEntity.setShg_code(shgCode);
            mainHelperEntity.setShg_reg_id(shgRegId);
            mainHelperEntity.setState_short_name(stateShortName);
            mainHelperEntity.setStall_no(stallNo);
            mainHelperEntity.setVillage_code(villageCode);

            appSharedPreferences.setShgInvoiceNumber(invoice_no);
            commonRepository.saveMaineHelper(mainHelperEntity);

            JSONArray productDetailArray = jsonData.getJSONArray("Productdetail");
            for (int i = 0; i < productDetailArray.length(); i++) {
                JSONObject productObject = productDetailArray.getJSONObject(i);

                ProductEntity productEntity = new ProductEntity();
                String available_quantity = productObject.getString("available_quantity");
                productEntity.setAvailable_quantity(available_quantity);
                String subcategory_id = productObject.getString("subcategory_id");
                productEntity.setSubcategory_id(subcategory_id);
                String category_name = productObject.getString("category_name");
                productEntity.setCategory_name(category_name);
                String category_id = productObject.getString("category_id");
                productEntity.setCategory_id(category_id);
                String product_id = productObject.getString("product_id");
                productEntity.setProduct_id(product_id);
                String subcategory_name = productObject.getString("subcategory_name");
                productEntity.setSubcategory_name(subcategory_name);
                String product_name = productObject.getString("product_name");
                productEntity.setProduct_name(product_name);
                productEntity.setShg_code(shgCode);
                productEntity.setShg_reg_id(shgRegId);
                productEntity.setState_short_name(stateShortName);
                productEntity.setStall_no(stallNo);
                productEntity.setVillage_code(villageCode);
                commonRepository.saveProductdata(productEntity);
                JSONArray proDescArray = productObject.getJSONArray("unit_description");
                for (int k = 0; k < proDescArray.length(); k++) {
                    JSONObject proDescObjct = proDescArray.getJSONObject(k);
                    if (proDescObjct.has("status")) {
                    } else {
                        ProductDescriptionEntity productDescriptionEntity = new ProductDescriptionEntity();
                        String unit = proDescObjct.getString("unit");
                        productDescriptionEntity.setUnit(unit);
                        String product_type = proDescObjct.getString("product_type");
                        productDescriptionEntity.setProduct_type(product_type);
                        String product_desc_id = proDescObjct.getString("product_desc_id");
                        productDescriptionEntity.setProduct_desc_id(product_desc_id);
                        String unit_price = proDescObjct.getString("unit_price");
                        productDescriptionEntity.setUnit_price(unit_price);
                        productDescriptionEntity.setProduct_id(product_id);
                        commonRepository.saveProductDescdata(productDescriptionEntity);
                    }
                }
            }
            progressDialog.dismiss();
            appSharedPreferences.setVisibleStatus("");
            appSharedPreferences.setLoginStatus("ok");
            if (appSharedPreferences.getMpinStatus().equalsIgnoreCase("")) {
                Intent intent = new Intent(LoginActivity.this, CreateMpinActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(LoginActivity.this, VerifyMpinActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void callOTPApi() {
        if (NetworkFactory.isInternetOn(LoginActivity.this)) {
            generatedOTP = appUtility.getRandomOtp();
            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("loading.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            JSONObject masterUrlObject = new JSONObject();
            try {
                masterUrlObject.accumulate("mobile", otpMobileNumber);
                masterUrlObject.accumulate("melaId", AppConstant.MELA_ID);
                masterUrlObject.accumulate("otpMessage", generatedOTP);

            } catch (JSONException e) {
                e.printStackTrace();
                appUtility.showLog("OTP json making exception:- " + e, LoginActivity.class);
            }

            /***********making jsonObject encrypted**********/
            JSONObject masterObjectUrlEncrypted = new JSONObject();
            try {
                Cryptography cryptography = new Cryptography();
                String encrypted = cryptography.encrypt(masterUrlObject.toString());
                masterObjectUrlEncrypted.accumulate("data", encrypted);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            mResultCallBack = new VolleyResult() {
                @Override
                public void notifySuccess(String requestType, JSONObject response) {
                    appUtility.showLog("requestType:- " + response.toString(), LoginActivity.class);
                    progressDialog.dismiss();
                    appUtility.showLog("get response:-" + response, LoginActivity.class);
                    //{"data":"","message":"Mobile Number Invalid","status":0}
                    //{"status":"This mobile no. is not exist"}

                    //encrypted data.
                    //{"data":"hfEQI+63t3KVHDHyo+JmSW2lXvJ8YiZDLmrrMAWE5m0="}
                    try {

                        Cryptography cryptography = null;
                        JSONObject jsonObj = null;
                        String objectResponse = "";
                        if (response.has("data")) {
                            try {
                                objectResponse = response.getString("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            return;
                        }
                        try {
                            cryptography = new Cryptography();
                            jsonObj = new JSONObject(cryptography.decrypt(objectResponse));
                            appUtility.showLog("Decrypted response:-" + jsonObj.toString(), LoginActivity.class);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        parseOTPData(jsonObj);

                    } catch (Exception e) {
                        appUtility.showLog("OTP get response Expection:-" + e, LoginActivity.class);
                    }
                }

                @Override
                public void notifyError(String requestType, VolleyError error) {
                    progressDialog.dismiss();
                    appUtility.showLog("volley error:-" + error, LoginActivity.class);
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                    materialAlertDialogBuilder.setCancelable(false);
                    materialAlertDialogBuilder.setMessage("Server error please try again");
                    materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            };
            volleyService.postDataVolley("otp_request", AppConstant.SEND_OTP_URL, masterObjectUrlEncrypted, mResultCallBack);
        } else {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
            materialAlertDialogBuilder.setCancelable(false);
            materialAlertDialogBuilder.setMessage("Please enable your internet...");
            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            materialAlertDialogBuilder.show();
        }
    }

    private void parseOTPData(JSONObject response) {
        try {
            if (response.has("status")) {
                String status = response.getString("status");
                if (status.equalsIgnoreCase("This mobile no. is not exist")) {
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                    materialAlertDialogBuilder.setCancelable(false);
                    materialAlertDialogBuilder.setMessage(status);
                    materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    materialAlertDialogBuilder.show();
                } else {
                    appSharedPreferences.setOtp(generatedOTP);
                    appSharedPreferences.setMobile(otpMobileNumber);
                    Intent intent = new Intent(LoginActivity.this, OtpVerificationActivity.class);
                    Toast.makeText(LoginActivity.this, "OTP: " + generatedOTP, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            } else {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                materialAlertDialogBuilder.setCancelable(false);
                materialAlertDialogBuilder.setMessage("Something Went Wrong Try Again");
                materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                materialAlertDialogBuilder.show();
            }

        } catch (Exception e) {

        }
    }

    /*****
     * login api for get shg details
     * **********/
    @OnClick(R.id.loginBtn)
    public void login() {
        mobileNumber = mobilNumberEt.getText().toString();
        password = passwordET.getText().toString();
        if (mobileNumber.isEmpty() || mobileNumber.length() < 10) {
            mobulInputLayout.setError("Enter valid mobile number");
        } else if (password.isEmpty() || password.length() < 6) {
            passwordInputLayout.setError("Enter 6 digit password");
        } else {
            commonRepository.deleteAllData();
            if (NetworkFactory.isInternetOn(LoginActivity.this)) {
                List<BillSyncEntity> billSyncDataList = commonRepository.getBillDataWithStatus("0");
                if (!billSyncDataList.isEmpty()) {
                    syncLocalDataBase();
                } else {
                    String fileName = AppConstant.myAppDir + AppConstant.commoUnsyncFile;
                    if (filesUtils.isFileExist(fileName)) {
                        syncLocalDataBase();
                    } else {
                        callLoginApi();
                      /*  ProgressDialog progressDialog =new ProgressDialog(LoginActivity.this);
                        progressDialog.setMessage("Loading....");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        Handler handler =new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                fetchLoginDetail(mobileNumber,password);
                            }
                        },2000);*/
                    }
                }
            } else {
                noInterNetConnection(LoginActivity.this);

            }
        }
    }

    private void syncLocalDataBase() {
        List<BillSyncEntity> billSyncDataList = commonRepository.getBillDataWithStatus("0");
        if (!billSyncDataList.isEmpty()) {
            if (NetworkFactory.isInternetOn(LoginActivity.this)) {
                callApiForUnsyncBill();
            }
        } else {
            String fileName = AppConstant.myAppDir + AppConstant.commoUnsyncFile;
            if (filesUtils.isFileExist(fileName)) {
                String jsonFiledata = filesUtils.read_file(fileName);
                appUtility.showLog("red file data:- " + jsonFiledata, LoginActivity.class);
                // {"data":[]}
                try {
                    JSONObject jsonObject = new JSONObject(jsonFiledata);
                    if (jsonObject.has("data")) {
                        JSONArray getArray = jsonObject.getJSONArray("data");
                        if (getArray.length() > 0) {
                            commonRepository.insetFiledataInDb(jsonObject);
                            callApiForUnsyncBill();
                        } else {
                            callLoginApi();
                           /* ProgressDialog progressDialog =new ProgressDialog(LoginActivity.this);
                            progressDialog.setMessage("Loading....");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            Handler handler =new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    fetchLoginDetail(mobileNumber,password);
                                }
                            },2000);*/
                        }
                    } else {
                        callLoginApi();
                       /* ProgressDialog progressDialog =new ProgressDialog(LoginActivity.this);
                        progressDialog.setMessage("Loading....");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        Handler handler =new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                fetchLoginDetail(mobileNumber,password);
                            }
                        },2000);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    appUtility.showLog("red file JSON Expection:- " + e, LoginActivity.class);
                }
            }
            //get data from file and save in local db ....
        }
    }


    private void parseGetData(JSONObject response) {
        //{"data":{"login_attempt":2,"status":"Invalid date!!!"},"message":"success","status":0}
        if (response.has("message") && response.has("status")) {
            try {
                int status = response.getInt("status");
                if (status == 0) {
                    JSONObject dataObject = response.getJSONObject("data");
                    if (dataObject.has("status")) {
                        String getStatus = dataObject.getString("status");
                        if (getStatus.equalsIgnoreCase("Invalid date!!!")) {
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                            materialAlertDialogBuilder.setCancelable(false);
                            materialAlertDialogBuilder.setMessage("Correct your mobile date..");
                            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            materialAlertDialogBuilder.show();
                        }


                        /********Login error for invalid mobile number******************/
                        else if (getStatus.equalsIgnoreCase("Invalid Mobile NO !!!")) {
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                            materialAlertDialogBuilder.setCancelable(false);
                            materialAlertDialogBuilder.setMessage("Invalid mobile number !!!");
                            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            materialAlertDialogBuilder.show();

                        }
                        /*******IMEI used by another person***************/
                   /*     else if(getStatus.equalsIgnoreCase("This IMEI No. is used by another user")){
                            MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(LoginActivity.this);
                            materialAlertDialogBuilder.setCancelable(false);
                            materialAlertDialogBuilder.setMessage("This IMEI No. is used by another user");
                            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            materialAlertDialogBuilder.show();

                        }*/
                        /******************Login error for invalid password**********/
                        else if (getStatus.equalsIgnoreCase("Invalid password!!!")) {
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                            materialAlertDialogBuilder.setCancelable(false);

                            materialAlertDialogBuilder.setTitle("Failed login attempt " + dataObject.getString("login_attempt"));

                            materialAlertDialogBuilder.setMessage("Invalid mobile number or password.");
                            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            materialAlertDialogBuilder.show();

                        }
                        /**********Login error after user exceeded the limit more than 5 blocking him for 15 minuted********/
                        else if (getStatus.equalsIgnoreCase(" Please wait for 5 minutes you exceed limit more than 5 !!!")) {
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                            materialAlertDialogBuilder.setCancelable(false);

                            materialAlertDialogBuilder.setTitle("Failed login attempt " + dataObject.getString("login_attempt"));
                            materialAlertDialogBuilder.setMessage("Please wait for 5 minutes you have exceeded the number of allowed attempts.");
                            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            materialAlertDialogBuilder.show();
                        } else if (getStatus.equalsIgnoreCase("Incorrect Version")) {
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                            materialAlertDialogBuilder.setCancelable(false);
                            materialAlertDialogBuilder.setMessage("Older version please update your application");
                            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    updateApplication();

                                }
                            });
                            materialAlertDialogBuilder.show();

                        }
                        /***********mela closed dialog*************/
                        else if (getStatus.equalsIgnoreCase("Mela Close")) {
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                            materialAlertDialogBuilder.setCancelable(false);
                            materialAlertDialogBuilder.setTitle("Failed login attempt " + dataObject.getString("login_attempt"));
                            materialAlertDialogBuilder.setMessage("Mela is closed.. ");
                            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            materialAlertDialogBuilder.show();

                        }
                        /*******Login error when user try to login with diffrent mobile number***********/
                        else {
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                            materialAlertDialogBuilder.setCancelable(false);
                            materialAlertDialogBuilder.setTitle("Failed login attempt " + dataObject.getString("login_attempt"));
                            materialAlertDialogBuilder.setMessage("This mobile number is registered with " + dataObject.getString("status") + " device");
                            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            materialAlertDialogBuilder.show();
                        }

                    } else {
                        insertLogindataInLocalDb(dataObject);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                appUtility.showLog("parse json exception:- " + e, LoginActivity.class);
            }

        } else {
            //show dialog error in get data
        }


    }

    private void updateApplication() {
        //  final String appPackageName = context.getPackageName();
        // String marketStores = "market://details?id=" + appPackageName;
        String nrlmLiveLocUri = "https://drive.google.com/drive/folders/1CEJEsmna8Q1BIieiIaXoE5do0goeG1Zv?usp=sharing";

        try {
            LoginActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(nrlmLiveLocUri)));
        } catch (android.content.ActivityNotFoundException anfe) {
            //((Activity) context).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            LoginActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(nrlmLiveLocUri)));
        }
        ((Activity) LoginActivity.this).finish();
    }

    private void bindShgData() {
        goToHomeLayout.setVisibility(View.VISIBLE);
        loginLinearLayout.setVisibility(View.GONE);
        appSharedPreferences.setVisibleStatus("1");
        shgDataItemList = commonRepository.getAllShgData();
        List<String> shgItem = new ArrayList<>();
        for (ShgDetailEntity shgEntity : shgDataItemList) {
            String shgName = shgEntity.getGroup_name();
            String stallNo = shgEntity.getStall_no();
            shgItem.add(shgName + " Stall No:- (" + stallNo + ")");
        }
        shgDetailAdapter = new ArrayAdapter<String>(LoginActivity.this, R.layout.spinner_textview, shgItem);
        shgNameSpinner.setAdapter(shgDetailAdapter);
        shgDetailAdapter.notifyDataSetChanged();
    }

    public void insertLogindataInLocalDb(JSONObject dataObject) {
        if (dataObject.has("login_data")) {
            JSONArray shgDataArray = null;
            try {
                shgDataArray = dataObject.getJSONArray("login_data");
                String melaFrom = dataObject.getString("mela_from");
                appSharedPreferences.setMelaFrom(melaFrom);
                String melaTo = dataObject.getString("mela_to");
                appSharedPreferences.setMelaTo(melaTo);

                for (int i = 0; i < shgDataArray.length(); i++) {
                    JSONObject shgDataObject = shgDataArray.getJSONObject(i);
                    ShgDetailEntity shgDetailEntity = new ShgDetailEntity();

                    shgDetailEntity.setGroup_name(shgDataObject.getString("group_name"));
                    shgDetailEntity.setVillage_code(shgDataObject.getString("village_code"));
                    shgDetailEntity.setShg_code(shgDataObject.getString("shg_code"));
                    shgDetailEntity.setStall_no(shgDataObject.getString("stall_no"));
                    shgDetailEntity.setMela_from(melaFrom);
                    shgDetailEntity.setMela_to(melaTo);
                    shgDetailEntity.setShg_reg_id(String.valueOf(shgDataObject.getInt("shg_reg_id")));
                    shgDetailEntity.setState_short_name(shgDataObject.getString("state_short_name"));

                    commonRepository.saveShgData(shgDetailEntity);
                }

                bindShgData();
            } catch (JSONException e) {
                e.printStackTrace();
                appUtility.showLog("SHGDetailsParsingError" + e, LoginActivity.class);
            }
        }
    }

    public void callApiForUnsyncBill() {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        JSONObject jsonObject = null;
        try {
            jsonObject = commonRepository.createBillJson();

        } catch (Exception e) {
            appUtility.showLog("create Bill Json Exception:- " + e, LoginActivity.class);
        }
        /**********encrypting call API for unsync bill******************/
        JSONObject unsyncbillEncryptedObj = new JSONObject();
        try {
            Cryptography cryptography = new Cryptography();
            String encrypted = cryptography.encrypt(jsonObject.toString());
            unsyncbillEncryptedObj.accumulate("data", encrypted);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mResultCallBack = new VolleyResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                progressDialog.dismiss();
                appUtility.showLog("bill sync response:-" + response, LoginActivity.class);
                try {
                    Cryptography cryptography = null;
                    JSONObject jsonObj = null;
                    String objectResponse = "";
                    if (response.has("data")) {
                        objectResponse = response.getString("data");
                    } else {
                        return;
                    }
                    try {
                        cryptography = new Cryptography();
                        jsonObj = new JSONObject(cryptography.decrypt(objectResponse));

                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String getData = jsonObj.getString("Bill");
                    if (!getData.equalsIgnoreCase("0")) {
                        commonRepository.flagStatusUpdate(getData);
                        commonRepository.deletAllTempData();
                        commonRepository.createUnsyncDataFile("hiiii");
                        callLoginApi();

                      /*  ProgressDialog progressDialog1 =new ProgressDialog(LoginActivity.this);
                        progressDialog1.setMessage("Loading....");
                        progressDialog1.setCancelable(false);
                        progressDialog1.show();
                        Handler handler =new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog1.dismiss();
                                fetchLoginDetail(mobileNumber,password);
                            }
                        },2000);*/
                    } else {
                        callLoginApi();
                        //show dialog msg not sync and go to login on ok click

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    appUtility.showLog("Unsyncbillparsingexp" + e, LoginActivity.class);
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                    materialAlertDialogBuilder.setCancelable(false);
                    materialAlertDialogBuilder.setMessage("Bill response Expection:- " + e);
                    materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    materialAlertDialogBuilder.show();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                /*********in case of error data is not synced**********/
                progressDialog.dismiss();
                appUtility.showLog("volley error:-" + error, LoginActivity.class);
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                materialAlertDialogBuilder.setCancelable(false);
                materialAlertDialogBuilder.setMessage("Server error data Not Sync Do you want to login");
                materialAlertDialogBuilder.setPositiveButton("Log in", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        commonRepository.deletAllTempData();
                        callLoginApi();
                    }
                });
                materialAlertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Please try again..", Toast.LENGTH_SHORT).show();
                    }
                });
                materialAlertDialogBuilder.show();
            }

        };
        volleyService.postDataVolley("bill_sync_at_login", AppConstant.SYNC_URL, unsyncbillEncryptedObj, mResultCallBack);
    }


    /***call login api***/
    public void callLoginApi() {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                fetchLoginDetail(mobileNumber, password);
            }
        }, 2000);
    }

    /***Fetch login data***/
    private void fetchLoginDetail(String mobileNumber, String password) {


        if (NetworkFactory.isInternetOn(LoginActivity.this)) {
            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("loading.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            JSONObject masterUrlObject = new JSONObject();
            try {
                masterUrlObject.accumulate("mobile", mobileNumber);
                masterUrlObject.accumulate("melaId", AppConstant.MELA_ID);
                masterUrlObject.accumulate("password", password);
                masterUrlObject.accumulate("imei", imeiNumber);//imeiNumber
                masterUrlObject.accumulate("deviceInfo", deviceInfo);//deviceInfo
                masterUrlObject.accumulate("version", appVersion);
                masterUrlObject.accumulate("date", dateFactory.getTodayDate());
                masterUrlObject.accumulate("logout_time", "");
                masterUrlObject.accumulate("location_coordinate", location);
                masterUrlObject.accumulate("app_login_time", dateFactory.getDateTime());
                masterUrlObject.accumulate("firebase_token", firebaseToken);
                //fc3Ado7kRNy5SBPyRdKcCh:APA91bGSGX2r5yUmf84s2vTZrZT8G8mKQ6Kd9IGkRFFI0ZLAHiD7px1JGwD2TPCgeDcRJHbE-VDiQeWzdCndiN6Tk_0GANk_GkgN3G0NCuJTqt7VGHCXiIHMMA2U5xBovn4cmNMVuaVl
                /****save all device info in preference******/
                appSharedPreferences.setMobile(mobileNumber);
                appSharedPreferences.setDeviceInfo(deviceInfo);
                appSharedPreferences.setImei(imeiNumber);
                appUtility.showLog("masterUrlObject--------------     " + masterUrlObject, LoginActivity.class);
            } catch (JSONException e) {
                e.printStackTrace();
                appUtility.showLog("json making exception:- " + e, LoginActivity.class);
            }
            /***********making jsonObject encrypted**********/
            JSONObject masterObjectUrlEncrypted = new JSONObject();
            try {
                Cryptography cryptography = new Cryptography();
                String encrypted = cryptography.encrypt(masterUrlObject.toString());
                masterObjectUrlEncrypted.accumulate("data", encrypted);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            mResultCallBack = new VolleyResult() {
                @Override
                public void notifySuccess(String requestType, JSONObject response) {
                    progressDialog.dismiss();
                    appUtility.showLog("get response:-" + response, LoginActivity.class);
                    Cryptography cryptography = null;
                    JSONObject jsonObj = null;
                    String objectResponse = "";
                    if (response.has("data")) {
                        try {
                            objectResponse = response.getString("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            appUtility.showLog("LogindataParsingException" + e, LoginActivity.class);
                        }
                    } else {
                        return;
                    }
                    try {
                        cryptography = new Cryptography();
                        jsonObj = new JSONObject(cryptography.decrypt(objectResponse));
                        appUtility.showLog("Decrypted response:-" + jsonObj.toString(), LoginActivity.class);
                        //  AppUtility.getInstance().showLog("LoginResponse"+jsonArr,LoginActivity.class);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //{"data":{"login_attempt":1,"status":"Invalid Login !!!"},"message":"success","status":0}

                    parseGetData(jsonObj);
                }

                @Override
                public void notifyError(String requestType, VolleyError error) {
                    progressDialog.dismiss();
                    appUtility.showLog("volley error:-" + error, LoginActivity.class);
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                    materialAlertDialogBuilder.setCancelable(false);
                    materialAlertDialogBuilder.setMessage("Server error please try again");
                    materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    materialAlertDialogBuilder.show();
                }
            };
            volleyService.postDataVolley("log_in_request", AppConstant.LOGIN_URL, masterObjectUrlEncrypted, mResultCallBack);
        } else {

            noInterNetConnection(LoginActivity.this);

            //show dialogh
        }

    }

    /***no internet connection*****/
    public void noInterNetConnection(Context context) {
        new MaterialAlertDialogBuilder(context).setTitle("Network/Connection error").setIcon(R.drawable.ic_outline_wifi_off_24)
                .setMessage("Check your data connection and refresh")
                .setPositiveButton("setting", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).setNegativeButton("cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        }).show();
    }

    /****get firebase token*****/
    private void getFirebaseToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    appUtility.showLog("Fetching FCM registration token failed   " + task.getException(), LoginActivity.class);
                    return;
                }
                appUtility.showLog("Fetching FCM Token  " + task.getResult(), LoginActivity.class);
                firebaseToken = task.getResult();
                appUtility.showLog("check verify   " + firebaseToken, LoginActivity.class);
            }
        });
    }

    /* @Override
     public void onProviderEnabled(@NonNull String provider) {

     }

     @Override
     public void onProviderDisabled(@NonNull String provider) {

     }

     @Override
     public void onStatusChanged(String provider, int status, Bundle extras) {

     }*/
    public void stopHandler() {
        handler.removeCallbacks(runnable);
    }

    public void startHandler() {
        handler.postDelayed(runnable, 30 * 60 * 1000);
    }
}