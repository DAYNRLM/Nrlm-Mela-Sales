package com.nrlm.melasalesoffline.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.adapter.WifiAdapter;
import com.nrlm.melasalesoffline.utils.AppConstant;
import com.nrlm.melasalesoffline.utils.AppUtility;
import com.nrlm.melasalesoffline.utils.PrintUtil;

import java.util.ArrayList;
import java.util.List;

public class WifiListActivity extends AppCompatActivity {

    private ListView mListWifi;
    private Button mBtnScan;

    private WifiManager mWifiManager;
    private WifiAdapter adapter;
    private WifiListener mWifiListener;

    private List<ScanResult> mScanResults = new ArrayList<ScanResult>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list);


        mBtnScan = (Button) findViewById(R.id.btnNext);
        mWifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWifiManager.startScan();
                Toast.makeText(getBaseContext(), getString(R.string.scanning), Toast.LENGTH_SHORT).show();
            }
        });
        mListWifi = (ListView) findViewById(R.id.wifiList);

        if (mWifiManager.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(), getString(R.string.wifi_is_disabled_make_it_enabeld), Toast.LENGTH_LONG).show();
            mWifiManager.setWifiEnabled(true);
        }

        mWifiListener = new WifiListener();

        adapter = new WifiAdapter(com.nrlm.melasalesoffline.activity.WifiListActivity.this, mScanResults);
        mListWifi.setAdapter(adapter);

        mListWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                connectToWifi(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            registerReceiver(mWifiListener, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            mWifiManager.startScan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(mWifiListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectToWifi(int position) {

        final ScanResult item = mScanResults.get(position);

        String Capabilities = item.capabilities;
        AppUtility.getInstance().showLog("Capabilities"+Capabilities, com.nrlm.melasalesoffline.activity.WifiListActivity.class);

        if (Capabilities.contains("WPA")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(com.nrlm.melasalesoffline.activity.WifiListActivity.this);
            builder.setTitle(getString(R.string.password));

            final EditText input = new EditText(com.nrlm.melasalesoffline.activity.WifiListActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String m_Text = input.getText().toString();
                    WifiConfiguration wifiConfiguration = new WifiConfiguration();
                    wifiConfiguration.SSID = "\"" + item.SSID + "\"";
                    wifiConfiguration.preSharedKey = "\"" + m_Text + "\"";
                    wifiConfiguration.hiddenSSID = true;
                    wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
                    wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA); // For WPA
                    wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN); // For WPA2
                    wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
                    wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                    int res = mWifiManager.addNetwork(wifiConfiguration);
                    boolean b = mWifiManager.enableNetwork(res, true);

                    finishActivity(wifiConfiguration, res);

                }
            });
            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();


        } else if (Capabilities.contains("WEP")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(com.nrlm.melasalesoffline.activity.WifiListActivity.this);
            builder.setTitle("Title");

            final EditText input = new EditText(com.nrlm.melasalesoffline.activity.WifiListActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String m_Text = input.getText().toString();
                    WifiConfiguration wifiConfiguration = new WifiConfiguration();
                    wifiConfiguration.SSID = "\"" + item.SSID + "\"";
                    wifiConfiguration.wepKeys[0] = "\"" + m_Text + "\"";
                    wifiConfiguration.wepTxKeyIndex = 0;
                    wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    int res = mWifiManager.addNetwork(wifiConfiguration);
                 //   Log.d("WifiPreference", "add Network returned " + res);
                    boolean b = mWifiManager.enableNetwork(res, true);
                 //   Log.d("WifiPreference", "enableNetwork returned " + b);

                    finishActivity(wifiConfiguration, res);
                }
            });
            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        } else {

            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            wifiConfiguration.SSID = "\"" + item.SSID + "\"";
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            int res = mWifiManager.addNetwork(wifiConfiguration);
          //  Log.d("WifiPreference", "add Network returned " + res);
            boolean b = mWifiManager.enableNetwork(res, true);
          //  Log.d("WifiPreference", "enableNetwork returned " + b);

            finishActivity(wifiConfiguration, res);
        }

    }

    private void finishActivity(WifiConfiguration mWifiConfiguration, int networkId) {

        mWifiConfiguration.networkId = networkId;

        PrintUtil.savePrinterConfiguration(com.nrlm.melasalesoffline.activity.WifiListActivity.this, mWifiConfiguration);
        Intent intent = new Intent();
        setResult(AppConstant.RESULT_CODE_PRINTER, intent);
        finish();
    }

    public class WifiListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mScanResults = mWifiManager.getScanResults();
           // Log.e("scan result size ", "" + mScanResults.size());
            adapter.setElements(mScanResults);
        }
    }

}
