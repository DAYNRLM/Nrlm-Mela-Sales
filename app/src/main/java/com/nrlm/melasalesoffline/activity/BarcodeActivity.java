package com.nrlm.melasalesoffline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.utils.AnyOrientationCaptureActivity;
import com.nrlm.melasalesoffline.utils.AppSharedPreferences;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeActivity extends AppCompatActivity {
    private String scannedShgRegId, scannedProductId, scannedDescriptionId;
    AppSharedPreferences appSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        appSharedPreferences = AppSharedPreferences.getsharedprefInstances(BarcodeActivity.this);
     /*   String preferenceValueForDialog = ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyScanFlashLightDialog(),BarcodeActivity.this);
        if(preferenceValueForDialog.equals(null)||preferenceValueForDialog.equalsIgnoreCase("")){

            ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyScanFlashLightDialog(),"flashon",BarcodeActivity.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeActivity.this);
            builder.setTitle("Flash Light On/Off");
            builder.setMessage("Press Volume UP/DOWN for  flash light ON/OFF respectively.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();

        }*/
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt(getString(R.string.scan_barcode_volume_up_down));
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                // Toast.makeText(this, "Product Not Registered", Toast.LENGTH_LONG).show();
                finish();
            } else {
               /* if (result.toString().contains("-")){
                }*/
                try {
                    //  Toast.makeText(this, "Result is:-" + result.getContents(), Toast.LENGTH_LONG).show();
                    String scannedValue[] = result.getContents().split("-");
                    if (scannedValue.length == 2) {
                        scannedShgRegId = scannedValue[0];
                        scannedProductId = scannedValue[1];
                    } else {
                        scannedShgRegId = scannedValue[0];
                        scannedProductId = scannedValue[1];
                        scannedDescriptionId = scannedValue[2];
                        appSharedPreferences.setScanDescriptionId(scannedDescriptionId);
                        // ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyScannedDescriptionid(),scannedDescriptionId,BarcodeActivity.this);
                    }
                    //AppUtility.getInstance().showLog("Barcode Activity"+scannedShgRegId+"//"+scannedProductId,BarcodeActivity.class);
                    appSharedPreferences.setScanProductId(scannedProductId);
                    appSharedPreferences.setScanRegID(scannedShgRegId);
                    //   ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyScannedProductid(),scannedProductId,BarcodeActivity.this);
                    //  ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyScannedShgRegId(),scannedShgRegId,BarcodeActivity.this);
                    //  AppUtility.getInstance().showLog("onBarcode:-" +scannedValue.length,BarcodeActivity.class);
                    //  AppUtility.getInstance().showLog("onBarcode:-" +scannedShgRegId+scannedProductId+scannedDescriptionId,BarcodeActivity.class);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, getString(R.string.product_not_registered), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
