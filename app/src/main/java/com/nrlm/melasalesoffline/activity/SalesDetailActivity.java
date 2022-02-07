package com.nrlm.melasalesoffline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.adapter.HistoryCustomAdapter;
import com.nrlm.melasalesoffline.adapter.ShgBillDetailAdapter;
import com.nrlm.melasalesoffline.pojo.ShgBillModel;
import com.nrlm.melasalesoffline.utils.AppConstant;
import com.nrlm.melasalesoffline.utils.AppSharedPreferences;
import com.nrlm.melasalesoffline.utils.AppUtility;
import com.nrlm.melasalesoffline.utils.Cryptography;
import com.nrlm.melasalesoffline.utils.DateFactory;
import com.nrlm.melasalesoffline.utils.NetworkFactory;
import com.nrlm.melasalesoffline.webService.volley.VolleyResult;
import com.nrlm.melasalesoffline.webService.volley.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesDetailActivity extends AppCompatActivity {

    @BindView(R.id.rv_shgBillData)
    RecyclerView rv_shgBillData;

    @BindView(R.id.tv_errorMessage)
    TextView tv_errorMessage;

    @BindView(R.id.cv_dataNotFound)
    MaterialCardView cv_dataNotFound;

    @BindView(R.id.cv_unsyncDaata)
    MaterialCardView cv_unsyncDaata;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btn_load)
    MaterialButton btn_load;

    @BindView(R.id.btn_home)
    MaterialButton btn_home;





    ShgBillDetailAdapter shgBillDetailAdapter;
    List<ShgBillModel> billListData;
    AppSharedPreferences appSharedPreferences;
    AppUtility appUtility;
    DateFactory dateFactory;
    VolleyService volleyService;

    VolleyResult mResultCallBack=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_detail);
        ButterKnife.bind(this);
        appSharedPreferences = AppSharedPreferences.getsharedprefInstances(SalesDetailActivity.this);
        appUtility = AppUtility.getInstance();
        dateFactory = DateFactory.getInstance();
        volleyService = VolleyService.getInstance(SalesDetailActivity.this);


        TextView tv =toolbar.findViewById(R.id.tbTitle);
        tv.setText("Bill Detail's");

        callBillDetailApi();

        btn_load.setOnClickListener(v -> {
            callBillDetailApi();
        });

        btn_home.setOnClickListener(v -> {
            Intent intent = new Intent(SalesDetailActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void callBillDetailApi() {
        //String todayDate = dateFactory.changeDateValue(dateFactory.getTodayDate());
        String todayDate = dateFactory.changeDateValue(dateFactory.getTodayDate());

        if (NetworkFactory.isInternetOn(SalesDetailActivity.this)) {
            ProgressDialog pDialog = new ProgressDialog(SalesDetailActivity.this);
            pDialog.setMessage(getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("mela_id", AppConstant.MELA_ID);
                jsonObject.accumulate("mobile", appSharedPreferences.getMobile());
                jsonObject.accumulate("imei_no",appSharedPreferences.getImei());
                jsonObject.accumulate("device_name",appSharedPreferences.getDeviceInfo() );
                jsonObject.accumulate("location_coordinate", "");
                jsonObject.accumulate("generated_date", todayDate);

            } catch (JSONException e) {
                appUtility.showLog("json Exception:-:-" +e,SalesDetailActivity.class);
            }


            mResultCallBack = new VolleyResult() {
                @Override
                public void notifySuccess(String requestType, JSONObject response) {
                    pDialog.dismiss();
                    appUtility.showLog("get response:-" +response,SalesDetailActivity.class);

                    try{
                        if(response.has("status")){
                            String status =response.getString("status");
                            if(status.equalsIgnoreCase("data not found")) {
                                cv_unsyncDaata.setVisibility(View.GONE);
                                cv_dataNotFound.setVisibility(View.VISIBLE);

                               /* MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(SalesDetailActivity.this);
                                materialAlertDialogBuilder.setCancelable(false);
                                materialAlertDialogBuilder.setMessage("No history found ...");
                                materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
                                materialAlertDialogBuilder.show();*/
                            }else if(status.equalsIgnoreCase("shg_reg_id Not Found ")){
                                cv_unsyncDaata.setVisibility(View.GONE);
                                cv_dataNotFound.setVisibility(View.VISIBLE);
                            }else if(status.equalsIgnoreCase(" shg_code Not Found ")){
                                cv_unsyncDaata.setVisibility(View.GONE);
                                cv_dataNotFound.setVisibility(View.VISIBLE);
                            }

                        }else if(response.has("shgbilldetail")) {
                            billListData =  new ArrayList<>();
                            JSONArray billDetailArray =  response.getJSONArray("shgbilldetail");
                            for(int i=0;i<billDetailArray.length();i++){
                                JSONObject billObject =  billDetailArray.getJSONObject(i);
                                ShgBillModel shgBillModel =  new ShgBillModel();
                                shgBillModel.setTotalPrice(String.valueOf(billObject.getInt("total_price")));
                                shgBillModel.setShgRegId(billObject.getString("shg_reg_id"));
                                shgBillModel.setMobileNumber(billObject.getString("mobile_number"));
                                shgBillModel.setSumInvoiceNo(billObject.getString("sum_invoice_no"));
                                shgBillModel.setShgName(billObject.getString("shg_group_name"));
                                billListData.add(shgBillModel);
                            }

                            cv_unsyncDaata.setVisibility(View.VISIBLE);
                            cv_dataNotFound.setVisibility(View.GONE);


                            shgBillDetailAdapter = new ShgBillDetailAdapter(billListData, SalesDetailActivity.this);
                            rv_shgBillData.setLayoutManager(new LinearLayoutManager(SalesDetailActivity.this));
                            rv_shgBillData.setAdapter(shgBillDetailAdapter);
                            shgBillDetailAdapter.notifyDataSetChanged();
                        }

                    }catch (Exception e){

                    }

                }

                @Override
                public void notifyError(String requestType, VolleyError error) {
                    pDialog.dismiss();
                    appUtility.showLog("volley error:-" +error,BillHistoyActivity.class);
                     MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(SalesDetailActivity.this);
                                materialAlertDialogBuilder.setCancelable(false);
                                materialAlertDialogBuilder.setMessage("Server Error Please try after some time...");
                                materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
                                materialAlertDialogBuilder.show();
                }
            };
            volleyService.postDataVolley("bill_today_history", AppConstant.GET_ALL_SHG_BILL_DATA_URL,jsonObject,mResultCallBack);
        }else {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(SalesDetailActivity.this);
            materialAlertDialogBuilder.setCancelable(false);
            materialAlertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_outline_wifi_off_24));
            materialAlertDialogBuilder.setMessage("Enable your Internet");
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SalesDetailActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}