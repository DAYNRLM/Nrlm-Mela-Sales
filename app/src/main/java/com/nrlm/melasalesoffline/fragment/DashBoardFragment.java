package com.nrlm.melasalesoffline.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.activity.BarcodeActivity;
import com.nrlm.melasalesoffline.activity.BillGenerateActivity;
import com.nrlm.melasalesoffline.activity.HomeActivity;
import com.nrlm.melasalesoffline.activity.LoginActivity;
import com.nrlm.melasalesoffline.activity.SplashScreen;
import com.nrlm.melasalesoffline.adapter.ProductAddCustomAdapter;
import com.nrlm.melasalesoffline.database.entity.BillSyncEntity;
import com.nrlm.melasalesoffline.database.entity.ProductAddTemptableEntity;
import com.nrlm.melasalesoffline.database.entity.ProductDescriptionEntity;
import com.nrlm.melasalesoffline.database.entity.ProductEntity;
import com.nrlm.melasalesoffline.database.repository.CommonRepository;
import com.nrlm.melasalesoffline.intrface.GetTotalPrice;
import com.nrlm.melasalesoffline.intrface.SetDate;
import com.nrlm.melasalesoffline.utils.AppConstant;
import com.nrlm.melasalesoffline.utils.AppSharedPreferences;
import com.nrlm.melasalesoffline.utils.AppUtility;
import com.nrlm.melasalesoffline.utils.Cryptography;
import com.nrlm.melasalesoffline.utils.DateFactory;
import com.nrlm.melasalesoffline.utils.DialogFactory;
import com.nrlm.melasalesoffline.utils.FilesUtils;
import com.nrlm.melasalesoffline.utils.MyDatePicker;
import com.nrlm.melasalesoffline.utils.NetworkFactory;

import com.nrlm.melasalesoffline.utils.PdfUtils;
import com.nrlm.melasalesoffline.utils.PrefrenceManager;
import com.nrlm.melasalesoffline.webService.volley.VolleyResult;
import com.nrlm.melasalesoffline.webService.volley.VolleyService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.OnClick;

public class DashBoardFragment extends BaseFragment {

    @BindView(R.id.generateBillBtn)
    MaterialButton generateBillBtn;

    @BindView(R.id.addItemBtn)
    MaterialButton addItemBtn;

    @BindView(R.id.avlbQntyTv)
    TextView avlbQntyTv;

    @BindView(R.id.totalAmountTv)
    TextView totalAmountTv;

    @BindView(R.id.syncDataTv)
    TextView syncDataTv;

    @BindView(R.id.billCreatedDateBtn)
    MaterialButton materialButton;

    @BindView(R.id.billDateTv)
    TextView billDateTv;

    @BindView(R.id.productNameSpinner)
    AutoCompleteTextView productNameSpinner;

    @BindView(R.id.productDisSpinner)
    AutoCompleteTextView productDisSpinner;

    @BindView(R.id.quentityEt)
    TextInputEditText quentityEt;

    @BindView(R.id.unitPriceEt)
    TextInputEditText unitPriceEt;

    @BindView(R.id.addItemRV)
    RecyclerView addItemRV;
    @BindView(R.id.imgBarcode)
    ImageView imgBarcode;
    DateFactory dateFactory;
    AppUtility appUtility;
    CommonRepository commonRepository;
    AppSharedPreferences appSharedPreferences;
    FilesUtils filesUtils;
    DialogFactory dialogFactory;

    ArrayAdapter<String> productNameAdapter;
    ArrayAdapter<String> productDescAdapter;
    ProductAddCustomAdapter productAddCustomAdapter;

    String billCreatedDate ="";
    String billGeneratedDate ="";
    String billGeneratedTime ="";
    String invoiceBillNumber ="";
    String filePath ="";
    String shgRegId ="";
    String shgCode ="";

    String productId ="";
    String productname ="";
    String avalableQuantity ="";
    String productDescId ="0";
    String productDescName ="";
    String finalBillNumber ="";
    String scanedShgRedId="";
    String scanedProductId="";
    String scanedDescriptionId="";
    String amountFromInterface="";



    int billNo;

    /********get string data from temp table ****/
    String productIDs="";
    String productDescIds="";
    String productQuantitys="";
    String productUnitPrices="";

    GetTotalPrice getTotalPrice;


    List<ProductEntity> productDataItemList;
    List<ProductDescriptionEntity> productDescriptionDataItem;

