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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.adapter.HistoryCustomAdapter;
import com.nrlm.melasalesoffline.database.repository.CommonRepository;
import com.nrlm.melasalesoffline.pojo.OnlineBillModle;
import com.nrlm.melasalesoffline.pojo.OnlineProductModle;
import com.nrlm.melasalesoffline.utils.AppConstant;
import com.nrlm.melasalesoffline.utils.AppSharedPreferences;
import com.nrlm.melasalesoffline.utils.AppUtility;
import com.nrlm.melasalesoffline.utils.Cryptography;
import com.nrlm.melasalesoffline.utils.DateFactory;
import com.nrlm.melasalesoffline.utils.FilesUtils;
import com.nrlm.melasalesoffline.utils.NetworkFactory;
import com.nrlm.melasalesoffline.webService.volley.VolleyResult;
import com.nrlm.melasalesoffline.webService.volley.VolleyService;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillHistoyActivity extends AppCompatActivity {

    @BindView(R.id.historyRV)
    RecyclerView historyRV;

    @BindView(R.id.bigningHistoryBtn)
    MaterialButton bigningHistoryBtn;

    @BindView(R.id.todayHistoryBtn)
    MaterialButton todayHistoryBtn;

    @BindView(R.id.shgNameTv)
    TextView shgNameTv;

    @BindView(R.id.billHistoryDateTv)
    TextView billHistoryDateTv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    DateFactory dateFactory;
    AppUtility appUtility;
    CommonRepository commonRepository;
    AppSharedPreferences appSharedPreferences;
    FilesUtils filesUtils;
    VolleyService volleyService;

    VolleyResult mResultCallBack=null;

    List<OnlineBillModle> onlineBillModlesDataList;
    List<OnlineProductModle> onlineProductDataList;

    HistoryCustomAdapter onlineBillHistoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_histoy);
        ButterKnife.bind(this);

        dateFactory =DateFactory.getInstance();
        dateFactory =DateFactory.getInstance();
        appUtility =AppUtility.getInstance();
        commonRepository =CommonRepository.getInstance(BillHistoyActivity.this);
        appSharedPreferences =AppSharedPreferences.getsharedprefInstances(BillHistoyActivity.this);
        filesUtils  =FilesUtils.getInstance(BillHistoyActivity.this);
        volleyService = VolleyService.getInstance(BillHistoyActivity.this);

        shgNameTv.setText(commonRepository.getShgName(appSharedPreferences.getShgRegId()));
        setUpToolBar();
        initalizationList();
    }

    private void setUpToolBar() {
        TextView tv =toolbar.findViewById(R.id.tbTitle);
        ImageView iv =toolbar.findViewById(R.id.iconbackImg);

      iv.setVisibility(View.VISIBLE);
        tv.setText("Sales history");
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  startActivity(new Intent(BillHistoyActivity.this,HomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });
    }

    private void initalizationList() {
        onlineBillModlesDataList=new ArrayList<>();
    }

    @OnClick(R.id.todayHistoryBtn)
    public void getTodayHistory(){
        String todayDate = dateFactory.changeDateValue(dateFactory.getTodayDate());
        billHistoryDateTv.setText(getString(R.string.history_for)+" "+dateFactory.changeDatePattern(dateFactory.getTodayDate()));

        if(NetworkFactory.isInternetOn(BillHistoyActivity.this)){
            final ProgressDialog pDialog = new ProgressDialog(BillHistoyActivity.this);
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
                jsonObject.accumulate("shg_reg_id",appSharedPreferences.getShgRegId());
                jsonObject.accumulate("generated_date", todayDate);

            } catch (JSONException e) {
                appUtility.showLog("json Exception:-:-" +e,BillHistoyActivity.class);
            }

            /**********encrypting history object******************/
            JSONObject historyTodayEncryptedObj =new JSONObject();
            try {
                Cryptography cryptography = new Cryptography();
                String encrypted=cryptography.encrypt(jsonObject.toString());
                historyTodayEncryptedObj.accumulate("data",encrypted);
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
                    pDialog.dismiss();
                    appUtility.showLog("get response:-" +response,BillHistoyActivity.class);

                    try{
                        Cryptography cryptography = null;
                        JSONObject    jsonObj=null;
                        String objectResponse="";
                        if(response.has("data")){
                            objectResponse=response.getString("data");
                        }else {
                            return;
                        }
                        try {
                            cryptography = new Cryptography();
                            jsonObj = new JSONObject(cryptography.decrypt(objectResponse));

                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        if(jsonObj.has("status")){
                            String status =jsonObj.getString("status");
                            if(status.equalsIgnoreCase("data not found")) {
                                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(BillHistoyActivity.this);
                                materialAlertDialogBuilder.setCancelable(false);
                                materialAlertDialogBuilder.setMessage("No history found ...");
                                materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
                                materialAlertDialogBuilder.show();
                            }

                        }else {
                            parseBillHistoryData(jsonObj,pDialog);
                        }

                    }catch (Exception e){

                    }

                }

                @Override
                public void notifyError(String requestType, VolleyError error) {
                    pDialog.dismiss();
                    appUtility.showLog("volley error:-" +error,BillHistoyActivity.class);
                    //show error dialog
                }
            };
            volleyService.postDataVolley("bill_today_history", AppConstant.BILL_HISTORY_URL,historyTodayEncryptedObj,mResultCallBack);

        }else {
            MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(BillHistoyActivity.this);
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



    @OnClick(R.id.bigningHistoryBtn)
    public void getBigningHistory(){
        String todayDate = "all";
        billHistoryDateTv.setText(getString(R.string.history_from)+" "+dateFactory.changeDatePattern(dateFactory.changeFormate(appSharedPreferences.getMelaFrom()))+" to "+dateFactory.changeDatePattern(dateFactory.getTodayDate()));

        if(NetworkFactory.isInternetOn(BillHistoyActivity.this)){
            final ProgressDialog pDialog = new ProgressDialog(BillHistoyActivity.this);
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
                jsonObject.accumulate("shg_reg_id",appSharedPreferences.getShgRegId());
                jsonObject.accumulate("generated_date", todayDate);

            } catch (JSONException e) {
                e.printStackTrace();
                appUtility.showLog("json Exception:-:-" +e,BillHistoyActivity.class);

            }
            /**********encrypting beginning history object******************/
            JSONObject historybeginningEncryptedObj =new JSONObject();
            try {
                Cryptography cryptography = new Cryptography();
                String encrypted=cryptography.encrypt(jsonObject.toString());
                historybeginningEncryptedObj.accumulate("data",encrypted);
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
                    pDialog.dismiss();
                    appUtility.showLog("get response:-" +response,BillHistoyActivity.class);
                    try{
                        Cryptography cryptography = null;
                        JSONObject    jsonObj=null;
                        String objectResponse="";
                        if(response.has("data")){
                            objectResponse=response.getString("data");
                        }else {
                            return;
                        }
                        try {
                            cryptography = new Cryptography();
                            jsonObj = new JSONObject(cryptography.decrypt(objectResponse));

                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        if(jsonObj.has("status")){
                            String status =response.getString("status");
                            if(status.equalsIgnoreCase("data not found")) {
                                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(BillHistoyActivity.this);
                                materialAlertDialogBuilder.setCancelable(false);
                                materialAlertDialogBuilder.setMessage("No history found ...");
                                materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
                                materialAlertDialogBuilder.show();
                            }

                        }else {
                            parseBillHistoryData(jsonObj,pDialog);
                        }

                    }catch (Exception e){

                    }
                }

                @Override
                public void notifyError(String requestType, VolleyError error) {
                    pDialog.dismiss();
                    appUtility.showLog("volley error:-" +error,BillHistoyActivity.class);
                    //show error dialog
                }
            };
            volleyService.postDataVolley("bill_today_history", AppConstant.BILL_HISTORY_URL,historybeginningEncryptedObj,mResultCallBack);

        }else {
            MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(BillHistoyActivity.this);
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


    private void parseBillHistoryData(JSONObject response,ProgressDialog pDialog) {
        //progress dialog dismiss
        onlineBillModlesDataList.clear();
        try {

            JSONArray jsonArray=response.getJSONArray("billdetail");
            for(int i=0;i<jsonArray.length();i++){
                onlineProductDataList =new ArrayList<>();
                JSONObject billObject =jsonArray.getJSONObject(i);
                OnlineBillModle onlineBillModle =new OnlineBillModle();
                String bill_id = billObject.getString("bill_id");
                String payment_mode =billObject.getString("payment_mode");
                String invoice_no =billObject.getString("invoice_no");
                String shg_reg_id =billObject.getString("shg_reg_id");
                String  mela_id=billObject.getString("mela_id");
                String  get_generated_date=billObject.getString("generated_date");
                JSONArray productArray =billObject.getJSONArray("product_detail");
                for(int k=0;k<productArray.length();k++){

                    OnlineProductModle onlineProductModle =new OnlineProductModle();
                    JSONObject productObject =productArray.getJSONObject(k);

                    String product_id=productObject.getString("product_id");
                    String product_description_id=productObject.getString("product_description_id");
                    String product_price=productObject.getString("product_price");
                    String created_date=productObject.getString("created_date");
                    String product_quantity=productObject.getString("product_quantity");

                    onlineProductModle.setProduct_id(product_id);
                    onlineProductModle.setProduct_description_id(product_description_id);
                    onlineProductModle.setProduct_price(product_price);
                    onlineProductModle.setCreated_date(created_date);
                    onlineProductModle.setProduct_quantity(product_quantity);

                    onlineProductDataList.add(onlineProductModle);
                }
                onlineBillModle.setBill_id(bill_id);
                onlineBillModle.setInvoice_no(invoice_no);
                onlineBillModle.setPaymentMode(payment_mode);
                onlineBillModle.setShg_reg_id(shg_reg_id);
                onlineBillModle.setMela_id(mela_id);
                onlineBillModle.setGenerated_date(get_generated_date);
                onlineBillModle.setOnlineProduct(onlineProductDataList);

                onlineBillModlesDataList.add(onlineBillModle);
            }
            pDialog.dismiss();
            onlineBillHistoryAdapter = new HistoryCustomAdapter(BillHistoyActivity.this, onlineBillModlesDataList);
            historyRV.setLayoutManager(new LinearLayoutManager(BillHistoyActivity.this));
            historyRV.setAdapter(onlineBillHistoryAdapter);
            onlineBillHistoryAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}