package com.nrlm.melasalesoffline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.database.repository.CommonRepository;
import com.nrlm.melasalesoffline.firebase.MyFirebaseMessagingService;
import com.nrlm.melasalesoffline.utils.AppConstant;
import com.nrlm.melasalesoffline.utils.AppSharedPreferences;
import com.nrlm.melasalesoffline.utils.AppUtility;
import com.nrlm.melasalesoffline.utils.DateFactory;
import com.nrlm.melasalesoffline.utils.FilesUtils;
import com.nrlm.melasalesoffline.utils.PermissionHelper;

import java.io.File;
import java.util.Date;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

public class SplashScreen extends AppCompatActivity {

    private boolean checkPermision;
    AppSharedPreferences appSharedPreferences;
    FilesUtils filesUtils;
    DateFactory dateFactory;
    CommonRepository commonRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        appSharedPreferences = AppSharedPreferences.getsharedprefInstances(SplashScreen.this);
        filesUtils = FilesUtils.getInstance(SplashScreen.this);
        dateFactory = DateFactory.getInstance();
        commonRepository = CommonRepository.getInstance(SplashScreen.this);

        checkPermision = PermissionHelper.getInstance(SplashScreen.this).checkAndRequestPermissions();
        AppUtility.getInstance().showLog("checkPermision" + checkPermision, SplashScreen.class);

        /*******create folder if not exist***********/
        if (!filesUtils.isFolderExist(AppConstant.myAppDir)) {
            filesUtils.createNewFolder(AppConstant.myAppDir);
        }

        dateCheckForLogout();
        if (checkPermision) {
            Toast.makeText(SplashScreen.this, getString(R.string.permission_done), Toast.LENGTH_SHORT).show();
            loadNextScreenWithDelay();
        } else {
            Toast.makeText(SplashScreen.this, "Please allow all permissions ", Toast.LENGTH_SHORT).show();
        }


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.d(MyFirebaseMessagingService.TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }
                // Get new FCM registration token
                String token = task.getResult();

                Log.d(MyFirebaseMessagingService.TAG, "token in splash " + token);


            }
        });
    }

  /*  @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try{
            AppUtility.getInstance().showLog("TRY---CATCH----" + "inside try...", SplashScreen.class);
           // String tttt  = getIntent().getExtras().getString("pushnotification");


            intent.hasExtra("pushnotification");
            AppUtility.getInstance().showLog("***intent.hasExtra()****" +intent.hasExtra("pushnotification") , SplashScreen.class);

           *//* boolean check = getIntent().getExtras().containsKey("pushnotification");
            AppUtility.getInstance().showLog("TRY---CATCH****STRING----" +check , SplashScreen.class);*//*

            if(getIntent().hasExtra("pushnotification")){
                AppUtility.getInstance().showLog("TRY---CATCH----" + "get pushnotification", SplashScreen.class);
                Intent intent1 = new Intent(SplashScreen.this, SalesDetailActivity.class);
                startActivity(intent1);
                finish();
            }else {
                AppUtility.getInstance().showLog("TRY---CATCH----" + "not  get", SplashScreen.class);
            }

        }catch (Exception e){
            AppUtility.getInstance().showLog("TRY---CATCH----" +e, SplashScreen.class);
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.getInstance(SplashScreen.this).requestPermissionResult(PermissionHelper.REQUEST_ID_MULTIPLE_PERMISSIONS, permissions, grantResults);
        checkPermision = true;
        if (checkPermision) {
            Toast.makeText(SplashScreen.this, getString(R.string.permission_done), Toast.LENGTH_SHORT).show();
            loadNextScreenWithDelay();

        } else {
            Toast.makeText(SplashScreen.this, getString(R.string.please_allow_the_permission), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNextScreenWithDelay() {
        android.os.Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToNextAndClearPreviousScreens(LoginActivity.class);
            }
        }, 3000);
    }

    private void moveToNextAndClearPreviousScreens(Class<LoginActivity> loginActivityClass) {
        if (appSharedPreferences.getLoginStatus().equalsIgnoreCase("")) {
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            intent.putExtra("testttt", "check");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else if (appSharedPreferences.getMpinStatus().equalsIgnoreCase("")) {
            Intent intent = new Intent(SplashScreen.this, CreateMpinActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashScreen.this, VerifyMpinActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void dateCheckForLogout() {
        String sessionDate = appSharedPreferences.getSessionDate();
        if (sessionDate.equalsIgnoreCase("")) {
            String saveTodayDate = dateFactory.getTodayDate(); //get string type date in (dd-MM-yyyy)
            appSharedPreferences.setSessionDate(saveTodayDate);
        } else {
            Date getSessionDate = dateFactory.getDateFormate(sessionDate);//get date in (dd-MM-yyyy)
            Date getTodayDate = dateFactory.getDateFormate(dateFactory.getTodayDate());

            if (getTodayDate.compareTo(getSessionDate) > 0) {
                saveAndClearData();

            } else if (getTodayDate.compareTo(getSessionDate) < 0) {
                saveAndClearData();
            }
        }
    }

    public void saveAndClearData() {
        String saveTodayDate = dateFactory.getTodayDate(); //get string type date in (dd-MM-yyyy)
        appSharedPreferences.setSessionDate(saveTodayDate);
        appSharedPreferences.setLogoutTime(dateFactory.getDateTime());// get time stramp (yyyy-MM-dd HH:mm:ss)
        appSharedPreferences.clearAllKeyPref();
        commonRepository.deletAllTempData();
        commonRepository.deleteAllData();

    }

}