    VolleyResult mResultCallBack=null;
    SetDate setDate=null;
    VolleyService volleyService;



    public static DashBoardFragment getInstance() {
        DashBoardFragment dasboardFrag = new DashBoardFragment();

        return dasboardFrag;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.dashboard_frag_layout;
    }

    @Override
    public void onFragmentReady() {
        ((HomeActivity) getActivity()).setToolBarTitle("Home");
        dateFactory =DateFactory.getInstance();
        dateFactory =DateFactory.getInstance();
        appUtility =AppUtility.getInstance();
        commonRepository =CommonRepository.getInstance(getContext());
        appSharedPreferences =AppSharedPreferences.getsharedprefInstances(getContext());
        filesUtils  =FilesUtils.getInstance(getContext());
        volleyService =VolleyService.getInstance(getContext());
        dialogFactory =DialogFactory.getInstance(getContext());

        shgRegId =appSharedPreferences.getShgRegId();
        shgCode =appSharedPreferences.getShgCode();
        filePath =appSharedPreferences.getShgFolderPath();

        /*********delete product temp table and update quantity in main table******/
        commonRepository.getTempUpdateProductQuantity();

        /*********by default bill created date is today date*******/
        billDateTv.setText(dateFactory.changeDatePattern(dateFactory.getTodayDate()));
        billCreatedDate =dateFactory.changeDateValue(dateFactory.getTodayDate());

        bindProductData();

        getTotalPrice =new GetTotalPrice() {
            @Override
            public void notifyPrice(String Type, String amount) {
                amountFromInterface =amount;
                totalAmountTv.setText(amount+" Rs.");
            }
        };

        productNameSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productId =productDataItemList.get(position).getProduct_id();
                productname = productDataItemList.get(position).getProduct_name();
               // avalableQuantity =productDataItemList.get(position).getAvailable_quantity();
                avalableQuantity =commonRepository.getQuantity(productId);
                avlbQntyTv.setText(avalableQuantity);

                bindProductDescdata(productId);
            }
        });

        productDisSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productDescId =productDescriptionDataItem.get(position).getProduct_desc_id();
                productDescName =productDescriptionDataItem.get(position).getProduct_type();
            }
        });

        quentityEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String getQuanttity = s.toString();
                if(!getQuanttity.isEmpty()){
                    if(productId.equalsIgnoreCase("")){
                        quentityEt.setText("");
                        Toast.makeText(getContext(),"Select product first",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        if(avalableQuantity.equalsIgnoreCase("")||avalableQuantity.equalsIgnoreCase("N/A")){
                            quentityEt.setText("");
                            Toast.makeText(getContext(),"Available Quantity is N/A",Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                           // int avlIntQuantity = Integer.parseInt(avalableQuantity);
                            avalableQuantity =commonRepository.getQuantity(productId);//added by manish.
                            int avlIntQuantity = Integer.parseInt(avalableQuantity);
                            int getIntQuantity = Integer.parseInt(getQuanttity);
                            if(getIntQuantity>avlIntQuantity){
                                quentityEt.setText("");
                                Toast.makeText(getContext(),"Entered quantity can't be more than available quantity ",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                }
               if(s.toString().length()==1 &&s.toString().startsWith("0"))
               {
                   s.clear();
               Toast.makeText(getContext(),"Product quantity can't be zero",Toast.LENGTH_LONG).show();
               }
            }
        });

        unitPriceEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String entredQuantity = quentityEt.getText().toString().trim();

                String entredunitprice = s.toString();
                if (!entredunitprice.isEmpty()) {
                    if (!entredQuantity.isEmpty()) {

                    } else {
                        unitPriceEt.setText("");
                        Toast.makeText(getContext(), "Enter quantity First..", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(s.toString().length()==1 && s.toString().startsWith("0")){
                    s.clear();
                    Toast.makeText(getContext(),"Unit price can't be zero",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });


    }

    /****************After Fatching the data from the Barcode**********************/
    @Override
    public void onResume() {
        super.onResume();
        scanedShgRedId=appSharedPreferences.getScanRegId();
        scanedProductId=appSharedPreferences.getScanProductId();
        scanedDescriptionId=appSharedPreferences.getScanDescriptionId();
        if ( scanedShgRedId.equals(null)&& scanedProductId.equals(null)||(scanedShgRedId.equalsIgnoreCase("")) && (scanedProductId.equalsIgnoreCase(""))) {
           /* ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyScannedProductid(), "", getContext());
            ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyScannedDescriptionid(), "NA", getContext());
            ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyScannedShgRegId(), "", getContext());*/
            appSharedPreferences.setScanRegID("");
            appSharedPreferences.setScanProductId("");
           appSharedPreferences.setScanDescriptionId("NA");
            return;
        }else
        {
            if(scanedShgRedId.equalsIgnoreCase(shgRegId))
            {
                productId =scanedProductId;                                   //to overpass the validation after the scan
                avalableQuantity =commonRepository.getQuantity(productId);     //available quantity to perform logical opretion
                productname=getProductName(scanedProductId);                   //to set the product name on tamp
                productNameSpinner.setText(productname);
                productNameSpinner.clearFocus();
                avlbQntyTv.setText(getAvailableQuantity(scanedProductId));
                productDisSpinner.setText(getProductDescription(scanedProductId,scanedDescriptionId));
                quentityEt.setText(""+1);
                unitPriceEt.setText(getProductUnitPrice(scanedProductId,scanedDescriptionId));

                clearBarcodeSharedprefrenceValues();
            }else
            {
                new AlertDialog.Builder(getContext())
                        .setTitle("Alert")
                        .setMessage("Scanned product is not register")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {                       //clean all the wrongly scaned value
                                appSharedPreferences.setScanRegID("");
                                appSharedPreferences.setScanProductId("");
                                appSharedPreferences.setScanDescriptionId("NA");
                            }
                        })
                         //   .setNegativeButton(android.R.string.no, null)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    private String getProductUnitPrice(String scanProductId,String scanDescriptionId) {
        return commonRepository.getProductPrice(scanProductId,scanDescriptionId);
    }

    private void clearBarcodeSharedprefrenceValues() {
        appSharedPreferences.removeSharedPrefKey(PrefrenceManager.getPrefKeyScanShgRegId());
        appSharedPreferences.removeSharedPrefKey(PrefrenceManager.getPrefKeyScanProductId());
        appSharedPreferences.removeSharedPrefKey(PrefrenceManager.getPrefKeyScanDescriptionId());
    }
    /***********geting productDescription**************/
    private String getProductDescription(String scanProductId,String scanDescriptionId) {
        return commonRepository.getProductType(scanProductId,scanDescriptionId);
    }

    private String getAvailableQuantity(String scanProductId) {
        return  commonRepository.getQuantity(scanProductId);
    }

    private String getProductName(String scnProductId) {
        return commonRepository.getProductName(scnProductId);
    }

    /*********bind product desc data on product desc spinner**********/
    private void bindProductDescdata(String productId) {
        productDescriptionDataItem = commonRepository.getAllProductDescData(productId);
        if(productDescriptionDataItem.isEmpty()){
            Toast.makeText(getContext(),"Product description is not available",Toast.LENGTH_LONG).show();
            productDisSpinner.setText("NA");
        }else {
            ArrayList<String> prodDescName =new ArrayList<>();
            for(int i=0;i<productDescriptionDataItem.size();i++){
                prodDescName.add(productDescriptionDataItem.get(i).getProduct_type());
            }
            productDescAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, prodDescName);
            productDisSpinner.setAdapter(productDescAdapter);
            productDescAdapter.notifyDataSetChanged();
        }
    }

    /********bind product data on product spinner******/
    private void bindProductData() {
        productDataItemList =commonRepository.getAllProductData();
        ArrayList<String> productnameList =new ArrayList<>();
        for(int i=0;i<productDataItemList.size();i++){
            productnameList.add(productDataItemList.get(i).getProduct_name());
        }
        productNameAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, productnameList);
        productNameSpinner.setAdapter(productNameAdapter);
        productNameAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.billCreatedDateBtn)
    public void selectBillDate(){
         setDate =new SetDate() {
            @Override
            public void notifyDate(String Type, String date) {
               // Toast.makeText(getContext(),"selected date:-"+date,Toast.LENGTH_SHORT).show();
                billDateTv.setText(date);
                billCreatedDate =date;
            }
         };

        DialogFragment dialogFragment = new MyDatePicker(getContext(),setDate);
        dialogFragment.show(getFragmentManager(),"date_picker");
    }
    @OnClick(R.id.addItemBtn)
    public void addItemInList(){
      avalableQuantity =commonRepository.getQuantity(productId);              //added by manish
        String productQuantity = quentityEt.getText().toString().trim();
        String unitPrice = unitPriceEt.getText().toString().trim();
        if(productId.equalsIgnoreCase("")){
            Toast.makeText(getContext(),"Select product first",Toast.LENGTH_SHORT).show();
        }else if(productQuantity.isEmpty()){
            Toast.makeText(getContext(),"Enter product quantity",Toast.LENGTH_SHORT).show();
        }else if(unitPrice.isEmpty()){
            Toast.makeText(getContext(),"Enter unit price",Toast.LENGTH_SHORT).show();
        }else {
            int entredProductQuant = Integer.parseInt(quentityEt.getText().toString().trim());
            int entredUnitProPrice= Integer.parseInt(unitPriceEt.getText().toString().trim());
            int totalPrice = entredProductQuant*entredUnitProPrice;
            int avalableIntQuantity = Integer.parseInt(avalableQuantity);

            /********update avalable quantity in may product table*******/
            int quantityDifference = avalableIntQuantity-entredProductQuant;
            if(quantityDifference>=0){
                commonRepository.updateAvalableQuantity(productId, String.valueOf(quantityDifference));
            }

            /**********insert product data in temp table**************/
            ProductAddTemptableEntity productAddTemptableEntity =new ProductAddTemptableEntity();
            productAddTemptableEntity.setProduct_desc_id(productDescId);
            productAddTemptableEntity.setProduct_desc_Name(productDescName);
            productAddTemptableEntity.setProduct_id(productId);
            productAddTemptableEntity.setProduct_name(productname);
            productAddTemptableEntity.setProduct_quantity(entredProductQuant);
            productAddTemptableEntity.setProduct_unit_price(entredUnitProPrice);
            productAddTemptableEntity.setProduct_total_price(totalPrice);

            productAddTemptableEntity.setShg_code(appSharedPreferences.getShgCode());
            productAddTemptableEntity.setShg_reg_id(appSharedPreferences.getShgRegId());
           /* productAddTemptableEntity.setStall_no(entredUnitProPrice);
            productAddTemptableEntity.setState_short_name(entredUnitProPrice);
            productAddTemptableEntity.setVillage_code(entredUnitProPrice);*/
            commonRepository.saveProductTempData(productAddTemptableEntity);
            /********************set all field null***********************/
            clearFocus(4);
            /***************set all temp data on adapter**********/
            List<ProductAddTemptableEntity> temDataList = commonRepository.getAllTempdata();
            productAddCustomAdapter = new ProductAddCustomAdapter(temDataList, getContext(),getTotalPrice,avlbQntyTv);
            addItemRV.setLayoutManager(new LinearLayoutManager(getContext()));
            addItemRV.setAdapter(productAddCustomAdapter);
            productAddCustomAdapter.notifyDataSetChanged();

            /**********show total sum of price on txt view***********/
            String totalSumAmount = String.valueOf(commonRepository.getTotalPriceSumFromTemp());
            amountFromInterface =totalSumAmount;
            totalAmountTv.setText(totalSumAmount+" Rs.");
        }
    }
    @OnClick(R.id.syncDataTv)
    public void goToSyncData(){

    }

    @OnClick(R.id.imgBarcode)
    public void scanBarCode(){
/*
      AppUtility.getInstance().makeIntent(getContext(), BarcodeActivity.class,false);

      */

        Intent intent=new Intent(getContext(),BarcodeActivity.class);
        startActivity(intent);
    }

    public void goToBillScreen(){
       //  generateBillBtn.setClickable(false);
        billGeneratedDate = dateFactory.changeDateValue(dateFactory.getTodayDate());
        billGeneratedTime =dateFactory.getCurrentTime("HH:mm:ss");

        List<ProductAddTemptableEntity> tempTable =commonRepository.getAllTempdata();
        if(!tempTable.isEmpty()){
            createAndSavePdf();

            getAllIdsFromTemp();

            saveAllDataInLoaclDb();
            try {

                CheckDuplicateBill();

               /*
                if(NetworkFactory.isInternetOn(getContext())){
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("loading.....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    JSONObject jsonObject=null;
                    try{
                        jsonObject = commonRepository.createBillJson();
                    }catch (Exception e){
                        appUtility.showLog("create Bill Json Exception:- "+e,DashBoardFragment.class);
                    }

                    mResultCallBack = new VolleyResult() {
                        @Override
                        public void notifySuccess(String requestType, JSONObject response) {
                            progressDialog.dismiss();
                            appUtility.showLog("get response:-" +response,LoginActivity.class);
                            //  {"Bill":"0","MelaClosedDataNOTSync":"2"}
                            try {
                                String getData =response.getString("Bill");
                                if(!getData.equalsIgnoreCase("0")){
                                    commonRepository.flagStatusUpdate(getData);
                                    commonRepository.deletAllTempData();
                                    commonRepository.createUnsyncDataFile("hiiii");
                                    appSharedPreferences.setSendUrlStatus("done");
                                    Intent intent =new Intent(getContext(), BillGenerateActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }else {
                                    MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(getContext());
                                    materialAlertDialogBuilder.setCancelable(false);
                                    materialAlertDialogBuilder.setMessage("Mela Closed not able to sync bill");
                                    materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            getActivity().finish();
                                            System.exit(0);
                                        }
                                    });
                                    materialAlertDialogBuilder.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(getContext());
                                materialAlertDialogBuilder.setCancelable(false);
                                materialAlertDialogBuilder.setMessage("Network Expection:- "+e);
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
                            *//*********in case of error data is not synced**********//*
                            progressDialog.dismiss();
                            appUtility.showLog("volley error:-" +error,LoginActivity.class);
                            MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(getContext());
                            materialAlertDialogBuilder.setCancelable(false);
                            materialAlertDialogBuilder.setMessage("server error data save in local device");
                            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    commonRepository.deletAllTempData();
                                    createUnsyncDataFile("hhhhhhh");
                                    Intent intent =new Intent(getContext(), BillGenerateActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                            materialAlertDialogBuilder.show();
                        }
                    };
                    volleyService.postDataVolley("bill_sync_project",AppConstant.SYNC_URL,jsonObject,mResultCallBack);

                }else {
                      MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(getContext());
                        materialAlertDialogBuilder.setCancelable(false);
                        materialAlertDialogBuilder.setMessage("Server Error Data Saved offline !!!");
                        materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                generateBillBtn.setClickable(false);
                                dialog.dismiss();
                                commonRepository.deletAllTempData();
                                createUnsyncDataFile("hhhhhhh");
                                Intent intent =new Intent(getContext(), BillGenerateActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        materialAlertDialogBuilder.show();
                }*/

            }catch (Exception e){

                appUtility.showLog("Network expection:  "+e,DashBoardFragment.class);
            }

        }else {
            Toast.makeText(getContext(),getString(R.string.add_the_product_first),Toast.LENGTH_SHORT).show();
        }

    }



    /*******************confirmation date alert on genrateBill button***************/
    @OnClick(R.id.generateBillBtn)
    public void dateAlert() {
        List<ProductAddTemptableEntity> tempTable =commonRepository.getAllTempdata();
        if(!tempTable.isEmpty()) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("Confirm your bill created date " + billCreatedDate);
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            goToBillScreen();             //Method for genrating the bill
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            return;
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }else
        {
            Toast.makeText(getContext(),"Add the product first",Toast.LENGTH_LONG).show();
        }
    }


    private void flagStatusUpdate( String getData) {
        List<BillSyncEntity> billDataList =commonRepository.getBillDataWithStatus("0");

        appUtility.showLog("get Sync Detail:-"+getData,DashBoardFragment.class);
        appUtility.showLog("get bill Sync list size:-"+billDataList.size(),DashBoardFragment.class);

        for(int i=0;i<billDataList.size();i++){
            String invoiceNumber =billDataList.get(i).getInvoice_number();
            commonRepository.updateStatusFlag("1",invoiceNumber);
        }

    }

    private void createAndSavePdf() {
        /******get invoice Number******/
        invoiceBillNumber =appSharedPreferences.getShgInvoiceNumber();
        if(invoiceBillNumber.equalsIgnoreCase("")){
            billNo =0;
            billNo =billNo+1;
            appSharedPreferences.setShgInvoiceNumber(String.valueOf(billNo));
            finalBillNumber = AppConstant.MELA_ID + appSharedPreferences.getShgRegId() + billNo + "-INM";
        }else {
            billNo= Integer.parseInt(invoiceBillNumber);
            billNo =billNo+1;
            appSharedPreferences.setShgInvoiceNumber(String.valueOf(billNo));
            finalBillNumber = AppConstant.MELA_ID + appSharedPreferences.getShgRegId() + billNo + "-INM";
        }

        appSharedPreferences.setFinalBillNo(finalBillNumber);


        String fileName = filePath+"/"+finalBillNumber+".pdf";

        appSharedPreferences.setPdfPath(fileName);
        appSharedPreferences.setInvoiceNumber(finalBillNumber);

        List<ProductAddTemptableEntity> tempTable =commonRepository.getAllTempdata();
        if (tempTable.size()>0){
            PdfUtils.getInstance().createPdfFile(fileName,tempTable,finalBillNumber, billGeneratedDate+ " " +billGeneratedTime);
        }else {
            Toast.makeText(getContext(), getString(R.string.no_item_found_to_create_the_pdf), Toast.LENGTH_SHORT).show();
        }

    }

    private void getAllIdsFromTemp() {
        List<ProductAddTemptableEntity> tempTable =commonRepository.getAllTempdata();

        for(int i=0;i<tempTable.size();i++){
            productIDs=  productIDs + tempTable.get(i).getProduct_id() + ",";
            productDescIds =productDescIds+tempTable.get(i).getProduct_desc_id()+",";
            productQuantitys =productQuantitys+tempTable.get(i).getProduct_quantity()+",";
            productUnitPrices =productUnitPrices+tempTable.get(i).getProduct_unit_price()+",";
        }
        productIDs=removeCommaFromLast(productIDs);
        productDescIds=removeCommaFromLast(productDescIds);
        productQuantitys=removeCommaFromLast(productQuantitys);
        productUnitPrices=removeCommaFromLast(productUnitPrices);

    }

    private void saveAllDataInLoaclDb() {
        BillSyncEntity billSyncEntity =new BillSyncEntity();

        /************create unique bill id*************/
        String createdBy = appSharedPreferences.getMobile();
        String melaId = AppConstant.MELA_ID;
        String shgId = appSharedPreferences.getShgRegId();
        String cretOn = billGeneratedDate + " " + billGeneratedTime;
        String uniqBillNo = finalBillNumber + melaId + shgId + createdBy + cretOn;
        /************************************************/


        billSyncEntity.setBill_created_date(billCreatedDate);
        billSyncEntity.setBill_generated_date(billGeneratedDate);
        billSyncEntity.setBill_generated_time(billGeneratedTime);
        billSyncEntity.setMela_Id(AppConstant.MELA_ID);
        billSyncEntity.setShg_code(appSharedPreferences.getShgCode());
        billSyncEntity.setShg_reg_id(appSharedPreferences.getShgRegId());
        billSyncEntity.setInvoice_number(finalBillNumber);
        billSyncEntity.setProduct_Qty(productQuantitys);
        billSyncEntity.setProduct_Id(productIDs);
        billSyncEntity.setProduct_desc_Id(productDescIds);
        billSyncEntity.setUnit_Price(productUnitPrices);
        billSyncEntity.setStatus_flag("0");
        billSyncEntity.setUnique_bill_number(uniqBillNo);
        billSyncEntity.setTotal_amount(Integer.parseInt(amountFromInterface));
        billSyncEntity.setGenerated_bill_no(billNo);
        billSyncEntity.setLogin_mobile_no(appSharedPreferences.getMobile());
        billSyncEntity.setImei_number(appSharedPreferences.getImei());
        billSyncEntity.setDevice_info(appSharedPreferences.getDeviceInfo());

        commonRepository.saveBillSyncData(billSyncEntity);
    }

    public void clearFocus(int value){
        switch (value){
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                productId = "";
                productname ="";
                productDescId = "0";
                productDescName="";
                quentityEt.setText("");
                unitPriceEt.setText("");
                productNameSpinner.setText("");
                productDisSpinner.setText("");
                avlbQntyTv.setText("");
                unitPriceEt.clearFocus();
                bindProductData();
                break;

        }
    }
    /****************create json object for sync data****************/
    private JSONObject createBillJson() {
        List<BillSyncEntity> billDataList =commonRepository.getBillDataWithStatus("0");
        JSONObject syncDataObject =new JSONObject();
        JSONArray syncDataJSONArray = new JSONArray();

        String productDescriptionKey = "product_description_id";
        String productDescIds = "";

        try {
            for(int i=0;i<billDataList.size();i++){
                JSONObject billObject =new JSONObject();
                productDescIds =billDataList.get(i).getProduct_desc_Id();
                billObject.accumulate("mela_id",AppConstant.MELA_ID);
                billObject.accumulate("bill_id","1" );
                billObject.accumulate("invoice_no",billDataList.get(i).getInvoice_number());
                billObject.accumulate("payment_mode", "Cash/Card/Pos");
                billObject.accumulate("shg_reg_id", appSharedPreferences.getShgRegId());
                billObject.accumulate("bill_no", "");//*****
                billObject.accumulate("generated_date",billDataList.get(i).getBill_generated_date());
                billObject.accumulate("created_date", billDataList.get(i).getBill_created_date());
                billObject.accumulate("bill_time", billDataList.get(i).getBill_generated_time());
                billObject.accumulate(productDescriptionKey, productDescIds);
                billObject.accumulate("product_id",billDataList.get(i).getProduct_Id());
                billObject.accumulate("product_quantity",billDataList.get(i).getProduct_Qty());
                billObject.accumulate("unit_price", billDataList.get(i).getUnit_Price());
                billObject.accumulate("mobile_no", appSharedPreferences.getMobile());
                syncDataJSONArray.put(billObject);

            }

            String syncDataJSONArrayString = syncDataJSONArray.toString();
            syncDataObject.accumulate("BILLOFFLINEDATA", syncDataJSONArrayString);
            syncDataObject.accumulate("melaId",AppConstant.MELA_ID );
            syncDataObject.accumulate("mobile",appSharedPreferences.getMobile() );
            syncDataObject.accumulate("imei_no",appSharedPreferences.getImei());
            syncDataObject.accumulate("device_name",appSharedPreferences.getDeviceInfo());
            syncDataObject.accumulate("location_coordinate","00.00");

        } catch (JSONException e) {
            e.printStackTrace();
            appUtility.showLog("bill create JSOn Exception:- "+e,DashBoardFragment.class);
        }
        return syncDataObject;
    }

    public String removeCommaFromLast(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public void createUnsyncDataFile(String textData){
        if(!filesUtils.isFolderExist(AppConstant.myAppDir)){
            filesUtils.createNewFolder(AppConstant.myAppDir);
        }else {
            String fileName = AppConstant.myAppDir+AppConstant.commoUnsyncFile;
            filesUtils.createBilltextFile(fileName,commonRepository.getUnsyncBillJson().toString());
           /* if(!filesUtils.isFileExist(AppConstant.commoUnsyncFile)){

            }else {
                String fileName = AppConstant.myAppDir+AppConstant.commoUnsyncFile;
                filesUtils.createBilltextFile(fileName,);
            }*/
        }
    }

    public void  CheckDuplicateBill() {
        if (NetworkFactory.isInternetOn(getContext())) {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("loading.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("bill_data", commonRepository.getUniqueBill());
                jsonObject.accumulate("mela_id", AppConstant.MELA_ID);
                jsonObject.accumulate("mobile", appSharedPreferences.getMobile());
                jsonObject.accumulate("imei_no", appSharedPreferences.getImei());
                jsonObject.accumulate("device_name", appSharedPreferences.getDeviceInfo());
                jsonObject.accumulate("location_coordinate", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*******make json object is encrypted and *********/
            JSONObject encryptedObject =new JSONObject();
            try {
                Cryptography cryptography = new Cryptography();
                String encrypted=cryptography.encrypt(jsonObject.toString());
                encryptedObject.accumulate("data",encrypted);
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
                    //{"duplicate_bill":[{"bill_data":"0"}]}
                    //{"duplicate_bill":[{"bill_data":"561419-INM"},{"bill_data":"561420-INM"}]}
                    try {
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

                        if (jsonObj.has("duplicate_bill")) {
                            String billSyncApiStatus = "";
                            JSONArray duplicateArray = jsonObj.getJSONArray("duplicate_bill");
                            for (int i = 0; i < duplicateArray.length(); i++) {
                                JSONObject duplicateObject = duplicateArray.getJSONObject(i);
                                String bill_data = duplicateObject.getString("bill_data");
                                if (bill_data.equalsIgnoreCase("0")) {
                                    billSyncApiStatus = "yes";
                                } else {
                                    commonRepository.updateStatusFlag("1", bill_data);
                                    billSyncApiStatus = "yes";
                                }
                            }

                            if (billSyncApiStatus.equalsIgnoreCase("yes")) {
                                callBillSyncApi();
                            } else {
                                callBillSyncApi();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext());
                        materialAlertDialogBuilder.setCancelable(false);
                        materialAlertDialogBuilder.setMessage("Network Expection:- " + e);
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
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext());
                    materialAlertDialogBuilder.setCancelable(false);
                    materialAlertDialogBuilder.setMessage("server error");
                    materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //callBillSyncApi();
                        }
                    });
                    materialAlertDialogBuilder.show();
                }
            };
            volleyService.postDataVolley("duplicate_bill_api", AppConstant.DUPLICATE_BILL_URL, encryptedObject, mResultCallBack);
        }else {
            MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(getContext());
            materialAlertDialogBuilder.setCancelable(false);
            materialAlertDialogBuilder.setMessage("No internet connectivity data saved offline !!!");
            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    generateBillBtn.setClickable(false);
                    dialog.dismiss();
                    commonRepository.deletAllTempData();
                    createUnsyncDataFile("hhhhhhh");
                    Intent intent =new Intent(getContext(), BillGenerateActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            materialAlertDialogBuilder.show();
        }
    }

    public void callBillSyncApi(){
        try {
            if(NetworkFactory.isInternetOn(getContext())){
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("loading.....");
                progressDialog.setCancelable(false);
                progressDialog.show();
                JSONObject jsonObject=null;
                JSONObject encryptedObject=null;
                try{
                    jsonObject = commonRepository.createBillJson();
                    /*******make json object is encrypted and *********/
             encryptedObject =new JSONObject();
                    try {
                        Cryptography cryptography = new Cryptography();
                        String encrypted=cryptography.encrypt(jsonObject.toString());
                        encryptedObject.accumulate("data",encrypted);
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

                }catch (Exception e){
                    appUtility.showLog("create Bill Json Exception:- "+e,DashBoardFragment.class);
                }

                mResultCallBack = new VolleyResult() {
                    @Override
                    public void notifySuccess(String requestType, JSONObject response) {
                        progressDialog.dismiss();
                        appUtility.showLog("get response:-" +response,LoginActivity.class);
                        //  {"Bill":"0","MelaClosedDataNOTSync":"2"}
                        try {

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
                            String getData =jsonObj.getString("Bill");
                            if(!getData.equalsIgnoreCase("0")){
                                commonRepository.flagStatusUpdate(getData);
                                commonRepository.deletAllTempData();
                                commonRepository.createUnsyncDataFile("hiiii");
                                appSharedPreferences.setSendUrlStatus("done");
                                Intent intent =new Intent(getContext(), BillGenerateActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(getContext());
                                materialAlertDialogBuilder.setCancelable(false);
                                materialAlertDialogBuilder.setMessage("Mela Closed not able to sync bill");
                                materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        getActivity().finish();
                                        System.exit(0);
                                    }
                                });
                                materialAlertDialogBuilder.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(getContext());
                            materialAlertDialogBuilder.setCancelable(false);
                            materialAlertDialogBuilder.setMessage("Network Expection:- "+e);
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
                        appUtility.showLog("volley error:-" +error,LoginActivity.class);
                        MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(getContext());
                        materialAlertDialogBuilder.setCancelable(false);
                        materialAlertDialogBuilder.setMessage("server error data save in local device");
                        materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                commonRepository.deletAllTempData();
                                createUnsyncDataFile("hhhhhhh");
                                Intent intent =new Intent(getContext(), BillGenerateActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        materialAlertDialogBuilder.show();
                    }
                };
                volleyService.postDataVolley("bill_sync_project",AppConstant.SYNC_URL,encryptedObject,mResultCallBack);

            }else {
                MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(getContext());
                materialAlertDialogBuilder.setCancelable(false);
                materialAlertDialogBuilder.setMessage("Data saved offline !!!");
                materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        generateBillBtn.setClickable(false);
                        dialog.dismiss();
                        commonRepository.deletAllTempData();
                        createUnsyncDataFile("hhhhhhh");
                        Intent intent =new Intent(getContext(), BillGenerateActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                materialAlertDialogBuilder.show();
            }

        }catch (Exception e){

            appUtility.showLog("Network expection:  "+e,DashBoardFragment.class);
        }

    }
}
