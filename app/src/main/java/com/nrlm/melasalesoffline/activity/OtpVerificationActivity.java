package com.nrlm.melasalesoffline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.database.repository.CommonRepository;
import com.nrlm.melasalesoffline.utils.AppConstant;
import com.nrlm.melasalesoffline.utils.AppSharedPreferences;
import com.nrlm.melasalesoffline.utils.AppUtility;
import com.nrlm.melasalesoffline.utils.Cryptography;
import com.nrlm.melasalesoffline.utils.DateFactory;
import com.nrlm.melasalesoffline.utils.DeviceInfoutils;
import com.nrlm.melasalesoffline.utils.FilesUtils;
import com.nrlm.melasalesoffline.utils.LocationMasterUtils;
import com.nrlm.melasalesoffline.utils.NetworkFactory;
import com.nrlm.melasalesoffline.webService.customvolley.T;
import com.nrlm.melasalesoffline.webService.volley.VolleyResult;
import com.nrlm.melasalesoffline.webService.volley.VolleyService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtpVerificationActivity extends AppCompatActivity {

    @BindView(R.id.passwordInputLayout)
    TextInputLayout passwordInputLayout;

    @BindView(R.id.confirmPasswordInputLayout)
    TextInputLayout confirmPasswordInputLayout;

    @BindView(R.id.passwordEt)
    TextInputEditText passwordEt;

    @BindView(R.id.confirmPasswordEt)
    TextInputEditText confirmPasswordEt;

    @BindView(R.id.updateBtn)
    MaterialButton updateBtn;

    @BindView(R.id.otpEt)
    PinEntryEditText otpEt;

    @BindView(R.id.otpCardview)
    CardView otpCardview;

    @BindView(R.id.tv_otpMessage)
    TextView tv_otpMessage;








    DeviceInfoutils deviceInfoutils;
    LocationMasterUtils locationMasterUtils;
    DateFactory dateFactory;
    AppUtility appUtility;
    CommonRepository commonRepository;
    AppSharedPreferences appSharedPreferences;
    FilesUtils filesUtils;
    VolleyResult mResultCallBack=null;
    VolleyService volleyService;


    String password ="";
    String confirmPassword ="";
    String getOtp ="";
    String otpStatus ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        ButterKnife.bind(this);

        deviceInfoutils  = DeviceInfoutils.getInstance(OtpVerificationActivity.this);
        locationMasterUtils =LocationMasterUtils.getInstance(OtpVerificationActivity.this);
        dateFactory =DateFactory.getInstance();
        appUtility =AppUtility.getInstance();
        volleyService = VolleyService.getInstance(OtpVerificationActivity.this);
        commonRepository =CommonRepository.getInstance(OtpVerificationActivity.this);
        appSharedPreferences =AppSharedPreferences.getsharedprefInstances(OtpVerificationActivity.this);
        filesUtils=FilesUtils.getInstance(OtpVerificationActivity.this);


        getOtp = appSharedPreferences.getOtp();

        tv_otpMessage.setText("OTP has been sent to (+91-"+appSharedPreferences.getMobile()+")");

        passwordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(passwordEt.getText().toString().length()<6){
                        passwordInputLayout.setError("Enter 6 digit password");
                        passwordEt.setText("");
                    }
                }
            }
        });
        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordInputLayout.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                String getPassword =passwordEt.getText().toString();
                String getConfirmPassword = confirmPasswordEt.getText().toString();

                if(getPassword.length()==6){
                    if(!getConfirmPassword.isEmpty()){
                        if(!getPassword.equalsIgnoreCase(getConfirmPassword)){
                            otpCardview.setVisibility(View.GONE);
                            passwordInputLayout.setError("Password is not Matched");
                        }else {
                            otpCardview.setVisibility(View.VISIBLE);
                            passwordInputLayout.setError(null);
                        }
                    }
                }
            }
        });

        confirmPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmPasswordInputLayout.setError(null);
                String getPassword =passwordEt.getText().toString();
                if(getPassword.equalsIgnoreCase("")||getPassword.isEmpty()){
                    passwordInputLayout.setError("Enter 6 digit password");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String getPassword =passwordEt.getText().toString();
                String getConfirmPassword = confirmPasswordEt.getText().toString();

                if(getConfirmPassword.length()==6){
                    if(!getPassword.equalsIgnoreCase(getConfirmPassword)){
                        otpCardview.setVisibility(View.GONE);
                        passwordInputLayout.setError("Password is not Matched");
                    }else {
                        otpCardview.setVisibility(View.VISIBLE);
                        passwordInputLayout.setError(null);
                    }
                }
            }
        });

        otpEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String getOtpFromEt = otpEt.getText().toString().trim();
                if(getOtpFromEt.length()==4){
                    Handler handler =new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(getOtpFromEt.equalsIgnoreCase(getOtp)){
                                otpStatus ="1";
                                appSharedPreferences.setOtp("");
                            }
                            else {
                                otpEt.setText(null);
                                Toast.makeText(OtpVerificationActivity.this,"Wrong OTP",Toast.LENGTH_SHORT).show();
                            }
                        }
                    },500);
                }
            }
        });
    }


    @OnClick(R.id.updateBtn)
    public void updatePassword(){

        password = passwordEt.getText().toString();
        confirmPassword = confirmPasswordEt.getText().toString();

        if(password.equalsIgnoreCase("")||password.isEmpty()){
            passwordInputLayout.setError("Enter 6 digit password");
        }else if(confirmPassword.equalsIgnoreCase("")||confirmPassword.isEmpty()){
            confirmPasswordInputLayout.setError("Enter confirm Password");
        }else if(otpStatus.equalsIgnoreCase("")){
            Toast.makeText(OtpVerificationActivity.this,"Please enter OTP first....",Toast.LENGTH_SHORT).show();

        }else {
            callApiForUpdate();
        }


    }

    /*********reset password**************/
    private void callApiForUpdate() {

        if(NetworkFactory.isInternetOn(OtpVerificationActivity.this)){
            ProgressDialog progressDialog =new ProgressDialog(OtpVerificationActivity.this);
            progressDialog.setMessage("loading.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            JSONObject masterUrlObject =new JSONObject();
            try {
                masterUrlObject.accumulate("mobile",appSharedPreferences.getMobile());
                masterUrlObject.accumulate("melaId", AppConstant.MELA_ID);
                masterUrlObject.accumulate("password",confirmPassword);

            } catch (JSONException e) {
                e.printStackTrace();
                appUtility.showLog("OTP json making exception:- "+e,LoginActivity.class);
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
                    appUtility.showLog("get response:-" +response,LoginActivity.class);
                    //{"status":"Updated Successfully!!!"}
                    //{"status":"Mobile No. not found!!!"}
                    //{"data":"XJhWHNKFEevGZE8opGyk2NWYCZBXpvP0ABaWQcK5ww1hlcd1QNCZFZJvBzREyhnK"}
                    try{

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


                        parseRestPassworddata( jsonObj);

                    }catch (Exception e){
                        appUtility.showLog("OTP get response Expection:-" +e,LoginActivity.class);
                    }
                }

                @Override
                public void notifyError(String requestType, VolleyError error) {
                    progressDialog.dismiss();
                    appUtility.showLog("volley error:-" +error,LoginActivity.class);
                    MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(OtpVerificationActivity.this);
                    materialAlertDialogBuilder.setCancelable(false);
                    materialAlertDialogBuilder.setMessage("Server error please try again");
                    materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent =new Intent(OtpVerificationActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(OtpVerificationActivity.this,"Password is not update",Toast.LENGTH_SHORT).show();
                        }
                    });
                    materialAlertDialogBuilder.show();
                }
            };
            volleyService.postDataVolley("otp_update_request",AppConstant.RESET_PASSWORD,masterObjectUrlEncrypted,mResultCallBack);
        }
        else {
            MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(OtpVerificationActivity.this);
            materialAlertDialogBuilder.setCancelable(false);
            materialAlertDialogBuilder.setMessage("Please enable Your internet...");
            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            materialAlertDialogBuilder.show();
        }
    }

    private void parseRestPassworddata(JSONObject response) {

        try{
            if(response.has("status")){
                String status =response.getString("status");
                if(status.equalsIgnoreCase("Updated Successfully!!!")){
                    MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(OtpVerificationActivity.this);
                    materialAlertDialogBuilder.setCancelable(false);
                    materialAlertDialogBuilder.setMessage("Password update successfully");
                    materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent =new Intent(OtpVerificationActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(OtpVerificationActivity.this,"Login with new password",Toast.LENGTH_SHORT).show();
                        }
                    });
                    materialAlertDialogBuilder.show();
                }else {
                    MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(OtpVerificationActivity.this);
                    materialAlertDialogBuilder.setCancelable(false);
                    materialAlertDialogBuilder.setMessage("Password is not update try again..");
                    materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent =new Intent(OtpVerificationActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(OtpVerificationActivity.this,"Password is not update",Toast.LENGTH_SHORT).show();
                        }
                    });
                    materialAlertDialogBuilder.show();
                }
            }
        }catch (Exception e){

        }

    }
}