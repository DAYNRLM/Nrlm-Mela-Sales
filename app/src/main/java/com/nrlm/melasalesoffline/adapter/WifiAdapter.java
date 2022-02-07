package com.nrlm.melasalesoffline.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nrlm.melasalesoffline.R;

import java.util.List;

public class WifiAdapter extends BaseAdapter {
    private Activity activity;
    private List<ScanResult> scannedResults;
    private View view;

    public WifiAdapter(Activity activity, List<ScanResult> scannedResults){
        this.activity=activity;
        this.scannedResults=scannedResults;
    }

    @Override
    public int getCount() {
        return scannedResults.size();
    }

    @Override
    public Object getItem(int position) {
        return scannedResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_wifi_list_item, null);

        TextView txtWifiName = (TextView) view.findViewById(R.id.txtWifiName);
        txtWifiName.setText(scannedResults.get(position).SSID);

        return view;
    }


    public void setElements(List<ScanResult> mWifis) {
        this.scannedResults = mWifis;
        notifyDataSetChanged();
    }
}
