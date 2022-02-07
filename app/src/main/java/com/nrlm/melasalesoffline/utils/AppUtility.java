package com.nrlm.melasalesoffline.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtility extends AppCompatActivity {
    public static AppUtility utilsInstance;
    private static boolean wantToShow = true;

    public String removeCommaFromLast(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    public void showLog(String logMsg, Class application) {
        if (wantToShow) {
            Log.d(application.getName(), logMsg);
        }
    }

    public static AppUtility getInstance() {
        if (utilsInstance == null) {
            utilsInstance = new AppUtility();
        }
        return utilsInstance;
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
    public static boolean isInternetOn(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connec.getActiveNetworkInfo();
        if (ni != null && ni.isAvailable() && ni.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private String loadAssetData(String filName) {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open(filName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void replaceFragment(FragmentManager fragmentManager, Fragment fragment, String tag, boolean addTobackStack, int container) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (addTobackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(container, fragment, tag);
        ft.commit();
    }

   /* public void makeIntent(Context context, Class activityToGo, boolean clearBackActitvity) {
        if (context != null) {
            Intent intent = new Intent(context, activityToGo);

            if (clearBackActitvity) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            context.startActivity(intent);
        }
    }*/
    public static boolean isGPSEnabled(Context context) {
        boolean flag = false;
        // getting GPS status
        LocationManager locationManager = (LocationManager) context
                .getSystemService(LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSEnabled;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus()) {
            inputMethodManager.hideSoftInputFromWindow(activity
                    .getCurrentFocus().getWindowToken(), 0);
        }
    }
    public String getRandomOtp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return "" + otp;
    }

    public  boolean isValid(@NonNull String s)
    {
        Pattern p = Pattern.compile("(0/91)?[6-9][0-9]{9}");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

}

